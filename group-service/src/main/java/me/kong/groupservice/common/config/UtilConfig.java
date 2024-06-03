package me.kong.groupservice.common.config;


import me.kong.commonlibrary.util.JwtReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfig {

    @Bean
    public JwtReader jwtReader() {
        return new JwtReader();
    }
}
