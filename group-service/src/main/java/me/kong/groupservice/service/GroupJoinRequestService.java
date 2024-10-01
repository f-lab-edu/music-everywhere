package me.kong.groupservice.service;

import lombok.RequiredArgsConstructor;
import me.kong.commonlibrary.event.dto.UserListRequestDto;
import me.kong.commonlibrary.event.dto.UserListResponseDto;
import me.kong.groupservice.client.circuitbreaker.UserServiceCircuitBreaker;
import me.kong.groupservice.domain.entity.GroupJoinRequest.GroupJoinRequest;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.repository.GroupJoinRequestRepository;
import me.kong.groupservice.dto.request.GroupJoinRequestDto;
import me.kong.groupservice.dto.request.enums.JoinRequestSearchCondition;
import me.kong.groupservice.dto.response.GroupJoinResponseDto;
import me.kong.groupservice.mapper.GroupJoinRequestMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GroupJoinRequestService {

    private final GroupJoinRequestRepository joinRequestRepository;
    private final GroupJoinRequestMapper joinRequestMapper;
    private final ProfileService profileService;
    private final UserServiceCircuitBreaker userServiceClient;

    @Transactional(readOnly = true)
    public GroupJoinRequest getGroupJoinRequestByRequestId(Long requestId) {
        return joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 가입 요청이 없습니다. id : " + requestId));
    }

    @Transactional(readOnly = true)
    public boolean pendingRequestExists(Long userId, Long groupId) {
        return joinRequestRepository.pendingRequestExists(userId, groupId);
    }

    @Transactional
    public void createNewGroupJoinRequest(GroupJoinRequestDto dto, Group group) {
        joinRequestRepository.save(joinRequestMapper.toEntity(dto, group));
    }

    @Transactional(readOnly = true)
    public List<GroupJoinResponseDto> getGroupJoinRequestsByGroupIdAndCondition(Long userId, Long groupId, JoinRequestSearchCondition condition) {
        profileService.checkLoggedInProfileIsGroupManager(userId, groupId);

        List<GroupJoinRequest> requests = getGroupJoinRequests(groupId, condition);

        List<Long> userIds = requests.stream()
                .map(GroupJoinRequest::getUserId)
                .toList();

        // 여기에 circuit breaker 적용
        List<UserListResponseDto> userInfos = userServiceClient.getUserInfo(new UserListRequestDto(userIds));

        if (userInfos.isEmpty()) {
            for (GroupJoinRequest request : requests) {
                userInfos.add(new UserListResponseDto(request.getUserId(), request.getNickname()));
            }
        }

        return requests.stream()
                .map(r -> GroupJoinResponseDto.builder()
                        .requestId(r.getId())
                        .requestInfo(r.getRequestInfo())
                        .status(r.getResponse())
                        .nickname(getNickname(userInfos, r.getUserId()))
                        .userId(r.getUserId())
                        .build())
                .toList();
    }

    private String getNickname(List<UserListResponseDto> userInfos, Long userId) {
        return userInfos.stream()
                .filter(userInfo -> userId.equals(userInfo.getUserId()))
                .map(UserListResponseDto::getNickname)
                .findFirst()
                .orElse(null);
    }


    private List<GroupJoinRequest> getGroupJoinRequests(Long groupId, JoinRequestSearchCondition condition) {
        List<GroupJoinRequest> requests;
        switch (condition) {
            case PENDING -> {
                requests = joinRequestRepository.findPendingGroupJoinRequests(groupId);
            }
            case PROCESSED -> {
                requests = joinRequestRepository.findProcessedGroupJoinRequests(groupId);
            }
            case ALL -> {
                requests = joinRequestRepository.findAllByGroupId(groupId);
            }
            default -> {
                throw new IllegalArgumentException("지원하지 않는 그룹 가입 검색 조건. status : " + condition);
            }
        }
        return requests;
    }
}
