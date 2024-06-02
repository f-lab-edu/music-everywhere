package me.kong.groupservice.mapper;

import lombok.RequiredArgsConstructor;
import me.kong.groupservice.common.JwtReader;
import me.kong.groupservice.domain.entity.GroupJoinRequest.GroupJoinRequest;
import me.kong.groupservice.domain.entity.GroupJoinRequest.JoinResponse;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.dto.request.GroupJoinRequestDto;
import me.kong.groupservice.dto.response.GroupJoinResponseDto;
import me.kong.groupservice.dto.response.GroupResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GroupJoinRequestMapper {

    private final JwtReader jwtReader;

    public GroupJoinRequest toEntity(GroupJoinRequestDto dto, Group group) {
        return GroupJoinRequest.builder()
                .requestInfo(dto.getRequestInfo())
                .nickname(dto.getNickname())
                .response(JoinResponse.PENDING)
                .userId(jwtReader.getUserId())
                .group(group)
                .build();
    }

    public GroupJoinResponseDto toDto(GroupJoinRequest request) {
        return GroupJoinResponseDto.builder()
                .requestId(request.getId())
                .nickname(request.getNickname())
                .requestInfo(request.getRequestInfo())
                .status(request.getResponse())
                .userId(request.getUserId())
                .build();
    }

    public List<GroupJoinResponseDto> toDtoList(List<GroupJoinRequest> requests) {
        return requests.stream()
                .map(this::toDto)
                .toList();
    }
}
