package me.kong.groupservice.domain.repository;

import jakarta.persistence.LockModeType;
import me.kong.groupservice.domain.entity.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT g FROM music_group g WHERE g.id = :id")
    Optional<Group> findByIdWithLock(@Param("id") Long id);
}
