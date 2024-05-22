package me.kong.groupservice.service;

import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.group.GroupScope;
import me.kong.groupservice.domain.entity.group.JoinCondition;
import me.kong.groupservice.domain.repository.GroupRepository;
import me.kong.groupservice.dto.request.SaveGroupRequestDto;
import me.kong.groupservice.mapper.GroupMapper;
import org.junit.jupiter.api.BeforeEach;
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
    ProfileService profileService;

    @Mock
    GroupMapper groupMapper;


    @Test
    @DisplayName("그룹 생성에 성공한다")
    void successToCreateNewGroup() {
        //given
        SaveGroupRequestDto dto = mock();
        Group group = mock();

        when(groupMapper.toEntity(any(SaveGroupRequestDto.class))).thenReturn(group);

        //when
        groupService.createNewGroup(dto);

        //then
        verify(groupRepository, times(1)).save(any(Group.class));
        verify(groupMapper, times(1)).toEntity(any(SaveGroupRequestDto.class));
    }
}