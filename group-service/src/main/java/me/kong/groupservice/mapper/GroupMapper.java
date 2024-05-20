package me.kong.groupservice.mapper;


import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.dto.request.SaveGroupRequestDto;
import me.kong.groupservice.dto.response.GroupResponseDto;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {

    public Group toEntity(SaveGroupRequestDto dto) {
        return Group.builder()
                .name(dto.getGroupName())
                .description(dto.getDescription())
                .joinCondition(dto.getJoinCondition())
                .groupScope(dto.getGroupScope())
                .state(State.GENERAL)
                .build();
    }

     public GroupResponseDto toDto(Group group) {
        return GroupResponseDto.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .build();
     }
}
