package me.kong.groupservice.mapper;

import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.post.Post;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.dto.request.SavePostRequestDto;
import me.kong.groupservice.dto.response.PostResponseDto;
import org.springframework.stereotype.Component;


@Component
public class PostMapper {

    public Post toEntity(SavePostRequestDto dto, Group group, Profile profile) {
        return Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .postScope(dto.getScope())
                .state(State.GENERAL)
                .group(group)
                .profile(profile)
                .build();
    }

    public PostResponseDto toDto(Post post, Profile profile) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .nickname(profile.getNickname())
                .profileId(profile.getId())
                .groupId(post.getGroup().getId())
                .updatedDate(post.getUpdatedDate())
                .build();
    }
}
