package me.kong.groupservice.service;

import lombok.RequiredArgsConstructor;
import me.kong.groupservice.common.JwtReader;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.repository.GroupJoinRequestRepository;
import me.kong.groupservice.dto.request.GroupJoinRequestDto;
import me.kong.groupservice.mapper.GroupJoinRequestMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupJoinRequestService {

    private final JwtReader jwtReader;
    private final GroupJoinRequestRepository joinRequestRepository;
    private final GroupJoinRequestMapper joinRequestMapper;

    @Transactional(readOnly = true)
    public boolean pendingRequestExists(Long groupId) {
        return joinRequestRepository.pendingRequestExists(jwtReader.getUserId(), groupId);
    }

    @Transactional
    public void createNewGroupJoinRequest(GroupJoinRequestDto dto, Group group) {
        joinRequestRepository.save(joinRequestMapper.toEntity(dto, group));
    }
}
