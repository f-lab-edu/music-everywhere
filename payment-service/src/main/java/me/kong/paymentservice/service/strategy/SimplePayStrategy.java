package me.kong.paymentservice.service.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class SimplePayStrategy implements PayStrategy {

    @Override
    public boolean process(BigDecimal amount, Long userId) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
