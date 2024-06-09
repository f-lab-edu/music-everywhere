package me.kong.groupservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kong.commonlibrary.exception.auth.UnAuthorizedException;
import me.kong.commonlibrary.exception.common.DuplicateElementException;
import me.kong.groupservice.common.exception.GroupFullException;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final ProfileService profileService;
    private final GroupMapper groupMapper;

    @Transactional
    public Group createNewGroup(SaveGroupRequestDto dto) {
        Group group = groupRepository.save(groupMapper.toEntity(dto));

        profileService.createNewProfile(dto.getNickname(), GroupRole.MANAGER, group);

        return group;
    }

    @Transactional(readOnly = true)
    public Group findGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("찾으려는 그룹이 없습니다. id : " + id));
    }

    @Transactional(readOnly = true)
    public void checkGroupSize(Group group) {
        if (group.getProfileCount() >= group.getGroupSize()) {
            throw new GroupFullException("최대 인원인 그룹입니다. id : " + group.getId());
        }
    }
}
