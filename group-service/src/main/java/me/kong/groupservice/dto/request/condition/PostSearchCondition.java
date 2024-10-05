package me.kong.groupservice.dto.request.condition;


import lombok.Builder;
import lombok.Getter;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.post.PostScope;

@Getter
@Builder
public class PostSearchCondition {
    private Long groupId;
    private PostScope postScope;
    private State state;
    private String searchText;
}
