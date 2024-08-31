package me.kong.commonlibrary.event.dto;

import lombok.Builder;
import lombok.Getter;
import me.kong.commonlibrary.event.BaseEvent;

import java.math.BigDecimal;

import static me.kong.commonlibrary.util.AmountCalculator.getGroupIncreaseAmount;


@Getter
public class GroupMemberIncreaseRequestDto {
    private Long groupId;
    private Long userId;
    private Integer additionalMembers;
    private BigDecimal amount;
    private BaseEvent event;

    @Builder
    public GroupMemberIncreaseRequestDto(Long groupId, Long userId, Integer additionalMembers) {
        this.groupId = groupId;
        this.userId = userId;
        this.additionalMembers = additionalMembers;
        this.amount = getGroupIncreaseAmount(additionalMembers);
        this.event = new BaseEvent(BaseEvent.Type.GROUP_SIZE_INCREASE);
    }

    @Builder
    public GroupMemberIncreaseRequestDto(Long groupId, Long userId, Integer additionalMembers, BigDecimal amount) {
        this.groupId = groupId;
        this.userId = userId;
        this.additionalMembers = additionalMembers;
        this.amount = amount;
    }
}
