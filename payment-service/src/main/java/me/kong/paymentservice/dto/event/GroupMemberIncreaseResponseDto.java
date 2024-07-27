package me.kong.paymentservice.dto.event;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kong.paymentservice.dto.enums.PaymentStatus;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
public class GroupMemberIncreaseResponseDto {

    private Long groupId;
    private Long userId;
    private Integer additionalMembers;
    private BigDecimal amount;
    private PaymentStatus status;

    @Builder
    public GroupMemberIncreaseResponseDto(Long groupId, Long userId,
                                          Integer additionalMembers, BigDecimal amount, PaymentStatus status) {
        this.groupId = groupId;
        this.userId = userId;
        this.additionalMembers = additionalMembers;
        this.amount = amount;
        this.status = status;
    }
}
