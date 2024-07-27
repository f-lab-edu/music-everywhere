package me.kong.groupservice.domain.repository;

import me.kong.groupservice.domain.entity.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    /**
     * 동시성 문제가 발생 가능
     * 해당 메소드 사용 전 Locking에 대한 고려가 선행되어야 함
     * 자체 구현한 @RedisLock 어노테이션 존재
     */
    @Modifying
    @Query("update Group g set g.groupSize = :groupSize where g.id = :id")
    void updateGroupSize(@Param("id") Long id, @Param("groupSize") Integer groupSize);
}
