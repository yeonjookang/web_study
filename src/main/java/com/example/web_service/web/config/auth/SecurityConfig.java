package com.example.web_service.web.config.auth;

import com.example.web_service.web.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests((authz->authz
                        .anyRequest().authenticated()
                        .requestMatchers("/","/css/**","/images/**",
                                "/js/**","h2-console/**").permitAll()
                        .requestMatchers("/api/v1/**").hasRole(Role.USER.name())
                        .anyRequest().authenticated()));
        http
                .logout(logout->logout.logoutSuccessUrl("/"));
        http
                .oauth2Login(oauth2Login-> oauth2Login.userInfoEndpoint( //로그인 성공 이후 사용자의 정보를 가져올 때의 설정 담당
                        userInfoEndPoint->userInfoEndPoint.userService(customOAuth2UserService) // 소셜 서비스에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시
                        ));

        return http.build();
    }
}
