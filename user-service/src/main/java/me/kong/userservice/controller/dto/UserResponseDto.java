package me.kong.userservice.controller.dto;

import lombok.Builder;
import lombok.Getter;
import me.kong.userservice.domain.entity.user.Role;
import me.kong.userservice.domain.entity.user.User;

@Getter
@Builder
public class UserResponseDto {
    private Long userId;
    private String email;
    private String name;
    private String nickname;
    private Role role;

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .role(user.getRole())
                .build();
    }
}
