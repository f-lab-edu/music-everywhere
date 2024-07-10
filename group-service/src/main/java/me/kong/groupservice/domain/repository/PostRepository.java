package me.kong.groupservice.domain.repository;

import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p " +
            " join fetch p.group g" +
            " join fetch p.profile pf" +
            " where p.state = :state and g.id = :groupId")
    Page<Post> findRecentPost(@Param("groupId") Long groupId, @Param("state") State state, Pageable pageable);
}
