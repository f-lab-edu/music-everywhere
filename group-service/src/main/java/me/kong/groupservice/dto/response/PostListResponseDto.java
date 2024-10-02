package me.kong.groupservice.dto.response;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostListResponseDto {
    private Long id;
    private String content;
    private Long groupId;
    private Long profileId;
    private String nickname;
    private LocalDateTime updatedDate;

    @Builder
    @QueryProjection
    public PostListResponseDto(Long id, String content, Long groupId, Long profileId, String nickname, LocalDateTime updatedDate) {
        this.id = id;
        this.content = content;
        this.groupId = groupId;
        this.profileId = profileId;
        this.nickname = nickname;
        this.updatedDate = updatedDate;
    }
}
