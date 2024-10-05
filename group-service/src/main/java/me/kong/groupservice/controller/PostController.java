package me.kong.groupservice.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kong.commonlibrary.util.JwtReader;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.post.Post;
import me.kong.groupservice.domain.entity.post.PostScope;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.dto.request.SavePostRequestDto;
import me.kong.groupservice.dto.request.condition.PostSearchCondition;
import me.kong.groupservice.dto.response.PostListResponseDto;
import me.kong.groupservice.dto.response.PostResponseDto;
import me.kong.groupservice.mapper.PostMapper;
import me.kong.groupservice.service.PostService;
import me.kong.groupservice.service.ProfileService;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static me.kong.commonlibrary.constant.HttpStatusResponseEntity.RESPONSE_OK;


@Slf4j
@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final ProfileService profileService;
    private final JwtReader jwtReader;


    @GetMapping("/posts")
    public ResponseEntity<Slice<PostListResponseDto>> getPostList(
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String searchText) {

        PostSearchCondition cond = PostSearchCondition.builder()
                .postScope(PostScope.PUBLIC)
                .state(State.GENERAL)
                .searchText(searchText)
                .build();

        Slice<PostListResponseDto> postLists = postService.getRecentPublicPosts(cursorId, cond, size);

        return new ResponseEntity<>(postLists, HttpStatus.OK);
    }

    @GetMapping("/{groupId}/posts")
    public ResponseEntity<Slice<PostListResponseDto>> getPostSlice(
            @PathVariable Long groupId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "GENERAL") State state,
            @RequestParam(required = false, defaultValue = "10") int size) {

        PostSearchCondition cond = PostSearchCondition.builder()
                .groupId(groupId)
                .state(state)
                .build();

        Slice<PostListResponseDto> postLists = postService.getRecentGroupPosts(cursorId, cond, size, jwtReader.getUserId(), groupId);

        return new ResponseEntity<>(postLists, HttpStatus.OK);
    }

    @PostMapping("/{groupId}/posts")
    public ResponseEntity<HttpStatus> createNewPost(@PathVariable Long groupId,
                                                    @RequestBody SavePostRequestDto dto) {
        postService.savePost(dto, jwtReader.getUserId(), groupId);
        return RESPONSE_OK;
    }

    @GetMapping("/{groupId}/posts/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long groupId,
                                                   @PathVariable Long postId) {
        Profile profile = profileService.getLoggedInProfile(jwtReader.getUserId(), groupId);
        Post post = postService.findPost(postId);

        return new ResponseEntity<>(postMapper.toDto(post, profile), HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}/posts/{postId}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable Long groupId,
                                                   @PathVariable Long postId) {
        postService.deletePost(postId, jwtReader.getUserId(), groupId);
        return RESPONSE_OK;
    }
}
