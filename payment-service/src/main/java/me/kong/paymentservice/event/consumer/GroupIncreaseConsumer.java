package me.kong.paymentservice.event.consumer;

import lombok.RequiredArgsConstructor;
import me.kong.paymentservice.dto.event.GroupMemberIncreaseRequestDto;
import me.kong.paymentservice.service.PayService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static me.kong.paymentservice.common.EventConstants.*;


@Service
@RequiredArgsConstructor
public class GroupIncreaseConsumer {

    private final PayService payService;

    @KafkaListener(
            topics = GROUP_MEMBER_INCREASE_REQUEST,
            groupId = "group-increase-1",
            containerFactory = "groupIncreaseKafkaListenerContainerFactory")
    public void listen(GroupMemberIncreaseRequestDto dto) {
        payService.processPayRequest(dto);
    }
}
