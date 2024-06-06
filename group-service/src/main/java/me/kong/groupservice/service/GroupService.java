package me.kong.groupservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kong.commonlibrary.exception.auth.UnAuthorizedException;
import me.kong.commonlibrary.exception.common.DuplicateElementException;
import me.kong.groupservice.common.exception.NoLoggedInProfileException;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.group.JoinCondition;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.domain.repository.GroupRepository;
import me.kong.groupservice.dto.request.GroupJoinRequestDto;
import me.kong.groupservice.dto.request.SaveGroupRequestDto;
import me.kong.groupservice.mapper.GroupMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupJoinRequestService joinRequestService;
    private final ProfileService profileService;
    private final GroupMapper groupMapper;


    @Transactional
    public Group createNewGroup(SaveGroupRequestDto dto) {
        Group group = groupRepository.save(groupMapper.toEntity(dto));

        profileService.createNewProfile(dto.getNickname(), GroupRole.MANAGER, group);

        return group;
    }

    @Transactional
    public void joinGroup(GroupJoinRequestDto dto, Long groupId) {
        Group group = findGroupById(groupId);

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
                        profileService.checkGroupSize(group);
                        profile.setState(State.GENERAL);
                    } else {
                        joinRequestService.createNewGroupJoinRequest(dto, group);
                    }
                }
            }
        } catch (NoLoggedInProfileException e) {
            if (group.getJoinCondition() == JoinCondition.OPEN) {
                profileService.checkGroupSize(group);
                profileService.createNewProfile(dto.getNickname(), GroupRole.MEMBER, group);
            } else {
                joinRequestService.createNewGroupJoinRequest(dto, group);
            }
        }
    }

    @Transactional(readOnly = true)
    public Group findGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("찾으려는 그룹이 없습니다. id : " + id));
    }
}
