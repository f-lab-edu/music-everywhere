package me.kong.groupservice.service;


import lombok.RequiredArgsConstructor;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.post.Post;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.domain.repository.PostRepository;
import me.kong.groupservice.dto.request.SavePostRequestDto;
import me.kong.groupservice.mapper.PostMapper;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final ProfileService profileService;

    public void savePost(SavePostRequestDto dto, Long groupId) {
        Profile profile = profileService.getLoggedInProfile(groupId);

        Post post = postMapper.toEntity(dto, groupId, profile.getId());

        postRepository.save(post);
    }

    public Post findPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("찾으려는 게시글이 없습니다. id : " + id));
    }

    public void deletePost(Long id) {
        Post post = findPost(id);
        post.setState(State.DELETED);
    }
}
