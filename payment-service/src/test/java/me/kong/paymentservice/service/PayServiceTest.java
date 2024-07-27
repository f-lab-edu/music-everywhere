package me.kong.paymentservice.service;

import me.kong.paymentservice.dto.enums.PaymentStatus;
import me.kong.paymentservice.dto.event.GroupMemberIncreaseRequestDto;
import me.kong.paymentservice.dto.event.GroupMemberIncreaseResponseDto;
import me.kong.paymentservice.event.KafkaProducer;
import me.kong.paymentservice.mapper.GroupMemberIncreaseMapper;
import me.kong.paymentservice.service.strategy.PayStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static me.kong.paymentservice.common.EventConstants.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PayServiceTest {

    @InjectMocks
    PayService payService;

    @Mock
    PayEventService payEventService;

    @Mock
    PayStrategy payStrategy;

    @Mock
    GroupMemberIncreaseMapper groupMemberIncreaseMapper;

    @Mock
    KafkaProducer kafkaProducer;


    Long groupId = 1L;
    Long userId = 2L;
    Integer additionalMembers = 10;
    BigDecimal amount = new BigDecimal(1000);

    GroupMemberIncreaseRequestDto requestDto;
    GroupMemberIncreaseResponseDto responseDto;


    @Test
    @DisplayName("결제 성공 시 성공 로그를 남긴다")
    void success_pay_process() {
        //given
        payProcessSetting();
        when(payStrategy.process(amount, userId)).thenReturn(true);
        responseDto = GroupMemberIncreaseResponseDto.builder()
                .groupId(groupId)
                .userId(userId)
                .additionalMembers(additionalMembers)
                .amount(amount)
                .status(PaymentStatus.SUCCESS)
                .build();
        when(groupMemberIncreaseMapper.toResponse(requestDto, PaymentStatus.SUCCESS))
                .thenReturn(responseDto);

        //when
        payService.processPayRequest(requestDto);

        //then
        verify(payEventService, times(1)).savePayResult(amount, PaymentStatus.SUCCESS, userId);
        verify(kafkaProducer, times(1)).send(GROUP_MEMBER_INCREASE_RESPONSE, responseDto);
    }

    @Test
    @DisplayName("결제 실패 시 실패 로그를 남긴다")
    void fail_pay_process() {
        //given
        payProcessSetting();
        when(payStrategy.process(amount, userId)).thenReturn(false);

        //when
        payService.processPayRequest(requestDto);

        //then
        verify(payEventService, times(1)).savePayResult(amount, PaymentStatus.FAIL, userId);
    }

    private void payProcessSetting() {
        requestDto = GroupMemberIncreaseRequestDto.builder()
                .groupId(groupId)
                .userId(userId)
                .additionalMembers(additionalMembers)
                .amount(amount)
                .build();
    }
}
