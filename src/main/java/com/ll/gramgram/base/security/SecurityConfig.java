package com.ll.gramgram.base.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // SecurityFilterChain 빈을 생성합니다.
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // HttpSecurity 객체를 사용하여 보안 필터 체인을 구성합니다.
        http
                .formLogin( // 폼 로그인에 대한 설정을 구성합니다.
                        formLogin -> formLogin
                                .loginPage("/member/login") // 로그인 페이지의 경로를 설정합니다.
                )
                .logout( // 로그아웃에 대한 설정을 구성합니다.
                        logout -> logout
                                .logoutUrl("/member/logout") // 로그아웃 URL을 설정합니다.
                );

        // 구성된 보안 필터 체인을 반환합니다.
        return http.build();
    }

    // PasswordEncoder 빈을 생성합니다.
    @Bean
    PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder를 사용하여 PasswordEncoder를 반환합니다.
        return new BCryptPasswordEncoder();
    }
}