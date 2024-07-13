package me.kong.groupservice.domain.repository.query;

import me.kong.groupservice.domain.entity.post.Post;
import me.kong.groupservice.dto.request.condition.PostSearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface CustomPostRepository {

    Slice<Post> searchRecentPosts(Long cursorId, PostSearchCondition cond, Pageable pageable);
}
