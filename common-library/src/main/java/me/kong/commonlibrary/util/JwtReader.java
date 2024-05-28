package me.kong.commonlibrary.util;

import org.springframework.stereotype.Component;

@Component
public class JwtReader {
    // 인증 기능이 구현되지 않아 임의의 값을 반환
    public Long getUserId() {
        return 1L;
    }
}