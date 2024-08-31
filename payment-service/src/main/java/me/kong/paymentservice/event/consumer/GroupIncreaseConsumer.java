package me.kong.paymentservice.event.consumer;

import lombok.RequiredArgsConstructor;
import me.kong.commonlibrary.event.dto.GroupMemberIncreaseRequestDto;
import me.kong.paymentservice.domain.entity.PayEvent;
import me.kong.paymentservice.service.PayEventService;
import me.kong.paymentservice.service.PayService;
import me.kong.paymentservice.service.strategy.PayStrategy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import static me.kong.commonlibrary.event.EventConstants.GROUP_MEMBER_INCREASE_REQUEST;

@Service
@RequiredArgsConstructor
public class GroupIncreaseConsumer {

    private final PayService payService;
    private final PayStrategy payStrategy;
    private final PayEventService payEventService;


    @KafkaListener(
            topics = GROUP_MEMBER_INCREASE_REQUEST,
            groupId = "group-increase-1",
            containerFactory = "groupIncreaseKafkaListenerContainerFactory")
    public void listen(GroupMemberIncreaseRequestDto message, Acknowledgment ack) {
        // 메시지를 받자마자 이벤트 히스토리 기록 & 커밋하기
        PayEvent payEvent = payEventService.savePayEvent(message);
        ack.acknowledge();

        // 결제 시도하기
        payService.processPayRequest(payEvent.getId(), message);
    }
}
