package me.kong.groupservice.domain.repository.query;

import me.kong.groupservice.dto.request.condition.PostSearchCondition;
import me.kong.groupservice.dto.response.PostListResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface CustomPostRepository {

    Slice<PostListResponseDto> searchRecentPosts(Long cursorId, PostSearchCondition cond, Pageable pageable);
}
