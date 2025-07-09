package com.fitple.fitple.base.user.repository;

import com.fitple.fitple.base.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void createUserData(){
        for(int i=0;i<100;i++){
            User user = User.builder()
                    .email("test"+i+"@email.com")
                    .nickname("testName"+i)
                    .password(passwordEncoder.encode("1234"))
                    .auth("USER")
                    .build();
            userRepository.save(user);
        }
    }

}