package me.kong.commonlibrary.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtReader {

    private final HttpServletRequest request;

    public Long getUserId() {
        String userIdHeader = request.getHeader("userid");
        if (userIdHeader != null) {
            try {
                return Long.parseLong(userIdHeader);
            } catch (NumberFormatException e) {
                return 1L;
            }
        }
        return 1L;
    }
}