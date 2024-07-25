package me.kong.paymentservice.service.strategy;

import java.math.BigDecimal;

public interface PayStrategy {

    boolean process(BigDecimal amount, Long userId);

}
