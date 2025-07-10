package com.fitple.fitple.common.config;

import com.fitple.fitple.base.user.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // 모든 요청에 대해 접근 허용 (추후 수정 가능)
                )

                .formLogin(form -> form
                        .loginPage("/user/login")                // 로그인 페이지 경로
                        .usernameParameter("email")              // 이메일로 로그인
                        .passwordParameter("password")
                        .defaultSuccessUrl("/mypage", true)      // 로그인 성공 후 이동할 페이지
                        .permitAll()                             // 로그인 페이지는 누구나 접근 가능
                )

                .logout(logout -> logout
                        .logoutUrl("/user/logout")              // 로그아웃 경로
                        .logoutSuccessUrl("/")                  // 로그아웃 성공 후 리디렉션할 경로
                        .permitAll()
                )

                .userDetailsService(customUserDetailsService);  // CustomUserDetailsService 등록

        return http.build();
    }
}

