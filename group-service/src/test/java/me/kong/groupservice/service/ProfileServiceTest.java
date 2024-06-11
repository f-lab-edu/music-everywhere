package me.kong.groupservice.service;

import me.kong.commonlibrary.exception.auth.UnAuthorizedException;
import me.kong.commonlibrary.util.JwtReader;
import me.kong.groupservice.common.exception.GroupFullException;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.common.Constants;
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

import java.util.Optional;

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
    Long userId;

    @BeforeEach
    void init() {
        nickname = "testUser";
        userId = 1L;
        groupRole = GroupRole.MANAGER;
        group = mock(Group.class);
    }


    @Test
    @DisplayName("프로필 생성에 성공한다")
    void successToCreateNewProfile() {
        // Given

        //when
        profileService.createNewProfile(nickname, userId, groupRole, group);

        //then
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    @DisplayName("데이터베이스 문제가 있을 경우 예외가 발생한다")
    void failToCreateNewProfileByDatabaseProblem() {
        //given
        when(profileRepository.save(any())).thenThrow(RuntimeException.class);

        //then
        assertThrows(RuntimeException.class, () -> profileService.createNewProfile(nickname, userId, groupRole, group));
    }

    @Test
    @DisplayName("그룹 매니저가 아닐 경우 예외가 발생한다")
    void unAuthorizeOccurred() {
        //given
        Long groupId = 1L;
        Profile profile = Profile.builder()
                .groupRole(GroupRole.MEMBER)
                .build();
        when(jwtReader.getUserId()).thenReturn(userId);
        when(profileRepository.findByUserIdAndGroupId(userId, groupId)).thenReturn(Optional.of(profile));

        //then
        assertThrows(UnAuthorizedException.class, () -> profileService.checkLoggedInProfileIsGroupManager(groupId));
    }

    @Test
    @DisplayName("그룹이 가득 차지 않았다면 예외가 발생하지 않는다")
    void isNotFullGroup() {
        //given
        Long groupId = 1L;
        when(profileRepository.countByGroupIdAndState(groupId, State.GENERAL)).thenReturn(Constants.BASIC-1);
        when(group.getId()).thenReturn(groupId);
        when(group.getGroupSize()).thenReturn(Constants.BASIC);

        //when
        profileService.checkGroupSize(group);

        //then
        verify(profileRepository, times(1)).countByGroupIdAndState(groupId, State.GENERAL);
    }

    @Test
    @DisplayName("그룹이 가득 찼다면 예외가 발생한다")
    void isFullGroupThrowsException() {
        //given
        Long groupId = 1L;
        when(profileRepository.countByGroupIdAndState(groupId, State.GENERAL)).thenReturn(Constants.BASIC);
        when(group.getId()).thenReturn(groupId);
        when(group.getGroupSize()).thenReturn(Constants.BASIC);

        //then
        assertThrows(GroupFullException.class, () -> profileService.checkGroupSize(group));
    }
}