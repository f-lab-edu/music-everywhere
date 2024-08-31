package me.kong.paymentservice.service;


import lombok.RequiredArgsConstructor;
import me.kong.commonlibrary.event.dto.GroupMemberIncreaseRequestDto;
import me.kong.paymentservice.domain.entity.PayEvent;
import me.kong.paymentservice.domain.repository.PayEventRepository;
import me.kong.paymentservice.mapper.GroupMemberIncreaseMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PayEventService {

    private final PayEventRepository payEventRepository;
    private final GroupMemberIncreaseMapper memberIncreaseMapper;

    @Transactional(readOnly = true)
    public PayEvent findPayEventById(Long id) {
        return payEventRepository.findById(id).orElseThrow(() -> new NoSuchElementException("찾으려는 이벤트 없음"));
    }

    @Transactional
    public PayEvent savePayEvent(GroupMemberIncreaseRequestDto dto) {
        return payEventRepository.save(memberIncreaseMapper.toPayEvent(dto));
    }
}
