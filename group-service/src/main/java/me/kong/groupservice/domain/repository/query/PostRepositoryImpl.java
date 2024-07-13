package me.kong.groupservice.domain.repository.query;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.kong.groupservice.domain.entity.post.Post;
import me.kong.groupservice.domain.entity.post.PostScope;
import me.kong.groupservice.dto.request.condition.PostSearchCondition;
import org.springframework.data.domain.*;

import java.util.List;

import static me.kong.groupservice.domain.entity.group.QGroup.*;
import static me.kong.groupservice.domain.entity.post.QPost.*;
import static me.kong.groupservice.domain.entity.profile.QProfile.*;


@RequiredArgsConstructor
public class PostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Post> searchRecentPosts(Long cursorId, PostSearchCondition cond, Pageable pageable) {
        List<Post> content = queryFactory
                .select(post)
                .from(post)
                .join(post.group, group).fetchJoin()
                .join(post.profile, profile).fetchJoin()
                .where(
                        groupIdEq(cond.getGroupId()),
                        postScopeEq(cond.getPostScope()),
                        post.state.eq(cond.getState()),
                        cursorIdLt(cursorId)
                )
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanExpression cursorIdLt(Long cursorId) {
        return cursorId != null ? post.id.lt(cursorId) : null;
    }

    private BooleanExpression postScopeEq(PostScope postScope) {
        return postScope != null ? post.postScope.eq(postScope) : null;
    }

    private BooleanExpression groupIdEq(Long groupId) {
        return groupId != null ? post.group.id.eq(groupId) : null;
    }
}
