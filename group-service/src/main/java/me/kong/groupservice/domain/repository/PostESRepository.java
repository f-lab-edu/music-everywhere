package me.kong.groupservice.domain.repository;

import me.kong.groupservice.domain.entity.post.PostESDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostESRepository extends ElasticsearchRepository<PostESDocument, Long> {

    @Query("{\"match\": {\"content\": \"?0\"}}")
    Page<PostESDocument> findByKeyword(String keyword, Pageable pageable);
}
