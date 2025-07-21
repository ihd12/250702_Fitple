package com.fitple.fitple.common.config;

import com.fitple.fitple.base.user.security.CustomUserDetails;
import com.fitple.fitple.base.user.security.CustomUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    // 비밀번호 암호화를 위한 PasswordEncoder 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 인증 매니저 빈 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 보안 설정의 핵심: SecurityFilterChain 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (개발용)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN") // /admin 경로는 ADMIN만 접근 가능
                        .anyRequest().permitAll() // 그 외 모든 요청은 허용
                )

                .formLogin(form -> form
                        .loginPage("/user/login")              // 사용자 정의 로그인 페이지
                        .usernameParameter("email")            // 로그인 파라미터: email
                        .passwordParameter("password")         // 로그인 파라미터: password
                        .successHandler(new AuthenticationSuccessHandler() {
                            // 로그인 성공 시 사용자 권한에 따라 리다이렉트 처리
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                                Authentication authentication) throws IOException {
                                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                                String authority = userDetails.getUser().getAuth(); // 예: "ADMIN" 또는 "USER"

                                if ("ADMIN".equalsIgnoreCase(authority)) {
                                    response.sendRedirect("/"); // 관리자 → 회원 목록
                                } else {
                                    response.sendRedirect("/"); // 일반 사용자 → 인덱스 페이지
                                }
                            }
                        })
                        .failureHandler(new AuthenticationFailureHandler() {
                            // 로그인 실패 시 동작 정의
                            @Override
                            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                                                AuthenticationException exception) throws IOException, ServletException {
                                // 로그인 실패 시 에러 메시지 세션에 저장하고 로그인 페이지로 리디렉트
                                request.getSession().setAttribute("loginError", "이메일 또는 비밀번호가 올바르지 않습니다.");
                                response.sendRedirect("/user/login?error=true");
                            }
                        })
                        .permitAll() // 로그인 페이지 접근 허용
                )

                .logout(logout -> logout
                        .logoutUrl("/user/logout")        // 로그아웃 URL
                        .logoutSuccessUrl("/")            // 로그아웃 성공 시 이동할 URL
                        .permitAll()
                )

                .userDetailsService(customUserDetailsService); // 사용자 인증 서비스 등록

        return http.build();
    }
}
