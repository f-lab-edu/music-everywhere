package me.kong.groupservice.service;

import me.kong.commonlibrary.exception.common.DuplicateElementException;
import me.kong.commonlibrary.util.JwtReader;
import me.kong.groupservice.domain.entity.GroupJoinRequest.GroupJoinRequest;
import me.kong.groupservice.domain.entity.GroupJoinRequest.JoinResponse;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.repository.GroupJoinRequestRepository;
import me.kong.groupservice.dto.request.GroupJoinProcessDto;
import me.kong.groupservice.dto.request.GroupJoinRequestDto;
import me.kong.groupservice.dto.request.enums.GroupJoinProcessAction;
import me.kong.groupservice.dto.request.enums.JoinRequestSearchCondition;
import me.kong.groupservice.mapper.GroupJoinRequestMapper;
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
class GroupJoinRequestServiceTest {

    @InjectMocks
    GroupJoinRequestService groupJoinRequestService;

    @Mock
    ProfileService profileService;

    @Mock
    GroupJoinRequestRepository groupJoinRequestRepository;

    @Mock
    GroupJoinRequestMapper groupJoinRequestMapper;

    @Mock
    JwtReader jwtReader;


    GroupJoinRequest request;
    GroupJoinRequestDto dto;
    Group group;
    Long groupId;
    JoinRequestSearchCondition condition;
    GroupJoinProcessDto processDto;

    @BeforeEach
    void init() {
        group = mock(Group.class);
        request = mock(GroupJoinRequest.class);
        dto = GroupJoinRequestDto.builder()
                .requestInfo("sample")
                .nickname("testuser")
                .build();
        groupId = 1L;
    }

    @Test
    @DisplayName("그룹 가입 요청 생성에 성공한다")
    void successToCreateNewGroupJoinRequest() {
        //given
        when(groupJoinRequestMapper.toEntity(any(GroupJoinRequestDto.class), any(Group.class)))
                .thenReturn(request);

        //when
        groupJoinRequestService.createNewGroupJoinRequest(dto, group);

        //then
        verify(groupJoinRequestRepository, times(1)).save(request);
        verify(groupJoinRequestMapper, times(1)).toEntity(dto, group);
    }

    @Test
    @DisplayName("데이터베이스에 문제가 있을 경우 예외가 발생한다")
    void failedByDatabaseProblem() {
        //given
        when(groupJoinRequestRepository.save(any())).thenThrow(RuntimeException.class);

        //then
        assertThrows(RuntimeException.class, () -> groupJoinRequestService.createNewGroupJoinRequest(dto, group));
    }

    @Test
    @DisplayName("searchCondition이 ALL이라면 해당 그룹의 모든 그룹 가입 요청을 조회한다")
    void successToFindAllGroupJoinRequests() {
        //given
        JoinRequestSearchCondition condition = JoinRequestSearchCondition.ALL;

        //when
        groupJoinRequestService.getGroupJoinRequestsByGroupIdAndCondition(groupId, condition);

        //then
        verify(groupJoinRequestRepository, times(1)).findAllByGroupId(groupId);
    }

    @Test
    @DisplayName("searchCondition이 PENDING이라면 해당 그룹의 대기중인 그룹 가입 요청을 조회한다")
    void successToFindPendingGroupJOinRequests() {
        //given
        condition = JoinRequestSearchCondition.PENDING;

        //when
        groupJoinRequestService.getGroupJoinRequestsByGroupIdAndCondition(groupId, condition);

        //then
        verify(groupJoinRequestRepository, times(1))
                .findPendingGroupJoinRequests(groupId);
    }

    @Test
    @DisplayName("searchCondition이 PROCESSED라면 처리된 그룹 가입 요청을 조회한다")
    void successToFindProcessedGroupJoinRequests() {
        //given
        condition = JoinRequestSearchCondition.PROCESSED;

        //when
        groupJoinRequestService.getGroupJoinRequestsByGroupIdAndCondition(groupId, condition);

        //then
        verify(groupJoinRequestRepository, times(1))
                .findProcessedGroupJoinRequests(groupId);
    }

    @Test
    @DisplayName("가입 승인 시, 우 가입 요청 상태가 승인으로 변경되고 새로운 프로필을 생성한다")
    void successToApproveGroupJoinRequest() {
        //given
        Long requestId = 1L;
        processDto = getProcessDto(GroupJoinProcessAction.APPROVE);
        request = getJoinRequest(JoinResponse.PENDING);
        when(groupJoinRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        doNothing().when(profileService).checkLoggedInProfileIsGroupManager(anyLong());

        // when
        groupJoinRequestService.processGroupJoinRequest(requestId, processDto);

        // then
        assertEquals(JoinResponse.APPROVED, request.getResponse());
        verify(profileService, times(1)).createNewProfile(request.getNickname(), GroupRole.MEMBER, request.getGroup());
    }

    @Test
    @DisplayName("가입 거절 시, 가입 요청 상태가 거절로 변경된다")
    void successToRejectGroupJoinRequest() {
        //given
        Long requestId = 1L;
        processDto = getProcessDto(GroupJoinProcessAction.REJECT);
        request = getJoinRequest(JoinResponse.PENDING);
        when(groupJoinRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        doNothing().when(profileService).checkLoggedInProfileIsGroupManager(anyLong());

        //when
        groupJoinRequestService.processGroupJoinRequest(requestId, processDto);

        //then
        assertEquals(JoinResponse.REJECTED, request.getResponse());
    }


    @Test
    @DisplayName("가입 요청이 이미 처리되었다면 예외가 발생한다")
    void testProcessGroupJoinRequest_AlreadyProcessed() {
        // given
        Long requestId = 1L;
        processDto = getProcessDto(GroupJoinProcessAction.APPROVE);
        request = getJoinRequest(JoinResponse.APPROVED);
        when(groupJoinRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

        // then
        assertThrows(DuplicateElementException.class, () -> {
            groupJoinRequestService.processGroupJoinRequest(requestId, processDto);
        });
    }



    private GroupJoinProcessDto getProcessDto(GroupJoinProcessAction action) {
        return GroupJoinProcessDto.builder()
                .action(action)
                .build();
    }

    private GroupJoinRequest getJoinRequest(JoinResponse response) {
        return GroupJoinRequest.builder()
                .response(response)
                .group(group)
                .build();
    }
}