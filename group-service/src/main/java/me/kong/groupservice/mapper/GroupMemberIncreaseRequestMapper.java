package me.kong.groupservice.mapper;

import me.kong.groupservice.dto.event.GroupMemberIncreaseRequestDto;
import org.springframework.stereotype.Component;

@Component
public class GroupMemberIncreaseRequestMapper {

    public GroupMemberIncreaseRequestDto toDto(Long groupId, Long userId, Integer size) {
        return GroupMemberIncreaseRequestDto.builder()
                .groupId(groupId)
                .userId(userId)
                .additionalMembers(size)
                .build();
    }
}
