package me.kong.commonlibrary.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserListResponseDto {

    private Long userId;
    private String nickname;

    public UserListResponseDto(final Long userId, final String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }
}
