package me.kong.groupservice.service;

import lombok.RequiredArgsConstructor;
import me.kong.commonlibrary.exception.auth.UnAuthorizedException;
import me.kong.commonlibrary.util.JwtReader;
import me.kong.groupservice.common.exception.GroupFullException;
import me.kong.groupservice.common.exception.NoLoggedInProfileException;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.domain.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional
    public void createNewProfile(String nickname, Long userId, GroupRole groupRole, Group group) {
        Profile profile = Profile.builder()
                .nickname(nickname)
                .groupRole(groupRole)
                .state(State.GENERAL)
                .userId(userId)
                .group(group)
                .build();

        profileRepository.save(profile);
    }

    @Transactional(readOnly = true, noRollbackFor = NoLoggedInProfileException.class)
    public Profile getLoggedInProfile(Long userId, Long groupId) {
        return profileRepository.findByUserIdAndGroupId(userId, groupId).orElseThrow(NoLoggedInProfileException::new);
    }

    @Transactional(readOnly = true)
    public void checkLoggedInProfileIsGroupManager(Long userId, Long groupId) {
        Profile profile = getLoggedInProfile(userId, groupId);

        if (profile.getGroupRole() != GroupRole.MANAGER) {
            throw new UnAuthorizedException("권한이 없습니다. groupId : "
                    + groupId + ", profileId : " + profile.getId() + " , userId : " + profile.getUserId());
        }
    }

    @Transactional(readOnly = true)
    public void checkGroupSize(Group group) {
        if (profileRepository.countByGroupIdAndState(group.getId(), State.GENERAL) >= group.getGroupSize()) {
            throw new GroupFullException("최대 인원인 그룹입니다. id : " + group.getId());
        }
    }
}
