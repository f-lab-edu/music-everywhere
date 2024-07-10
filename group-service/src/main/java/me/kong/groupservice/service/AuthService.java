package me.kong.groupservice.service;

import lombok.RequiredArgsConstructor;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.profile.GroupRole;
import me.kong.groupservice.domain.entity.profile.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    public boolean isGroupMember(Group group, Profile profile) {
        return profile.getGroup().getId().equals(group.getId())
                && profile.getState() == State.GENERAL;
    }

    public boolean isGroupManager(Group group, Profile profile) {
        return isGroupMember(group, profile)
                && profile.getGroupRole() == GroupRole.MANAGER;
    }
}
