package me.kong.groupservice.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kong.groupservice.dto.request.enums.GroupJoinProcessAction;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupJoinProcessDto {

    @NotNull
    private GroupJoinProcessAction action;
}
