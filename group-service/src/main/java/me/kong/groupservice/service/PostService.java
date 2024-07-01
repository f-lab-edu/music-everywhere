package me.kong.groupservice.service;


import lombok.RequiredArgsConstructor;
import me.kong.commonlibrary.exception.auth.UnAuthorizedException;
import me.kong.groupservice.common.annotation.GroupId;
import me.kong.groupservice.common.annotation.GroupOnly;
import me.kong.groupservice.common.annotation.UserId;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.post.Post;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.domain.repository.PostRepository;
import me.kong.groupservice.dto.request.SavePostRequestDto;
import me.kong.groupservice.mapper.PostMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static me.kong.groupservice.domain.entity.profile.GroupRole.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final ProfileService profileService;


    @GroupOnly(role = MEMBER)
    @Transactional
    public void savePost(SavePostRequestDto dto, @UserId Long userId, @GroupId Long groupId) {
        Profile profile = profileService.getLoggedInProfile(userId, groupId);

        Post post = postMapper.toEntity(dto, groupId, profile.getId());

        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Post findPost(Long id) {
        return postRepository.findById(id)
                .map(p -> {
                    if (p.getState() != State.GENERAL) {
                        throw new UnAuthorizedException("접근이 제한된 게시글입니다.");
                    }
                    return p;
                })
                .orElseThrow(() -> new NoSuchElementException("찾으려는 게시글이 없습니다. id : " + id));
    }

    @GroupOnly(role = MANAGER)
    @Transactional
    public void deletePost(Long id, @UserId Long userId, @GroupId Long groupId) {
        Post post = findPost(id);
        post.setState(State.DELETED);
    }
}
