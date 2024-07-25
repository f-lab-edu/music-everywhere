package me.kong.paymentservice.service;

import me.kong.paymentservice.domain.entity.PayEvent;
import me.kong.paymentservice.domain.entity.State;
import me.kong.paymentservice.domain.repository.PayEventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
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
        State state = State.SUCCESS;
        Long userId = 1L;

        //when
        payEventService.savePayResult(amount, state, userId);

        //then
        verify(payEventRepository, times(1)).save(any(PayEvent.class));
    }
}