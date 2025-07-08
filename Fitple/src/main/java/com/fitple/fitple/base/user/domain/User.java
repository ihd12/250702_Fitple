package com.fitple.fitple.base.user.domain;

import com.fitple.fitple.base.user.dto.UserDTO;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 50)
    private String nickname;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // UserDTO로부터 값을 받아서 User 객체 생성
    public User(UserDTO dto) {
        this.email = dto.getEmail();
        this.password = dto.getPassword();  // 비밀번호는 암호화 없이 저장하기 전에 설정할 수 있습니다
        this.nickname = dto.getNickname();
        this.createdAt = LocalDateTime.now();  // 기본 생성일자
        this.updatedAt = LocalDateTime.now();  // 기본 수정일자
    }
}
