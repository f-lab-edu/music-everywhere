package me.kong.groupservice.mapper;

import me.kong.commonlibrary.event.dto.GroupMemberIncreaseRequestDto;
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
