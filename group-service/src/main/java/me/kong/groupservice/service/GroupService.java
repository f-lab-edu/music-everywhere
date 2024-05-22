package me.kong.groupservice.service;


import lombok.RequiredArgsConstructor;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.repository.GroupRepository;
import me.kong.groupservice.dto.request.SaveGroupRequestDto;
import me.kong.groupservice.mapper.GroupMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    private final ProfileService profileService;

    private final GroupMapper groupMapper;

    @Transactional
    public Group createNewGroup(SaveGroupRequestDto dto) {
        Group group = groupRepository.save(groupMapper.toEntity(dto));

        profileService.createNewProfile(dto.getNickname(), GroupRole.MASTER, group);

        return group;
    }
}
