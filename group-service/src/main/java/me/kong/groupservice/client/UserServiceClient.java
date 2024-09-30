package me.kong.groupservice.client;

import me.kong.commonlibrary.event.dto.UserListRequestDto;
import me.kong.commonlibrary.event.dto.UserListResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @PostMapping("/api/users/list")
    List<UserListResponseDto> getUserInfo(@RequestBody UserListRequestDto dto);

}
