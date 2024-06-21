package me.kong.groupservice.service;

import me.kong.commonlibrary.util.JwtReader;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.repository.GroupRepository;
import me.kong.groupservice.dto.request.SaveGroupRequestDto;
import me.kong.groupservice.mapper.GroupMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @InjectMocks
    GroupService groupService;

    @Mock
    GroupRepository groupRepository;

    @Mock
    GroupMapper groupMapper;

    @Mock
    ProfileService profileService;

    SaveGroupRequestDto dto;
    Group group;
    Long userId = 1L;

    @Test
    @DisplayName("그룹 생성에 성공한다")
    void successToCreateNewGroup() {
        //given
        dto = mock(SaveGroupRequestDto.class);
        group = mock(Group.class);
        when(dto.getNickname()).thenReturn("test");
        when(groupMapper.toEntity(any(SaveGroupRequestDto.class))).thenReturn(group);
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        //when
        groupService.createNewGroup(dto, userId);

        //then
        verify(groupRepository, times(1)).save(any(Group.class));
        verify(profileService, times(1))
                .createNewProfile(eq("test"), eq(1L), eq(GroupRole.MANAGER), eq(group));
    }

    @Test
    @DisplayName("데이터베이스 문제가 있을 경우 예외가 발생한다")
    void failToCreateNewGroupByDatabaseProblem() {
        //given
        when(groupRepository.save(any())).thenThrow(RuntimeException.class);

        //then
        assertThrows(RuntimeException.class, () -> groupService.createNewGroup(dto, userId));
    }
}