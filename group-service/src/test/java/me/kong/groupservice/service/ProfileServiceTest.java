package me.kong.groupservice.service;

import me.kong.groupservice.common.JwtReader;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.domain.repository.ProfileRepository;
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


    @Test
    @DisplayName("프로필 생성에 성공한다")
    void successToCreateNewProfile() {
        // Given
        String nickname = "testUser";
        GroupRole groupRole = GroupRole.MEMBER;
        Group group = mock(Group.class);
        when(jwtReader.getUserId()).thenReturn(1L);

        //when
        profileService.createNewProfile(nickname, groupRole, group);

        //then
        verify(jwtReader, times(1)).getUserId();
        verify(profileRepository, times(1)).save(any(Profile.class));
    }
}