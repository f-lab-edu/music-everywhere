package me.kong.groupservice.service;

import me.kong.commonlibrary.exception.auth.UnAuthorizedException;
import me.kong.commonlibrary.exception.common.DuplicateElementException;
import me.kong.commonlibrary.util.JwtReader;
import me.kong.groupservice.common.exception.NoLoggedInProfileException;
import me.kong.groupservice.domain.entity.GroupJoinRequest.GroupJoinRequest;
import me.kong.groupservice.domain.entity.GroupJoinRequest.JoinResponse;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.group.GroupScope;
import me.kong.groupservice.domain.entity.group.JoinCondition;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.dto.request.GroupJoinProcessDto;
import me.kong.groupservice.dto.request.GroupJoinRequestDto;
import me.kong.groupservice.dto.request.enums.GroupJoinProcessAction;
import me.kong.groupservice.service.strategy.Consumers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupJoinFacadeTest {

    @InjectMocks
    GroupJoinFacade groupJoinFacade;

    @Mock
    GroupJoinRequestService joinRequestService;

    @Mock
    GroupService groupService;

    @Mock
    ProfileService profileService;

    @Mock
    Consumers consumers;

    @Mock
    JwtReader jwtReader;


    Group group;
    Profile profile;
    GroupJoinRequest joinRequest;
    GroupJoinRequestDto joinRequestDto;
    GroupJoinProcessDto joinProcessDto;

    Long userId = 1L;
    Long groupId = 1L;
    Long requestId = 1L;
    String groupName = "groupName";
    String desc = "desc";
    String nickname = "nickname";
    String requestInfo = "requestInfo";

    @BeforeEach
    void init() {
        group = mock(Group.class);
        profile = mock(Profile.class);
    }

    @Test
    @DisplayName("대기중인 가입 요청이 있다면 예외가 발생한다")
    void failToJoinByPendingRequestExist() {
        //given
        when(group.getId()).thenReturn(groupId);
        when(groupService.findGroupById(groupId)).thenReturn(group);
        when(joinRequestService.pendingRequestExists(groupId)).thenReturn(true);

        //then
        assertThrows(DuplicateElementException.class, () -> groupJoinFacade.joinGroup(joinRequestDto, groupId));
    }

    @Test
    @DisplayName("OPEN 그룹에 새로 가입 요청 시 새로운 프로필이 등록된다")
    void successToJoinOpenGroup() {
        //given
        firstJoinSetting(JoinCondition.OPEN);
        when(jwtReader.getUserId()).thenReturn(userId);

        //when
        groupJoinFacade.joinGroup(joinRequestDto, groupId);

        //then
        verify(group, times(1)).increaseProfileCount();
        verify(profileService)
                .createNewProfile(joinRequestDto.getNickname(), userId, GroupRole.MEMBER, group);
    }

    @Test
    @DisplayName("APPROVAL_REQUIRED 그룹에 새로 가입 요청 시 GroupJoinRequest가 생성된다")
    void successToJoinApprovalRequired() {
        //given
        firstJoinSetting(JoinCondition.APPROVAL_REQUIRED);

        //when
        groupJoinFacade.joinGroup(joinRequestDto, groupId);

        //then
        verify(joinRequestService, times(1)).createNewGroupJoinRequest(joinRequestDto, group);
    }

    @Test
    @DisplayName("OPEN 그룹 가입 시 탈퇴한 전적이 존재하면 이전 프로필을 되살린다")
    void successToJoinWithPreviousProfile() {
        //given
        joinWithProfileSetting(State.DELETED, JoinCondition.OPEN);

        //when
        groupJoinFacade.joinGroup(joinRequestDto, groupId);

        //then
        assertEquals(profile.getState(), State.GENERAL);
        verify(group, times(1)).increaseProfileCount();
    }

    @Test
    @DisplayName("APPROVAL_REQUIRED 그룹에 탈퇴한 전적이 존재하면 GroupJoinRequest가 생성된다")
    void successToJoinApprovalRequiredWithPreviousProfile() {
        //given
        joinWithProfileSetting(State.DELETED, JoinCondition.APPROVAL_REQUIRED);

        //when
        groupJoinFacade.joinGroup(joinRequestDto, groupId);

        //then
        verify(joinRequestService, times(1)).createNewGroupJoinRequest(joinRequestDto, group);
    }

    @Test
    @DisplayName("추방당한 전적이 있다면 예외가 발생한다")
    void failToJoinByPreviousBanRecord() {
        //given
        joinWithProfileSetting(State.RESTRICTED, JoinCondition.OPEN);

        //then
        assertThrows(UnAuthorizedException.class, () -> groupJoinFacade.joinGroup(joinRequestDto, groupId));
    }

    @Test
    @DisplayName("이미 가입한 그룹에 요청 시 예외가 발생한다")
    void failedByAlreadyJoinGroup() {
        //given
        joinWithProfileSetting(State.GENERAL, JoinCondition.OPEN);

        //then
        assertThrows(DuplicateElementException.class, () -> groupJoinFacade.joinGroup(joinRequestDto, groupId));
    }


    private void groupJoinConsumerSetting(State state, JoinCondition joinCondition) {
        BiConsumer<Profile, GroupJoinRequestDto> restricted = (p, dto) -> {
            throw new UnAuthorizedException("추방당한 회원입니다. userId : " + profile.getUserId());
        };
        BiConsumer<Profile, GroupJoinRequestDto> general = (p, dto) -> {
            throw new DuplicateElementException("이미 가입한 그룹입니다.");
        };
        BiConsumer<Profile, GroupJoinRequestDto> deleted = (p, dto) -> {
            Group group = p.getGroup();
            if (group.getJoinCondition() == JoinCondition.OPEN) {
                groupService.checkGroupSize(group);
                group.increaseProfileCount();
                p.setState(State.GENERAL);
            } else {
                joinRequestService.createNewGroupJoinRequest(dto, group);
            }
        };

        if (state == State.DELETED) {
            when(consumers.getGroupJoinConsumer(any(State.class))).thenReturn(deleted);
            when(group.getJoinCondition()).thenReturn(joinCondition);
        } else if (state == State.RESTRICTED) {
            when(consumers.getGroupJoinConsumer(any(State.class))).thenReturn(restricted);
        } else if (state == State.GENERAL) {
            when(consumers.getGroupJoinConsumer(any(State.class))).thenReturn(general);
        }
    }

    private void joinWithProfileSetting(State state, JoinCondition joinCondition) {
        joinGroupSetting();
        profile = makeProfile(GroupRole.MEMBER, state);
        when(profileService.getLoggedInProfile(groupId)).thenReturn(profile);
        groupJoinConsumerSetting(state, joinCondition);
    }

    private void firstJoinSetting(JoinCondition joinCondition) {
        joinGroupSetting();
        when(group.getJoinCondition()).thenReturn(joinCondition);
        when(profileService.getLoggedInProfile(groupId)).thenThrow(NoLoggedInProfileException.class);
    }

    private void joinGroupSetting() {
        joinRequestDto = makeJoinRequestDto();
        when(group.getId()).thenReturn(groupId);
        when(groupService.findGroupById(groupId)).thenReturn(group);
        when(joinRequestService.pendingRequestExists(groupId)).thenReturn(false);
    }



    @Test
    @DisplayName("가입 요청이 이미 처리되었다면 예외가 발생한다")
    void failedByAlreadyProcessed() {
        //given
        processJoinRequestSetting(GroupJoinProcessAction.APPROVE, JoinResponse.APPROVED);

        // then
        assertThrows(DuplicateElementException.class,
                () -> groupJoinFacade.processGroupJoinRequest(requestId, joinProcessDto));
    }

    @Test
    @DisplayName("가입 승인 시, 가입 요청 상태가 승인으로 변경되고 새로운 프로필을 생성한다")
    void successToApproveGroupJoinRequest() {
        //given
        processJoinRequestConsumerSetting(GroupJoinProcessAction.APPROVE);

        // when
        groupJoinFacade.processGroupJoinRequest(requestId, joinProcessDto);

        // then
        assertEquals(joinRequest.getResponse(), JoinResponse.APPROVED);
        verify(profileService, times(1)).createNewProfile(nickname, userId, GroupRole.MEMBER, group);
    }

    @Test
    @DisplayName("가입 거절 시, 가입 요청 상태가 거절로 변경된다")
    void successToRejectGroupJoinRequest() {
        //given
        processJoinRequestConsumerSetting(GroupJoinProcessAction.REJECT);

        //when
        groupJoinFacade.processGroupJoinRequest(requestId, joinProcessDto);

        //then
        assertEquals(joinRequest.getResponse(), JoinResponse.REJECTED);
    }


    private void processJoinRequestConsumerSetting(GroupJoinProcessAction action) {
        Consumer<GroupJoinRequest> approve = req -> {
            Group group = req.getGroup();
            groupService.checkGroupSize(group);
            group.increaseProfileCount();

            req.approveJoinRequest();
            profileService.createNewProfile(req.getNickname(), req.getUserId(), GroupRole.MEMBER, req.getGroup());
        };
        Consumer<GroupJoinRequest> reject = GroupJoinRequest::rejectJoinRequest;

        if (action == GroupJoinProcessAction.APPROVE) {
            when(consumers.getJoinRequestConsumer(any(GroupJoinProcessAction.class))).thenReturn(approve);
        } else if (action == GroupJoinProcessAction.REJECT) {
            when(consumers.getJoinRequestConsumer(any(GroupJoinProcessAction.class))).thenReturn(reject);
        }
        processJoinRequestSetting(action, JoinResponse.PENDING);
    }


    private void processJoinRequestSetting(GroupJoinProcessAction action, JoinResponse joinResponse) {
        joinProcessDto = makeProcessDto(action);
        joinRequest = makeJoinRequest(joinResponse);
        when(joinRequestService.getGroupJoinRequestByRequestId(requestId)).thenReturn(joinRequest);
    }

    private Profile makeProfile(GroupRole groupRole, State state) {
        return Profile.builder()
                .nickname(nickname)
                .groupRole(groupRole)
                .state(state)
                .group(group)
                .userId(userId)
                .build();
    }

    private GroupJoinRequestDto makeJoinRequestDto() {
        return GroupJoinRequestDto.builder()
                .nickname(nickname)
                .requestInfo(requestInfo)
                .build();
    }

    private GroupJoinProcessDto makeProcessDto(GroupJoinProcessAction action) {
        return GroupJoinProcessDto.builder()
                .action(action)
                .build();
    }

    private GroupJoinRequest makeJoinRequest(JoinResponse response) {
        return GroupJoinRequest.builder()
                .nickname(nickname)
                .response(response)
                .group(group)
                .userId(userId)
                .build();
    }
}