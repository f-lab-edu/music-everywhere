package me.kong.groupservice.domain.repository.query;

import me.kong.groupservice.domain.entity.post.Post;
import me.kong.groupservice.dto.request.condition.PostSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CustomPostRepository {

    Page<Post> searchRecentPosts(PostSearchCondition cond, Pageable pageable);
}
