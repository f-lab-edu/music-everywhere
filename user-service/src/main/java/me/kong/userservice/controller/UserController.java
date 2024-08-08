package me.kong.userservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.kong.userservice.controller.dto.UserRegisterRequestDto;
import me.kong.userservice.controller.dto.UserResponseDto;
import me.kong.userservice.domain.entity.user.User;
import me.kong.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static me.kong.commonlibrary.constant.HttpStatusResponseEntity.RESPONSE_OK;

@RestController
@RequestMapping("/user-service/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid UserRegisterRequestDto dto) {
        userService.addNewUser(dto);
        return RESPONSE_OK;
    }

    @GetMapping("/check-email")
    public ResponseEntity<HttpStatus> checkUniqueEmail(@RequestParam(name = "email") String email) {
        userService.checkUniqueEmail(email);
        return RESPONSE_OK;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserInfo(@PathVariable Long userId) {
        User user = userService.findUserById(userId);

        return ResponseEntity.ok(UserResponseDto.of(user));
    }
}
