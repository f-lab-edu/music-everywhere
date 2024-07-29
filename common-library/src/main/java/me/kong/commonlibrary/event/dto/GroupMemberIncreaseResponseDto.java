package me.kong.commonlibrary.event.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
public class GroupMemberIncreaseResponseDto {

    private Long groupId;
    private Long userId;
    private Integer additionalMembers;
    private BigDecimal amount;

    @Builder
    public GroupMemberIncreaseResponseDto(Long groupId, Long userId,
                                          Integer additionalMembers, BigDecimal amount) {
        this.groupId = groupId;
        this.userId = userId;
        this.additionalMembers = additionalMembers;
        this.amount = amount;
    }
}
