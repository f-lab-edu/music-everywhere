package me.kong.groupservice.domain.entity.post;


import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "test_connect_posts")
@Getter
@NoArgsConstructor
public class PostESDocument {

    @Id
    private Long id;

    public PostESDocument(Long id) {
        this.id = id;
    }
}
