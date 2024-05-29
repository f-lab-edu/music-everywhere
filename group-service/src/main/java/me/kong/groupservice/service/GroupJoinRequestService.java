package me.kong.groupservice.service;

import lombok.RequiredArgsConstructor;
import me.kong.groupservice.common.JwtReader;
import me.kong.groupservice.common.exception.DuplicateElementException;
import me.kong.groupservice.domain.entity.GroupJoinRequest.GroupJoinRequest;
import me.kong.groupservice.domain.entity.GroupJoinRequest.JoinResponse;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.repository.GroupJoinRequestRepository;
import me.kong.groupservice.dto.request.GroupJoinProcessDto;
import me.kong.groupservice.dto.request.GroupJoinRequestDto;
import me.kong.groupservice.dto.request.enums.JoinRequestSearchCondition;
import me.kong.groupservice.mapper.GroupJoinRequestMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GroupJoinRequestService {

    private final JwtReader jwtReader;
    private final GroupJoinRequestRepository joinRequestRepository;
    private final GroupJoinRequestMapper joinRequestMapper;
    private final GroupJoinRequestRepository groupJoinRequestRepository;
    private final ProfileService profileService;

    @Transactional(readOnly = true)
    public GroupJoinRequest getGroupJoinRequestByRequestId(Long requestId) {
        return joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 가입 요청이 없습니다. id : " + requestId));
    }

    @Transactional(readOnly = true)
    public boolean pendingRequestExists(Long groupId) {
        return joinRequestRepository.pendingRequestExists(jwtReader.getUserId(), groupId);
    }

    @Transactional
    public void createNewGroupJoinRequest(GroupJoinRequestDto dto, Group group) {
        joinRequestRepository.save(joinRequestMapper.toEntity(dto, group));
    }

    @Transactional(readOnly = true)
    public List<GroupJoinRequest> getGroupJoinRequestsByGroupIdAndCondition(Long groupId, JoinRequestSearchCondition condition) {
        List<GroupJoinRequest> requests;

        profileService.checkLoggedInProfileIsGroupManager(groupId);

        switch (condition) {
            case PENDING -> {
                requests = groupJoinRequestRepository.findPendingGroupJoinRequests(groupId);
            }
            case PROCESSED -> {
                requests = groupJoinRequestRepository.findProcessedGroupJoinRequests(groupId);
            }
            case ALL -> {
                requests = groupJoinRequestRepository.findAllByGroupId(groupId);
            }
            default -> {
                throw new IllegalArgumentException("지원하지 않는 그룹 가입 검색 조건. status : " + condition);
            }
        }
        return requests;
    }

    @Transactional
    public void processGroupJoinRequest(Long requestId, GroupJoinProcessDto dto) {
        GroupJoinRequest joinRequest = getGroupJoinRequestByRequestId(requestId);

        if (joinRequest.getResponse() != JoinResponse.PENDING) {
            throw new DuplicateElementException("이미 처리된 가입 요청입니다. 요청 id : " + requestId);
        }

        profileService.checkLoggedInProfileIsGroupManager(joinRequest.getGroup().getId());

        switch (dto.getAction()) {
            case APPROVE -> {
                joinRequest.approveJoinRequest();
                profileService.createNewProfile(joinRequest.getNickname(), GroupRole.MEMBER, joinRequest.getGroup());
            }
            case REJECT -> {
                joinRequest.rejectJoinRequest();
            }
            default -> {
                throw new IllegalArgumentException("지원하지 않는 처리 요청. action : " + dto.getAction());
            }
        }
    }
}
