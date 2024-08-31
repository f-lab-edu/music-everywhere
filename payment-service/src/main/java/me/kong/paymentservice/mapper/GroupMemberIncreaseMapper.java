package me.kong.paymentservice.mapper;


import me.kong.commonlibrary.event.dto.GroupMemberIncreaseRequestDto;
import me.kong.commonlibrary.event.dto.GroupMemberIncreaseResponseDto;
import me.kong.paymentservice.domain.entity.PayEvent;
import me.kong.paymentservice.domain.entity.PaymentStatus;

import org.springframework.stereotype.Component;

@Component
public class GroupMemberIncreaseMapper {

    public GroupMemberIncreaseResponseDto toResponse(GroupMemberIncreaseRequestDto requestDto, PaymentStatus status) {
        return GroupMemberIncreaseResponseDto.builder()
                .groupId(requestDto.getGroupId())
                .userId(requestDto.getUserId())
                .additionalMembers(requestDto.getAdditionalMembers())
                .amount(requestDto.getAmount())
                .build();
    }

    public PayEvent toPayEvent(GroupMemberIncreaseRequestDto requestDto) {
        return PayEvent.builder()
                .amount(requestDto.getAmount())
                .userId(requestDto.getUserId())
                .baseEvent(requestDto.getEvent())
                .build();
    }

}
