package me.kong.groupservice.domain.repository;

import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserIdAndGroupId(Long userId, Long groupId);

    Integer countByGroupIdAndState(Long groupId, State state);
}
