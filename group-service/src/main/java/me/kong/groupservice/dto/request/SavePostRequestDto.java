package me.kong.groupservice.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import me.kong.groupservice.domain.entity.post.PostScope;

@Getter
@Builder
public class SavePostRequestDto {

    @NotEmpty
    private String content;

    @NotNull
    private PostScope scope;
}
