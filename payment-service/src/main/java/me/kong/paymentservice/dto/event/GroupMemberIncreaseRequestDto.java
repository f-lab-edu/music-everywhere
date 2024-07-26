package me.kong.paymentservice.dto.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;


@NoArgsConstructor
@Getter
@ToString
public class GroupMemberIncreaseRequestDto {
    private Long groupId;
    private Long userId;
    private Integer additionalMembers;
    private BigDecimal amount;

    @Builder
    public GroupMemberIncreaseRequestDto(Long groupId, Long userId, Integer additionalMembers, BigDecimal amount) {
        this.groupId = groupId;
        this.userId = userId;
        this.additionalMembers = additionalMembers;
        this.amount = amount;
    }
}
