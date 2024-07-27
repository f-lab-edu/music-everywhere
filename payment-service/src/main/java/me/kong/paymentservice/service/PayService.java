package me.kong.paymentservice.service;

import lombok.RequiredArgsConstructor;
import me.kong.paymentservice.dto.enums.PaymentStatus;
import me.kong.paymentservice.dto.event.GroupMemberIncreaseRequestDto;
import me.kong.paymentservice.event.KafkaProducer;
import me.kong.paymentservice.mapper.GroupMemberIncreaseMapper;
import me.kong.paymentservice.service.strategy.PayStrategy;
import org.springframework.stereotype.Service;

import static me.kong.paymentservice.common.EventConstants.*;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayEventService payEventService;
    private final PayStrategy payStrategy;
    private final GroupMemberIncreaseMapper groupMemberIncreaseMapper;
    private final KafkaProducer kafkaProducer;

    public void processPayRequest(GroupMemberIncreaseRequestDto dto) {
        if (payStrategy.process(dto.getAmount(), dto.getUserId())) {
            payEventService.savePayResult(dto.getAmount(), PaymentStatus.SUCCESS, dto.getUserId());

            kafkaProducer.send(GROUP_MEMBER_INCREASE_RESPONSE, groupMemberIncreaseMapper.toResponse(dto, PaymentStatus.SUCCESS));
        } else {
            payEventService.savePayResult(dto.getAmount(), PaymentStatus.FAIL, dto.getUserId());
        }
    }
}