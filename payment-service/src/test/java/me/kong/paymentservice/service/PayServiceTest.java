package me.kong.paymentservice.service;

import me.kong.commonlibrary.event.dto.GroupMemberIncreaseRequestDto;
import me.kong.commonlibrary.event.dto.GroupMemberIncreaseResponseDto;
import me.kong.paymentservice.domain.entity.PayEvent;

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
import static org.junit.jupiter.api.Assertions.*;
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
    PayEventService payEventService;

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
        payProcessWithResponseSetting();
        when(payStrategy.process(amount, userId)).thenReturn(true);
        when(groupMemberIncreaseMapper.toResponse(requestDto, SUCCESS)).thenReturn(responseDto);

        //when
        payService.processPayRequest(any(Long.class), requestDto);

        //then
        verify(kafkaProducer, times(1)).send(GROUP_MEMBER_INCREASE_RESPONSE, responseDto);
        assertEquals(SUCCESS, payEvent.getStatus());
        assertNotNull(payEvent.getBaseEvent().getProcessedAt());
    }

    @Test
    @DisplayName("결제 실패 시 실패 로그를 남긴다")
    void fail_pay_process() {
        //given
        payProcessSetting();
        when(payStrategy.process(amount, userId)).thenReturn(false);

        //when
        payService.processPayRequest(any(Long.class), requestDto);

        //then
        assertEquals(FAIL, payEvent.getStatus());
        assertNotNull(payEvent.getBaseEvent().getProcessedAt());
    }

    @Test
    @DisplayName("결제 완료 후 이벤트 발행에 실패했을 경우, 처리 시간이 null 이다")
    void fail_after_pay_process() {
        //given
        payProcessWithResponseSetting();
        when(payStrategy.process(amount, userId)).thenReturn(true);
        doThrow(new RuntimeException()).when(kafkaProducer).send(any(), any());

        //when
        payService.processPayRequest(any(Long.class), requestDto);

        //then
        assertEquals(SUCCESS, payEvent.getStatus());
        assertNull(payEvent.getBaseEvent().getProcessedAt());
    }


    private void payProcessSetting() {
        requestDto = GroupMemberIncreaseRequestDto.builder()
                .groupId(groupId)
                .userId(userId)
                .additionalMembers(additionalMembers)
                .amount(amount)
                .build();

        payEvent = PayEvent.builder()
                .amount(requestDto.getAmount())
                .userId(requestDto.getUserId())
                .baseEvent(requestDto.getEvent())
                .build();

        when(payEventService.findPayEventById(any(Long.class))).thenReturn(payEvent);
    }

    private void payProcessWithResponseSetting() {
        payProcessSetting();
        responseDto = GroupMemberIncreaseResponseDto.builder()
                .groupId(groupId)
                .userId(userId)
                .additionalMembers(additionalMembers)
                .amount(amount)
                .build();
    }
}
