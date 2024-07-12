package me.kong.groupservice.domain.repository;

import me.kong.groupservice.domain.entity.post.Post;
import me.kong.groupservice.domain.repository.query.CustomPostRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
}
