package me.kong.paymentservice.service;


import lombok.RequiredArgsConstructor;
import me.kong.paymentservice.domain.entity.PayEvent;
import me.kong.paymentservice.domain.entity.State;
import me.kong.paymentservice.domain.repository.PayEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PayEventService {

    private final PayEventRepository payEventRepository;

    @Transactional
    public PayEvent savePayResult(BigDecimal amount, State payState, Long userId) {
        PayEvent payEvent = PayEvent.builder()
                .amount(amount)
                .state(payState)
                .userId(userId)
                .build();
        return payEventRepository.save(payEvent);
    }
}
