package me.kong.paymentservice.service;

import me.kong.paymentservice.domain.entity.State;
import me.kong.paymentservice.domain.repository.PayEventRepository;
import me.kong.paymentservice.service.strategy.PayStrategy;
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
    PayEventService payEventService;

    @Mock
    PayStrategy payStrategy;


    BigDecimal amount;
    Long userId;


    @Test
    @DisplayName("결제 성공 시 성공 로그를 남긴다")
    void success_pay_process() {
        //given
        payProcessSetting();
        when(payStrategy.process(amount, userId)).thenReturn(true);

        //when
        payService.processPayRequest(amount, userId);

        //then
        verify(payEventService, times(1)).savePayResult(amount, State.SUCCESS, userId);
    }

    @Test
    @DisplayName("결제 실패 시 실패 로그를 남긴다")
    void fail_pay_process() {
        //given
        payProcessSetting();
        when(payStrategy.process(amount, userId)).thenReturn(false);

        //when
        payService.processPayRequest(amount, userId);

        //then
        verify(payEventService, times(1)).savePayResult(amount, State.FAIL, userId);
    }

    private void payProcessSetting() {
        amount = new BigDecimal("100");
        userId = 1L;
    }
}
