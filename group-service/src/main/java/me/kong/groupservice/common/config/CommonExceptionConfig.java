package me.kong.groupservice.common.config;

import me.kong.commonlibrary.exception.advice.AuthExceptionAdvice;
import me.kong.commonlibrary.exception.advice.CommonExceptionAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonExceptionConfig {

    @Bean
    public AuthExceptionAdvice authExceptionAdvice() {
        return new AuthExceptionAdvice();
    }

    @Bean
    public CommonExceptionAdvice commonExceptionAdvice() {
        return new CommonExceptionAdvice();
    }

}
