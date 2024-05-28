package me.kong.groupservice.service;

import me.kong.groupservice.common.JwtReader;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.domain.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @InjectMocks
    ProfileService profileService;

    @Mock
    ProfileRepository profileRepository;

    @Mock
    JwtReader jwtReader;

    String nickname;
    GroupRole groupRole;
    Group group;

    @BeforeEach
    void init() {
        nickname = "testUser";
        groupRole = GroupRole.MANAGER;
        group = mock(Group.class);
    }


    @Test
    @DisplayName("프로필 생성에 성공한다")
    void successToCreateNewProfile() {
        // Given
        when(jwtReader.getUserId()).thenReturn(1L);

        //when
        profileService.createNewProfile(nickname, groupRole, group);

        //then
        verify(jwtReader, times(1)).getUserId();
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    @DisplayName("데이터베이스 문제가 있을 경우 예외가 발생한다")
    void failToCreateNewProfileByDatabaseProblem() {
        //given
        when(profileRepository.save(any())).thenThrow(RuntimeException.class);

        //then
        assertThrows(RuntimeException.class, () -> profileService.createNewProfile(nickname, groupRole, group));
    }
}