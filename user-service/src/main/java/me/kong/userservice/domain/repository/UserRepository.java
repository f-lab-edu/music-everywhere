package me.kong.userservice.domain.repository;

import me.kong.userservice.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);

    @Query("SELECT u FROM users u WHERE u.id IN :ids")
    List<User> findByIds(List<Long> ids);
}
