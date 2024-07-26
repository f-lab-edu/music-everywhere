package me.kong.groupservice.dto.event;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

import static me.kong.groupservice.common.util.AmountCalculator.*;

@Getter
public class GroupMemberIncreaseRequestDto {
    private Long groupId;
    private Long userId;
    private Integer additionalMembers;
    private BigDecimal amount;

    @Builder
    public GroupMemberIncreaseRequestDto(Long groupId, Long userId, Integer additionalMembers) {
        this.groupId = groupId;
        this.userId = userId;
        this.additionalMembers = additionalMembers;
        this.amount = getGroupIncreaseAmount(additionalMembers);
    }
}
