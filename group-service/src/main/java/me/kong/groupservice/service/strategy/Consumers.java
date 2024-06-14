package me.kong.groupservice.service.strategy;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.kong.commonlibrary.exception.auth.UnAuthorizedException;
import me.kong.commonlibrary.exception.common.DuplicateElementException;
import me.kong.groupservice.domain.entity.GroupJoinRequest.GroupJoinRequest;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.group.JoinCondition;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.dto.request.GroupJoinRequestDto;
import me.kong.groupservice.dto.request.enums.GroupJoinProcessAction;
import me.kong.groupservice.service.GroupJoinRequestService;
import me.kong.groupservice.service.GroupService;
import me.kong.groupservice.service.ProfileService;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class Consumers {

    private final GroupService groupService;
    private final GroupJoinRequestService joinRequestService;
    private final ProfileService profileService;

    private Map<State,BiConsumer<Profile, GroupJoinRequestDto>> groupJoinConsumerMap;
    private Map<GroupJoinProcessAction, Consumer<GroupJoinRequest>> joinRequestConsumerMap;

    public BiConsumer<Profile, GroupJoinRequestDto> getGroupJoinConsumer(State state) {
        return groupJoinConsumerMap.get(state);
    }

    public Consumer<GroupJoinRequest> getJoinRequestConsumer(GroupJoinProcessAction action) {
        return joinRequestConsumerMap.get(action);
    }

    @PostConstruct
    public void initGroupJoinConsumerMap() {
        groupJoinConsumerMap = new EnumMap<>(State.class);

        groupJoinConsumerMap.put(State.RESTRICTED, (profile, dto) -> {
            throw new UnAuthorizedException("추방당한 회원입니다. userId : " + profile.getUserId());
        });

        groupJoinConsumerMap.put(State.GENERAL, (profile, dto) -> {
            throw new DuplicateElementException("이미 가입한 그룹입니다.");
        });

        groupJoinConsumerMap.put(State.DELETED, (profile, dto) -> {
            Group group = profile.getGroup();
            if (group.getJoinCondition() == JoinCondition.OPEN) {
                groupService.checkGroupSize(group);
                group.increaseProfileCount();
                profile.setState(State.GENERAL);
            } else {
                joinRequestService.createNewGroupJoinRequest(dto, group);
            }
        });
    }

    @PostConstruct
    public void initJoinRequestConsumerMap() {
        joinRequestConsumerMap = new EnumMap<>(GroupJoinProcessAction.class);

        joinRequestConsumerMap.put(GroupJoinProcessAction.APPROVE, joinRequest -> {
            Group group = groupService.findGroupByIdWithLock(joinRequest.getGroup().getId());
            groupService.checkGroupSize(group);
            group.increaseProfileCount();

            joinRequest.approveJoinRequest();
            profileService.createNewProfile(joinRequest.getNickname(), joinRequest.getUserId(), GroupRole.MEMBER, joinRequest.getGroup());
        });

        joinRequestConsumerMap.put(GroupJoinProcessAction.REJECT, GroupJoinRequest::rejectJoinRequest);
    }
}