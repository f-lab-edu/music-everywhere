package me.kong.groupservice.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kong.commonlibrary.util.JwtReader;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.post.Post;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.dto.request.SavePostRequestDto;
import me.kong.groupservice.dto.response.PostListResponseDto;
import me.kong.groupservice.dto.response.PostResponseDto;
import me.kong.groupservice.mapper.PostMapper;
import me.kong.groupservice.service.PostService;
import me.kong.groupservice.service.ProfileService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static me.kong.commonlibrary.constant.HttpStatusResponseEntity.RESPONSE_OK;


@Slf4j
@RestController
@RequestMapping("/api/groups/{groupId}/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final ProfileService profileService;
    private final JwtReader jwtReader;

    @GetMapping
    public ResponseEntity<Page<PostListResponseDto>> getPostList(
            @PathVariable Long groupId,
            @RequestParam(required = false, defaultValue = "GENERAL") State state,
            @RequestParam(required = false , defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        Page<Post> posts = postService.getRecentPosts(groupId, state, page, size);

        return new ResponseEntity<>(postMapper.toDto(posts), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createNewPost(@PathVariable Long groupId,
                                                    @RequestBody SavePostRequestDto dto) {
        postService.savePost(dto, jwtReader.getUserId(), groupId);
        return RESPONSE_OK;
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long groupId,
                                                   @PathVariable Long postId) {
        Profile profile = profileService.getLoggedInProfile(jwtReader.getUserId(), groupId);
        Post post = postService.findPost(postId);

        return new ResponseEntity<>(postMapper.toDto(post, profile), HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable Long groupId,
                                                   @PathVariable Long postId) {
        postService.deletePost(postId, jwtReader.getUserId(), groupId);
        return RESPONSE_OK;
    }
}
