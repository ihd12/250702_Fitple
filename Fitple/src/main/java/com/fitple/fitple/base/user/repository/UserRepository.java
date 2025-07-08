package com.fitple.fitple.base.user.repository;

import com.fitple.fitple.base.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);  // 로그인용
    // findByUsername을 제거하거나 수정
}
