package me.kong.userservice.controller.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import me.kong.userservice.domain.entity.user.User;

@Getter
@Builder
public class UserRegisterRequestDto {
    @NotEmpty
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String name;

    @NotEmpty
    private String nickname;

    public User toEntity(String encryptedPassword) {
        return User.builder()
                .email(email)
                .password(encryptedPassword)
                .name(name)
                .nickname(nickname)
                .build();
    }
}
