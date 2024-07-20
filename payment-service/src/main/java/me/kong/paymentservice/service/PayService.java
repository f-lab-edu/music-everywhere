package me.kong.paymentservice.service;

import me.kong.paymentservice.domain.entity.PayEvent;

import java.math.BigDecimal;

public interface PayService {

    PayEvent processPayment(BigDecimal amount, Long userId);

}
