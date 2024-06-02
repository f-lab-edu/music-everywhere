package me.kong.groupservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import me.kong.groupservice.domain.entity.GroupJoinRequest.JoinResponse;

@Getter
@Builder
public class GroupJoinResponseDto {
    private Long requestId;
    private String nickname;
    private String requestInfo;
    private JoinResponse status;
    private Long userId;
}
