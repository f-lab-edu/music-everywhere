package me.kong.groupservice.dto.request;


import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GroupJoinRequestDto {

    @NotEmpty
    private String nickname;

    private String requestInfo;
}
