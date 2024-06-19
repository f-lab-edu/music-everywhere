package me.kong.groupservice.service;

import me.kong.groupservice.domain.entity.State;
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


    SavePostRequestDto savePostRequestDto;
    Profile profile;
    Post post;

    Long profileId = 1L;
    Long groupId = 1L;
    Long postId = 1L;
    String title = "테스트 제목";
    String content = "테스트 내용";
    PostScope scope = PostScope.GROUP_ONLY;


    @Test
    void successToSavePost() {
        //given
        savePostSetting(scope, State.GENERAL);

        //when
        postService.savePost(savePostRequestDto, groupId);

        //then
        verify(profileService, times(1)).getLoggedInProfile(groupId);
        verify(postMapper, times(1)).toEntity(savePostRequestDto, groupId, profileId);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void failToSavePost() {
        //given
        savePostSetting(scope, State.GENERAL);
        when(postRepository.save(any())).thenThrow(RuntimeException.class);

        //when & then
        assertThrows(RuntimeException.class, () -> postService.savePost(savePostRequestDto, groupId));
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
        postService.deletePost(postId);

        //then
        assertEquals(State.DELETED, post.getState());
    }


    private void savePostSetting(PostScope scope, State state) {
        profile = mock();
        when(profile.getId()).thenReturn(profileId);
        savePostRequestDto = makeSavePostRequestDto(scope);
        post = makePost(state);
        when(profileService.getLoggedInProfile(groupId)).thenReturn(profile);
        when(postMapper.toEntity(savePostRequestDto, groupId, profile.getId())).thenReturn(post);
    }

    private SavePostRequestDto makeSavePostRequestDto(PostScope scope) {
        return savePostRequestDto = SavePostRequestDto.builder()
                .title(title)
                .content(content)
                .scope(scope)
                .build();
    }

    private Post makePost(State state) {
        return Post.builder()
                .title(title)
                .content(content)
                .postScope(scope)
                .state(state)
                .groupId(groupId)
                .profileId(profileId)
                .build();
    }
}