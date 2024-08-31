package me.kong.paymentservice.service;

import lombok.RequiredArgsConstructor;
import me.kong.commonlibrary.event.dto.GroupMemberIncreaseRequestDto;
import me.kong.paymentservice.domain.entity.PaymentStatus;
import me.kong.paymentservice.domain.repository.PayEventRepository;
import me.kong.paymentservice.event.KafkaProducer;
import me.kong.paymentservice.mapper.GroupMemberIncreaseMapper;
import me.kong.paymentservice.service.strategy.PayStrategy;
import org.springframework.stereotype.Service;

import static me.kong.commonlibrary.event.EventConstants.GROUP_MEMBER_INCREASE_RESPONSE;
import static me.kong.paymentservice.domain.entity.PaymentStatus.*;


@Service
@RequiredArgsConstructor
public class PayService {

    private final PayEventRepository payEventRepository;
    private final PayStrategy payStrategy;
    private final GroupMemberIncreaseMapper groupMemberIncreaseMapper;
    private final KafkaProducer kafkaProducer;

    public void processPayRequest(GroupMemberIncreaseRequestDto dto) {
        PaymentStatus status = payStrategy.process(dto.getAmount(), dto.getUserId()) ? SUCCESS : FAIL;

        if (status == SUCCESS) {
            kafkaProducer.send(GROUP_MEMBER_INCREASE_RESPONSE, groupMemberIncreaseMapper.toResponse(dto, status));
        }

        payEventRepository.save(groupMemberIncreaseMapper.toPayEvent(dto, status));
    }
}
