package com.fitple.fitple.housing.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Table(name = "home_reply")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class HousingReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "user_id", nullable = false)
    private String UserId;

    @Column (name = "reply_content", nullable = false)
    private String ReplyContent;

    @Column (name = "pnu", nullable = false, updatable = false)
    private Long pnu;

    // 관리 기능: 논리적 삭제 (Soft Delete)
    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted = false;

    // 상호작용 기능: 좋아요 수
    @Column(name = "like_count", nullable = false)
    @ColumnDefault("0")
    private int likeCount = 0;

    @CreationTimestamp // JPA가 생성 시각을 자동 주입
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // JPA가 수정 시각을 자동 주입
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //== 편의 메서드 ==//
    // 논리적 삭제 - 데이터 자체가 아닌 삭제된 것처럼 보이게 해줌
    public void markAsDeleted() {
        this.isDeleted = true;
    }

    // 좋아요 추가
    public void addLike() {
        this.likeCount++;
    }

    // 좋아요 제거
    public void removeLike() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}