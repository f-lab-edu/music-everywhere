package me.kong.groupservice.integration;


import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.group.GroupScope;
import me.kong.groupservice.domain.entity.group.JoinCondition;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.repository.GroupRepository;
import me.kong.groupservice.domain.repository.ProfileRepository;
import me.kong.groupservice.dto.request.SaveGroupRequestDto;
import me.kong.groupservice.service.GroupService;
import me.kong.groupservice.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class GroupManagementServiceTest {

    @Autowired
    private GroupService groupService;

    @MockBean
    private ProfileService profileService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @BeforeEach
    void init() {
        profileRepository.deleteAll();
        groupRepository.deleteAll();
    }


    @Test
    @DisplayName("프로필 생성 시 예외가 발생하면 그룹과 프로필 모두 롤백된다")
    void createGroupTransactionPropagationTest() {
        //given
        SaveGroupRequestDto dto = SaveGroupRequestDto.builder()
                .groupName("테스트 그룹")
                .description("트랜잭션 전파 동작 테스트용")
                .groupScope(GroupScope.PUBLIC)
                .joinCondition(JoinCondition.OPEN)
                .nickname("testUser")
                .build();

        doThrow(new RuntimeException())
                .when(profileService).createNewProfile(anyString(), any(Long.class), any(GroupRole.class), any(Group.class));

        //when
        try {
            groupService.createNewGroup(dto);
        } catch (RuntimeException e) {
            // 예외 발생!!!
        }

        //then
        assertTrue(groupRepository.findAll().isEmpty());
        assertTrue(profileRepository.findAll().isEmpty());
    }
}
