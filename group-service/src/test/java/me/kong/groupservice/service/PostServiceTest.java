package me.kong.groupservice.service;

import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.post.Post;
import me.kong.groupservice.domain.entity.post.PostScope;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.domain.repository.PostRepository;
import me.kong.groupservice.dto.request.SavePostRequestDto;
import me.kong.groupservice.mapper.PostMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    PostService postService;

    @Mock
    PostRepository postRepository;

    @Mock
    PostMapper postMapper;

    @Mock
    ProfileService profileService;

    @Mock
    GroupService groupService;

    SavePostRequestDto savePostRequestDto;
    Group group;
    Profile profile;
    Post post;

    Long profileId = 1L;
    Long groupId = 2L;
    Long postId = 3L;
    Long userId = 4L;

    String content = "테스트 내용";

    PostScope scope = PostScope.GROUP_ONLY;



    @Test
    void successToSavePost() {
        //given
        savePostSetting(scope, State.GENERAL);

        //when
        postService.savePost(savePostRequestDto, userId, groupId);

        //then
        verify(profileService, times(1)).getLoggedInProfile(userId, groupId);
        verify(postMapper, times(1)).toEntity(savePostRequestDto, group, profile);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void failToSavePost() {
        //given
        savePostSetting(scope, State.GENERAL);
        when(postRepository.save(any())).thenThrow(RuntimeException.class);

        //when & then
        assertThrows(RuntimeException.class, () -> postService.savePost(savePostRequestDto, userId, groupId));
    }

    @Test
    void successToFindPost() {
        //given
        post = makePost(State.GENERAL);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        //when
        postService.findPost(postId);

        //then
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void failToFindPost_NotFound() {
        //given
        when(postRepository.findById(any(Long.class))).thenThrow(NoSuchElementException.class);

        //when & then
        assertThrows(NoSuchElementException.class, () -> postService.findPost(postId));
    }

    @Test
    void successToDeletePost() {
        //given
        post = makePost(State.GENERAL);
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));

        //when
        postService.deletePost(postId, userId, groupId);

        //then
        assertEquals(State.DELETED, post.getState());
    }


    private void savePostSetting(PostScope scope, State state) {
        group = mock();
        profile = mock();

        savePostRequestDto = makeSavePostRequestDto(scope);
        post = makePost(state);

        when(groupService.findGroupById(any())).thenReturn(group);
        when(profileService.getLoggedInProfile(userId, groupId)).thenReturn(profile);
        when(postMapper.toEntity(savePostRequestDto, group, profile)).thenReturn(post);
    }

    private SavePostRequestDto makeSavePostRequestDto(PostScope scope) {
        return savePostRequestDto = SavePostRequestDto.builder()
                .content(content)
                .scope(scope)
                .build();
    }

    private Post makePost(State state) {
        return Post.builder()
                .content(content)
                .postScope(scope)
                .state(state)
                .group(group)
                .profile(profile)
                .build();
    }
}