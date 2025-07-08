package com.fitple.fitple.housing.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Table(name = "home_reply")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Builder
public class HousingReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "user_id", nullable = false)
    private String userId;

    @Column (name = "reply_content", nullable = false)
    private String replyContent;

    @Column(name = "property_id", nullable = false, updatable = false)
    private String propertyId;

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

    // 서비스에서 사용하는 updateContent 메소드
    public void updateContent(String newContent) {
        this.replyContent = newContent;
    }

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