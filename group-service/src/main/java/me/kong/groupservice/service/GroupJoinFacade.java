package me.kong.groupservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kong.commonlibrary.exception.auth.UnAuthorizedException;
import me.kong.commonlibrary.exception.common.DuplicateElementException;
import me.kong.groupservice.common.exception.NoLoggedInProfileException;
import me.kong.groupservice.domain.entity.GroupJoinRequest.GroupJoinRequest;
import me.kong.groupservice.domain.entity.GroupJoinRequest.JoinResponse;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.group.JoinCondition;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.dto.request.GroupJoinProcessDto;
import me.kong.groupservice.dto.request.GroupJoinRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@Service
@RequiredArgsConstructor
public class GroupJoinFacade {

    private final GroupJoinRequestService joinRequestService;
    private final GroupService groupService;
    private final ProfileService profileService;


    @Transactional
    public void joinGroup(GroupJoinRequestDto dto, Long groupId) {
        Group group = groupService.findGroupById(groupId);

        if (joinRequestService.pendingRequestExists(group.getId())) {
            throw new DuplicateElementException("이미 가입 요청한 그룹입니다.");
        }

        try {
            Profile profile = profileService.getLoggedInProfile(groupId);

            switch (profile.getState()) {
                case RESTRICTED -> {
                    throw new UnAuthorizedException("추방당한 회원입니다. userId : " + profile.getUserId());
                }
                case GENERAL -> {
                    throw new DuplicateElementException("이미 가입한 그룹입니다.");
                }
                case DELETED -> {
                    if (group.getJoinCondition() == JoinCondition.OPEN) {
                        groupService.checkGroupSize(group);
                        group.increaseProfileCount();
                        profile.setState(State.GENERAL);
                    } else {
                        joinRequestService.createNewGroupJoinRequest(dto, group);
                    }
                }
            }
        } catch (NoLoggedInProfileException e) {
            if (group.getJoinCondition() == JoinCondition.OPEN) {
                groupService.checkGroupSize(group);
                group.increaseProfileCount();
                profileService.createNewProfile(dto.getNickname(), GroupRole.MEMBER, group);
            } else {
                joinRequestService.createNewGroupJoinRequest(dto, group);
            }
        }
    }

    @Transactional
    public void processGroupJoinRequest(Long requestId, GroupJoinProcessDto dto) {
        GroupJoinRequest joinRequest = joinRequestService.getGroupJoinRequestByRequestId(requestId);

        if (joinRequest.getResponse() != JoinResponse.PENDING) {
            throw new DuplicateElementException("이미 처리된 가입 요청입니다. 요청 id : " + requestId);
        }

        profileService.checkLoggedInProfileIsGroupManager(joinRequest.getGroup().getId());

        switch (dto.getAction()) {
            case APPROVE -> {
                Group group = joinRequest.getGroup();
                groupService.checkGroupSize(group);
                group.increaseProfileCount();

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
