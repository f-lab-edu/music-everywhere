package me.kong.paymentservice.service;

import me.kong.paymentservice.domain.entity.PayEvent;
import me.kong.paymentservice.domain.repository.PayEventRepository;
import me.kong.paymentservice.domain.entity.PaymentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PayEventServiceTest {

    @InjectMocks
    PayEventService payEventService;

    @Mock
    PayEventRepository payEventRepository;


    @Test
    @DisplayName("결제 이벤트 정보를 저장한다")
    void success_pay_process() {
        //given
        BigDecimal amount = new BigDecimal("100");
        PaymentStatus status = PaymentStatus.SUCCESS;
        Long userId = 1L;

        //when
        payEventService.savePayResult(amount, status, userId);

        //then
        verify(payEventRepository, times(1)).save(any(PayEvent.class));
    }
}