package me.kong.groupservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long groupId;
    private Long profileId;
    private String nickname;
    private LocalDateTime updatedDate;
}
