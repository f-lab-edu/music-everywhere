package me.kong.paymentservice.service;

import lombok.RequiredArgsConstructor;
import me.kong.commonlibrary.event.dto.GroupMemberIncreaseRequestDto;
import me.kong.paymentservice.domain.entity.PayEvent;
import me.kong.paymentservice.domain.entity.PaymentStatus;
import me.kong.paymentservice.event.KafkaProducer;
import me.kong.paymentservice.mapper.GroupMemberIncreaseMapper;
import me.kong.paymentservice.service.strategy.PayStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static me.kong.commonlibrary.event.EventConstants.GROUP_MEMBER_INCREASE_RESPONSE;
import static me.kong.paymentservice.domain.entity.PaymentStatus.*;


@Service
@RequiredArgsConstructor
public class PayService {

    private final PayEventService payEventService;
    private final PayStrategy payStrategy;
    private final GroupMemberIncreaseMapper groupMemberIncreaseMapper;
    private final KafkaProducer kafkaProducer;

    @Transactional
    public void processPayRequest(Long eventId, GroupMemberIncreaseRequestDto dto) {
        PayEvent payEvent = payEventService.findPayEventById(eventId);

        PaymentStatus status = payStrategy.process(dto.getAmount(), dto.getUserId()) ? SUCCESS : FAIL;
        // 이벤트 결과 기록
        payEvent.setStatus(status);

        try {
            if (status == SUCCESS) {
                kafkaProducer.send(GROUP_MEMBER_INCREASE_RESPONSE, groupMemberIncreaseMapper.toResponse(dto, status));
            }
            // 이벤트 완전 처리 시점 기록
            payEvent.getBaseEvent().setProcessedAt(LocalDateTime.now());
        } catch (Exception e) {
            // 예외 처리 필요
        }
    }

    @Transactional
    public boolean processPayRequest() {
        Long tempUserId = 1L;
        BigDecimal tempAmount = BigDecimal.ZERO;

        return payStrategy.process(tempAmount, tempUserId);
    }
}
