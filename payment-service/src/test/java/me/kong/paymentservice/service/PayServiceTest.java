package me.kong.paymentservice.service;

import me.kong.commonlibrary.event.dto.GroupMemberIncreaseRequestDto;
import me.kong.commonlibrary.event.dto.GroupMemberIncreaseResponseDto;
import me.kong.paymentservice.domain.entity.PayEvent;
import me.kong.paymentservice.domain.entity.PaymentStatus;

import me.kong.paymentservice.domain.repository.PayEventRepository;
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

import static me.kong.commonlibrary.event.EventConstants.GROUP_MEMBER_INCREASE_RESPONSE;
import static me.kong.paymentservice.domain.entity.PaymentStatus.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PayServiceTest {

    @InjectMocks
    PayService payService;

    @Mock
    PayStrategy payStrategy;

    @Mock
    GroupMemberIncreaseMapper groupMemberIncreaseMapper;

    @Mock
    KafkaProducer kafkaProducer;

    @Mock
    PayEventRepository payEventRepository;

    Long groupId = 1L;
    Long userId = 2L;
    Integer additionalMembers = 10;
    BigDecimal amount = new BigDecimal(1000);

    GroupMemberIncreaseRequestDto requestDto;
    GroupMemberIncreaseResponseDto responseDto;
    PayEvent payEvent;


    @Test
    @DisplayName("결제 성공 시 성공 로그를 남긴다")
    void success_pay_process() {
        //given
        payProcessSetting(SUCCESS);
        when(payStrategy.process(amount, userId)).thenReturn(true);
        responseDto = GroupMemberIncreaseResponseDto.builder()
                .groupId(groupId)
                .userId(userId)
                .additionalMembers(additionalMembers)
                .amount(amount)
                .build();
        when(groupMemberIncreaseMapper.toResponse(requestDto, SUCCESS)).thenReturn(responseDto);
        when(groupMemberIncreaseMapper.toPayEvent(requestDto, SUCCESS)).thenReturn(payEvent);

        //when
        payService.processPayRequest(requestDto);

        //then
        verify(payEventRepository, times(1)).save(payEvent);
        verify(kafkaProducer, times(1)).send(GROUP_MEMBER_INCREASE_RESPONSE, responseDto);
    }

    @Test
    @DisplayName("결제 실패 시 실패 로그를 남긴다")
    void fail_pay_process() {
        //given
        payProcessSetting(FAIL);
        when(payStrategy.process(amount, userId)).thenReturn(false);
        when(groupMemberIncreaseMapper.toPayEvent(requestDto, FAIL)).thenReturn(payEvent);

        //when
        payService.processPayRequest(requestDto);

        //then
        verify(payEventRepository, times(1)).save(payEvent);
    }


    private void payProcessSetting(PaymentStatus status) {
        requestDto = GroupMemberIncreaseRequestDto.builder()
                .groupId(groupId)
                .userId(userId)
                .additionalMembers(additionalMembers)
                .amount(amount)
                .build();

        payEvent = PayEvent.builder()
                .amount(requestDto.getAmount())
                .userId(requestDto.getUserId())
                .status(status)
                .baseEvent(requestDto.getEvent())
                .build();
    }
}
