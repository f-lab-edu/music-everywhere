package me.kong.groupservice.dto.request.condition;


import lombok.Builder;
import lombok.Getter;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.post.PostScope;

@Getter
@Builder
public class PostSearchCondition {
    Long groupId;
    PostScope postScope;
    State state;
}
