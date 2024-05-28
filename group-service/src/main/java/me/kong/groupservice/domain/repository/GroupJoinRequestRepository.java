package me.kong.groupservice.domain.repository;

import me.kong.groupservice.domain.entity.GroupJoinRequest.GroupJoinRequest;
import me.kong.groupservice.domain.entity.GroupJoinRequest.JoinResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface GroupJoinRequestRepository extends JpaRepository<GroupJoinRequest, Long> {

    default boolean pendingRequestExists(Long userId, Long groupId) {
        return findByResponseAndUserIdAndGroupId(JoinResponse.PENDING, userId, groupId).isPresent();
    }

    Optional<GroupJoinRequest> findByResponseAndUserIdAndGroupId(JoinResponse response, Long userId, Long groupId);
}
