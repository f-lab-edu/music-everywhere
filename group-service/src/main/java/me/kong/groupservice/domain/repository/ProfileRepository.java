package me.kong.groupservice.domain.repository;

import me.kong.groupservice.domain.entity.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
