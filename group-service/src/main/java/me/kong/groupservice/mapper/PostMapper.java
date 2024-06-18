package me.kong.groupservice.mapper;

import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.post.Post;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.dto.request.SavePostRequestDto;
import me.kong.groupservice.dto.response.PostResponseDto;
import org.springframework.stereotype.Component;


@Component
public class PostMapper {

    public Post toEntity(SavePostRequestDto dto, Long groupId, Long profileId) {
        return Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .postScope(dto.getScope())
                .state(State.GENERAL)
                .groupId(groupId)
                .profileId(profileId)
                .build();
    }

    public PostResponseDto toDto(Post post, Profile profile) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .nickname(profile.getNickname())
                .profileId(profile.getId())
                .groupId(post.getGroupId())
                .updatedDate(post.getUpdatedDate())
                .build();
    }
}
