package me.kong.groupservice.mapper;


import lombok.RequiredArgsConstructor;
import me.kong.commonlibrary.util.JwtReader;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.group.GroupSizeConstants;
import me.kong.groupservice.dto.request.SaveGroupRequestDto;
import me.kong.groupservice.dto.response.GroupResponseDto;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupMapper {

    private final JwtReader jwtReader;

    public Group toEntity(SaveGroupRequestDto dto) {
        return Group.builder()
                .name(dto.getGroupName())
                .description(dto.getDescription())
                .groupSize(GroupSizeConstants.BASIC)
                .joinCondition(dto.getJoinCondition())
                .groupScope(dto.getGroupScope())
                .state(State.GENERAL)
                .ownerUserId(jwtReader.getUserId())
                .profileCount(1)
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
