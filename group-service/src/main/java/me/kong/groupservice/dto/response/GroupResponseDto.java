package me.kong.groupservice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GroupResponseDto {
    private Long id;
    private String name;
    private String description;
}
