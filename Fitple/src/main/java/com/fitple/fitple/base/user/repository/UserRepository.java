package com.fitple.fitple.base.user.repository;

import com.fitple.fitple.base.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);  // 로그인용

    @Query("SELECT u FROM User u WHERE u.email LIKE %:keyword% OR u.nickname LIKE %:keyword%")
    Page<User> searchUser(@Param("keyword") String keyword, Pageable pageable);

    void deleteByEmail(String email);
}
