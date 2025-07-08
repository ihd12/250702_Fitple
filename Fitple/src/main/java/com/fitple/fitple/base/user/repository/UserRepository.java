package com.fitple.fitple.base.user.repository;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.base.user.repository.dsl.UserDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserDslRepository {
    Optional<User> findByEmail(String email);  // 로그인용

}
