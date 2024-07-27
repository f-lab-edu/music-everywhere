package me.kong.groupservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kong.groupservice.common.annotation.RedisLock;
import me.kong.groupservice.common.exception.GroupFullException;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.repository.GroupRepository;
import me.kong.groupservice.dto.event.GroupMemberIncreaseRequestDto;
import me.kong.groupservice.dto.request.SaveGroupRequestDto;
import me.kong.groupservice.event.KafkaProducer;
import me.kong.groupservice.mapper.GroupMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static me.kong.groupservice.common.EventConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final ProfileService profileService;
    private final GroupMapper groupMapper;
    private final KafkaProducer kafkaProducer;

    @Transactional
    public Group createNewGroup(SaveGroupRequestDto dto, Long userId) {
        Group group = groupRepository.save(groupMapper.toEntity(dto));

        profileService.createNewProfile(dto.getNickname(), userId, GroupRole.MANAGER, group);

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

    @Transactional(readOnly = true)
    public void requestIncreaseGroupSize(GroupMemberIncreaseRequestDto dto) {
        kafkaProducer.send(GROUP_MEMBER_INCREASE_REQUEST, dto);
    }

    @RedisLock(key = "'group-size:'.concat(#groupId)")
    public void increaseGroupSize(Long groupId, Integer size) {
        groupRepository.updateGroupSize(groupId, size);
    }

}
