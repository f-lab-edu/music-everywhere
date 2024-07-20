package me.kong.paymentservice.service;

import me.kong.paymentservice.domain.entity.PayEvent;
import me.kong.paymentservice.domain.repository.PayEventRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SimplePayService implements PayService {

    PayEventRepository payEventRepository;

    @Override
    public PayEvent processPayment(BigDecimal amount, Long userId) {
        return payEventRepository.save(PayEvent.builder()
                .amount(amount)
                .userId(userId)
                .build());
    }
}
