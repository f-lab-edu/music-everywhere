package me.kong.groupservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kong.commonlibrary.exception.common.DuplicateElementException;
import me.kong.commonlibrary.util.JwtReader;
import me.kong.groupservice.common.annotation.RedisLock;
import me.kong.groupservice.common.exception.NoLoggedInProfileException;
import me.kong.groupservice.domain.entity.GroupJoinRequest.GroupJoinRequest;
import me.kong.groupservice.domain.entity.GroupJoinRequest.JoinResponse;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.group.JoinCondition;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.dto.request.GroupJoinProcessDto;
import me.kong.groupservice.dto.request.GroupJoinRequestDto;
import me.kong.groupservice.service.strategy.Consumers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static me.kong.groupservice.common.Constants.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class GroupJoinFacade {

    private final GroupJoinRequestService joinRequestService;
    private final GroupService groupService;
    private final ProfileService profileService;
    private final Consumers consumers;
    private final JwtReader jwtReader;


    @RedisLock(key = "'group:'.concat(#groupId)")
    public void processGroupJoinRequest(Long groupId, Long requestId, GroupJoinProcessDto dto) {
        GroupJoinRequest joinRequest = joinRequestService.getGroupJoinRequestByRequestId(requestId);

        if (joinRequest.getResponse() != JoinResponse.PENDING) {
            throw new DuplicateElementException("이미 처리된 가입 요청입니다. 요청 id : " + requestId);
        }

        profileService.checkLoggedInProfileIsGroupManager(joinRequest.getGroup().getId());

        Consumer<GroupJoinRequest> action = consumers.getJoinRequestConsumer(dto.getAction());
        if (action != null) {
            action.accept(joinRequest);
        } else {
            log.warn(NoStateExceptionMessage, "가입 요청 처리", dto.getAction());
            throw new IllegalStateException("No action found for state: " + dto.getAction());
        }
    }

    @RedisLock(key = "'group:'.concat(#groupId)")
    public void joinGroup(GroupJoinRequestDto dto, Long groupId) {
        Group group = groupService.findGroupById(groupId);
        if (joinRequestService.pendingRequestExists(group.getId())) {
            throw new DuplicateElementException("이미 가입 요청한 그룹입니다.");
        }

        try {
            Profile profile = profileService.getLoggedInProfile(groupId);
            BiConsumer<Profile, GroupJoinRequestDto> action = consumers.getGroupJoinConsumer(profile.getState());
            if (action != null) {
                action.accept(profile, dto);
            } else {
                log.warn(NoStateExceptionMessage, "그룹 가입", profile.getState());
                throw new IllegalStateException("No action found : " + profile.getState());
            }
        } catch (NoLoggedInProfileException e) {
            firstJoinProcess(dto, group);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // 동시성
        }
    }

    private void firstJoinProcess(GroupJoinRequestDto dto, Group group) {
        if (group.getJoinCondition() == JoinCondition.OPEN) {
            groupService.checkGroupSize(group);
            group.increaseProfileCount();
            profileService.createNewProfile(dto.getNickname(), jwtReader.getUserId(), GroupRole.MEMBER, group);
        } else {
            joinRequestService.createNewGroupJoinRequest(dto, group);
        }
    }

}
