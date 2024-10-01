package me.kong.groupservice.client.circuitbreaker;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import me.kong.commonlibrary.event.dto.UserListRequestDto;
import me.kong.commonlibrary.event.dto.UserListResponseDto;
import me.kong.groupservice.client.UserServiceClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserServiceCircuitBreaker {

    private final UserServiceClient userServiceClient;

    @CircuitBreaker(name = "USER_SERVICE_CIRCUIT_BREAKER", fallbackMethod = "getUserInfoFallback")
    public List<UserListResponseDto> getUserInfo(UserListRequestDto dto) {
        return userServiceClient.getUserInfo(dto);
    }

    public List<UserListResponseDto> getUserInfoFallback(UserListRequestDto requestDto, Throwable throwable) {
        return new ArrayList<>();
    }
}
