package me.kong.groupservice.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserNameLockDAO {

    private final JdbcTemplate jdbcTemplate;
    
    public void getLock(String key, int timeoutSeconds) {
        String sql = "SELECT GET_LOCK(?, ?)";
        jdbcTemplate.queryForList(sql, key, timeoutSeconds);
    }

    public void releaseLock(String key) {
        String sql = "SELECT RELEASE_LOCK(?)";
        jdbcTemplate.queryForList(sql, key);
    }

}
