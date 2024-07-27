package me.kong.paymentservice.mapper;


import me.kong.paymentservice.dto.enums.PaymentStatus;
import me.kong.paymentservice.dto.event.GroupMemberIncreaseRequestDto;
import me.kong.paymentservice.dto.event.GroupMemberIncreaseResponseDto;
import org.springframework.stereotype.Component;

@Component
public class GroupMemberIncreaseMapper {

    public GroupMemberIncreaseResponseDto toResponse(GroupMemberIncreaseRequestDto requestDto, PaymentStatus status) {
        return GroupMemberIncreaseResponseDto.builder()
                .groupId(requestDto.getGroupId())
                .userId(requestDto.getUserId())
                .additionalMembers(requestDto.getAdditionalMembers())
                .amount(requestDto.getAmount())
                .status(status)
                .build();
    }

}
