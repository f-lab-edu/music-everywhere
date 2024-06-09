package me.kong.groupservice.service;

import lombok.RequiredArgsConstructor;
import me.kong.commonlibrary.util.JwtReader;
import me.kong.groupservice.domain.entity.GroupJoinRequest.GroupJoinRequest;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.repository.GroupJoinRequestRepository;
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
