package me.kong.paymentservice.service;

import me.kong.paymentservice.domain.repository.PayEventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayEventServiceTest {

    @InjectMocks
    PayEventService payEventService;

    @Mock
    PayEventRepository payEventRepository;


    Long eventId = 1L;


    @Test
    @DisplayName("찾으려는 결제 이벤트가 없을 경우 예외가 발생한다")
    void fail_to_find_pay_event() {
        //given
        when(payEventRepository.findById(eventId)).thenThrow(NoSuchElementException.class);

        //when & then
        assertThrows(NoSuchElementException.class, () -> payEventService.findPayEventById(eventId));
    }
}