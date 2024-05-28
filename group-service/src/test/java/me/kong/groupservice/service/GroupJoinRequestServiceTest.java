package me.kong.groupservice.service;

import me.kong.groupservice.common.JwtReader;
import me.kong.groupservice.domain.entity.GroupJoinRequest.GroupJoinRequest;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.repository.GroupJoinRequestRepository;
import me.kong.groupservice.dto.request.GroupJoinRequestDto;
import me.kong.groupservice.mapper.GroupJoinRequestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GroupJoinRequestServiceTest {

    @InjectMocks
    GroupJoinRequestService groupJoinRequestService;

    @Mock
    GroupJoinRequestRepository groupJoinRequestRepository;

    @Mock
    GroupJoinRequestMapper groupJoinRequestMapper;

    @Mock
    JwtReader jwtReader;


    GroupJoinRequest request;
    GroupJoinRequestDto dto;
    Group group;

    @BeforeEach
    void init() {
        group = mock(Group.class);
        request = mock(GroupJoinRequest.class);
        dto = GroupJoinRequestDto.builder()
                .requestInfo("sample")
                .nickname("testuser")
                .build();
    }

    @Test
    @DisplayName("그룹 가입 요청 생성에 성공한다")
    void successToCreateNewGroupJoinRequest() {
        //given
        when(groupJoinRequestMapper.toEntity(any(GroupJoinRequestDto.class), any(Group.class)))
                .thenReturn(request);

        //when
        groupJoinRequestService.createNewGroupJoinRequest(dto, group);

        //then
        verify(groupJoinRequestRepository, times(1)).save(request);
        verify(groupJoinRequestMapper, times(1)).toEntity(dto, group);
    }

    @Test
    @DisplayName("데이터베이스에 문제가 있을 경우 예외가 발생한다")
    void failedByDatabaseProblem() {
        //given
        when(groupJoinRequestRepository.save(any())).thenThrow(RuntimeException.class);

        //then
        assertThrows(RuntimeException.class, () -> groupJoinRequestService.createNewGroupJoinRequest(dto, group));
    }

}