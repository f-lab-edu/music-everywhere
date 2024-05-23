package me.kong.groupservice.service;

import lombok.RequiredArgsConstructor;
import me.kong.groupservice.common.JwtReader;
import me.kong.groupservice.common.exception.NoLoggedInProfileException;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.domain.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final JwtReader jwtReader;

    @Transactional
    public void createNewProfile(String nickname, GroupRole groupRole, Group group) {
        Profile profile = Profile.builder()
                .nickname(nickname)
                .groupRole(groupRole)
                .state(State.GENERAL)
                .userId(jwtReader.getUserId()) // token에서 현재 로그인한 user의 id를 가져온다.
                .group(group)
                .build();

        profileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public Profile getLoggedInProfile(Long groupId) {
        return getOptionalLoggedInProfile(groupId).orElseThrow(NoLoggedInProfileException::new);
    }

    @Transactional(readOnly = true)
    public Optional<Profile> getOptionalLoggedInProfile(Long groupId) {
        Long userId = jwtReader.getUserId();

        return profileRepository.findByUserIdAndGroupId(userId, groupId);
    }

}
