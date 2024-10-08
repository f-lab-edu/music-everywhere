package me.kong.groupservice.domain.repository;

import me.kong.groupservice.domain.entity.post.Post;
import me.kong.groupservice.domain.repository.query.CustomPostRepository;
import me.kong.groupservice.dto.response.PostListResponseDto;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

    @Query("SELECT new me.kong.groupservice.dto.response.PostListResponseDto( " +
            "p.id, " +
            "SUBSTRING(p.content, 1, 300), " +
            "p.group.id, " +
            "p.profile.id, " +
            "p.profile.nickname, " +
            "p.updatedDate) " +
            "FROM Post p " +
            "JOIN p.profile pr " +
            "WHERE p.id IN :postIds " +
            "ORDER BY p.id DESC")
    Slice<PostListResponseDto> findPostListsByIds(@Param("postIds") List<Long> postIds);
}
