package me.kong.paymentservice.service;

import me.kong.paymentservice.domain.entity.PayEvent;
import me.kong.paymentservice.domain.repository.PayEventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PayServiceTest {

    @InjectMocks
    SimplePayService payService;

    @Mock
    PayEventRepository payEventRepository;


    @Test
    @DisplayName("결제에 성공한다")
    void success_pay_process() {
        //given
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("100.00");
        PayEvent payEvent = PayEvent.builder()
                .amount(amount)
                .userId(userId)
                .build();
        when(payEventRepository.save(any(PayEvent.class))).thenReturn(payEvent);

        //when
        PayEvent savedEvent = payService.processPayment(amount, userId);

        //then
        assertEquals(userId, savedEvent.getUserId());
        assertEquals(amount, savedEvent.getAmount());
        verify(payEventRepository, times(1)).save(any(PayEvent.class));
    }
}
