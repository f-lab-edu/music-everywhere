package me.kong.paymentservice.service.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class SimplePayStrategy implements PayStrategy {

    @Override
    public boolean process(BigDecimal amount, Long userId) {
        return true;
    }
}
