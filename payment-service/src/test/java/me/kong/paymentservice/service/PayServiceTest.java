package me.kong.paymentservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PayServiceTest {

    @InjectMocks
    PayService payService;

    @Mock
    PayEventRepository payEventRepository;


    @Test
    @DisplayName("결제에 성공한다")
    void success_pay_process() {
        //given
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("100.00");

        //when
        PayEvent payEvent = payService.processPayment(userId, amount);

        //then
        verify(payEventRepository, times(1)).save(payEvent);
    }
}
