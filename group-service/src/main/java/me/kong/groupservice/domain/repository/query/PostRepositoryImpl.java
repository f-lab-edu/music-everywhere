package me.kong.groupservice.domain.repository.query;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.kong.groupservice.domain.entity.post.PostScope;
import me.kong.groupservice.dto.request.condition.PostSearchCondition;
import me.kong.groupservice.dto.response.PostListResponseDto;
import me.kong.groupservice.dto.response.QPostListResponseDto;
import org.springframework.data.domain.*;

import java.util.List;

import static me.kong.groupservice.domain.entity.post.QPost.*;
import static me.kong.groupservice.domain.entity.profile.QProfile.*;


@RequiredArgsConstructor
public class PostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<PostListResponseDto> searchRecentPosts(Long postId, PostSearchCondition cond, Pageable pageable) {
        List<Long> postIds = queryFactory
                .select(post.id)
                .from(post)
                .where(
                        groupIdEq(cond.getGroupId()),
                        postScopeEq(cond.getPostScope()),
                        post.state.eq(cond.getState()),
                        postIdLt(postId)
                ).orderBy(post.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (postIds.size() > pageable.getPageSize()) {
            postIds.remove(pageable.getPageSize());
            hasNext = true;
        }

        List<PostListResponseDto> postLists = queryFactory
                .select(new QPostListResponseDto(
                        post.id,
                        Expressions.stringTemplate("SUBSTRING({0}, 1, 300)", post.content),
                        post.group.id, post.profile.id,
                        post.profile.nickname, post.updatedDate))
                .from(post)
                .join(post.profile, profile)
                .where(post.id.in(postIds))
                .orderBy(post.id.desc())
                .fetch();

        return new SliceImpl<>(postLists, pageable, hasNext);
    }

    private BooleanExpression postIdLt(Long postId) {
        return postId != null ? post.id.lt(postId) : null;
    }

    private BooleanExpression postScopeEq(PostScope postScope) {
        return postScope != null ? post.postScope.eq(postScope) : null;
    }

    private BooleanExpression groupIdEq(Long groupId) {
        return groupId != null ? post.group.id.eq(groupId) : null;
    }
}
