package com.fitple.fitple.admin.init;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.base.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAccountInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        String adminEmail = "admin@fitple.com";

        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode("1234")) // 초기 비번
                    .nickname("관리자")
                    .auth("ADMIN")
                    .build();

            userRepository.save(admin);
            System.out.println("관리자 계정 생성: " + adminEmail);
        } else {
            System.out.println("관리자 계정 이미 존재함");
        }
    }
}
