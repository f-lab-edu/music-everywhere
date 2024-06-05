package me.kong.groupservice.service;

import me.kong.commonlibrary.exception.auth.UnAuthorizedException;
import me.kong.commonlibrary.exception.common.DuplicateElementException;
import me.kong.groupservice.common.exception.NoLoggedInProfileException;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.group.GroupScope;
import me.kong.groupservice.domain.entity.group.JoinCondition;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.domain.repository.GroupRepository;
import me.kong.groupservice.dto.request.GroupJoinRequestDto;
import me.kong.groupservice.dto.request.SaveGroupRequestDto;
import me.kong.groupservice.mapper.GroupMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @InjectMocks
    GroupService groupService;

    @Mock
    GroupRepository groupRepository;

    @Mock
    ProfileService profileService;

    @Mock
    GroupJoinRequestService joinRequestService;

    @Mock
    GroupMapper groupMapper;

    SaveGroupRequestDto dto;
    GroupJoinRequestDto joinRequestDto;
    Group group;
    Profile profile;

    String groupName;
    String description;
    String nickname;

    @BeforeEach
    void init() {
        dto = mock(SaveGroupRequestDto.class);
        joinRequestDto = GroupJoinRequestDto.builder()
                .nickname("testUser")
                .build();
        group = mock(Group.class);
        groupName = "test group";
        description = "테스트 그룹입니다";
        nickname = "testNickname";
    }

    @Test
    @DisplayName("그룹 생성에 성공한다")
    void successToCreateNewGroup() {
        //given
        when(groupMapper.toEntity(any(SaveGroupRequestDto.class))).thenReturn(group);

        //when
        groupService.createNewGroup(dto);

        //then
        verify(groupRepository, times(1)).save(any(Group.class));
        verify(groupMapper, times(1)).toEntity(any(SaveGroupRequestDto.class));
    }

    @Test
    @DisplayName("데이터베이스 문제가 있을 경우 예외가 발생한다")
    void failToCreateNewGroupByDatabaseProblem() {
        //given
        when(groupRepository.save(any())).thenThrow(RuntimeException.class);

        //then
        assertThrows(RuntimeException.class, () -> groupService.createNewGroup(dto));
    }


    @Test
    @DisplayName("OPEN 그룹에 새로 가입 요청 시 새로운 프로필이 등록된다")
    void successToJoinOpenGroup() {
        //given
        group = makeGroup(JoinCondition.OPEN);

        when(joinRequestService.pendingRequestExists(any())).thenReturn(false);
        when(profileService.getLoggedInProfile(any())).thenThrow(NoLoggedInProfileException.class);
        when(groupRepository.findById(any())).thenReturn(Optional.of(group));

        //when
        groupService.joinGroup(joinRequestDto, any(Long.class));

        //then
        verify(profileService, times(1))
                .createNewProfile(joinRequestDto.getNickname(), GroupRole.MEMBER, group);
    }

    @Test
    @DisplayName("APPROVAL_REQUIRED 그룹에 새로 가입 요청 시 GroupJoinRequest가 생성된다")
    void successToJoinApprovalRequired() {
        //given
        group = makeGroup(JoinCondition.APPROVAL_REQUIRED);

        when(joinRequestService.pendingRequestExists(any())).thenReturn(false);
        when(profileService.getLoggedInProfile(any())).thenThrow(NoLoggedInProfileException.class);
        when(groupRepository.findById(any())).thenReturn(Optional.of(group));

        //when
        groupService.joinGroup(joinRequestDto, any(Long.class));

        //then
        verify(joinRequestService, times(1))
                .createNewGroupJoinRequest(any(GroupJoinRequestDto.class), any(Group.class));
    }

    @Test
    @DisplayName("OPEN 그룹 가입 시 탈퇴한 전적이 존재하면 이전 프로필을 되살린다")
    void successToJoinWithPreviousProfile() {
        //given
        group = makeGroup(JoinCondition.OPEN);
        profile = makeProfile(GroupRole.MEMBER, State.DELETED);

        when(joinRequestService.pendingRequestExists(any())).thenReturn(false);
        when(profileService.getLoggedInProfile(any())).thenReturn(profile);
        when(groupRepository.findById(any())).thenReturn(Optional.of(group));

        //when
        groupService.joinGroup(joinRequestDto, any(Long.class));

        //then
        assertEquals(State.GENERAL, profile.getState());
    }

    @Test
    @DisplayName("APPROVAL_REQUIRED 그룹에 탈퇴한 전적이 존재하면 GroupJoinRequest가 생성된다")
    void successToJoinApprovalRequiredWithPreviousProfile() {
        //given
        group = makeGroup(JoinCondition.APPROVAL_REQUIRED);
        profile = makeProfile(GroupRole.MEMBER, State.DELETED);

        when(joinRequestService.pendingRequestExists(any())).thenReturn(false);
        when(profileService.getLoggedInProfile(any())).thenReturn(profile);
        when(groupRepository.findById(any())).thenReturn(Optional.of(group));

        //when
        groupService.joinGroup(joinRequestDto, any(Long.class));

        //then
        verify(joinRequestService, times(1))
                .createNewGroupJoinRequest(any(GroupJoinRequestDto.class), any(Group.class));
    }

    @Test
    @DisplayName("대기중인 가입 요청이 있다면 DuplicateElementException이 발생한다")
    void failToJoinByPendingRequestExist() {
        //given
        when(joinRequestService.pendingRequestExists(any())).thenReturn(true);
        when(groupRepository.findById(any())).thenReturn(Optional.of(group));

        //then
        assertThrows(DuplicateElementException.class, () -> groupService.joinGroup(joinRequestDto, any(Long.class)));
    }

    @Test
    @DisplayName("추방당한 전적이 있다면 ForbiddenAccessException이 발생한다")
    void failToJoinByPreviousBanRecord() {
        //given
        profile = makeProfile(GroupRole.MEMBER, State.RESTRICTED);
        when(joinRequestService.pendingRequestExists(any())).thenReturn(false);
        when(profileService.getLoggedInProfile(any())).thenReturn(profile);
        when(groupRepository.findById(any())).thenReturn(Optional.of(group));

        //then
        assertThrows(UnAuthorizedException.class, () -> groupService.joinGroup(joinRequestDto, any(Long.class)));
    }


    private Group makeGroup(JoinCondition joinCondition) {
        return Group.builder()
                .name(groupName)
                .description(description)
                .groupScope(GroupScope.PUBLIC)
                .joinCondition(joinCondition)
                .ownerUserId(1L)
                .build();
    }

    private Profile makeProfile(GroupRole groupRole, State state) {
        return Profile.builder()
                .nickname(nickname)
                .groupRole(groupRole)
                .state(state)
                .group(group)
                .userId(1L)
                .build();
    }
}