package me.kong.groupservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import me.kong.groupservice.domain.entity.group.GroupScope;
import me.kong.groupservice.domain.entity.group.JoinCondition;

@Getter
@Builder
public class SaveGroupRequestDto {

    @NotEmpty
    private String groupName;

    private String description;

    @NotNull
    private GroupScope groupScope;

    @NotNull
    private JoinCondition joinCondition;

    @NotEmpty
    private String nickname;
}
