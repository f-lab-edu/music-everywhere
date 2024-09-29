package me.kong.groupservice.common.config;


import jakarta.servlet.http.HttpServletRequest;
import me.kong.commonlibrary.util.JwtReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class UtilConfig {

    @Bean
    @RequestScope
    public JwtReader jwtReader(HttpServletRequest request) {
        return new JwtReader(request);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
