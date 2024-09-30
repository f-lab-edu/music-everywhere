package me.kong.commonlibrary.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class UserListRequestDto {

    private List<Long> userIds;

    public UserListRequestDto(List<Long> userIds) {
        this.userIds = userIds;
    }
}
