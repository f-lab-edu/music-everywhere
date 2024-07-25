package me.kong.paymentservice.service;

import lombok.RequiredArgsConstructor;
import me.kong.paymentservice.domain.entity.State;
import me.kong.paymentservice.service.strategy.PayStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayEventService payEventService;
    private final PayStrategy payStrategy;

    public void processPayRequest(BigDecimal amount, Long userId) {
        if (payStrategy.process(amount, userId)) {
            payEventService.savePayResult(amount, State.SUCCESS, userId);
        } else {
            payEventService.savePayResult(amount, State.FAIL, userId);
        }

    }
}
