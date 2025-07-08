package com.fitple.fitple.housing.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Table(name = "home_rating")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Builder
public class HousingRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "property_id", nullable = false, updatable = false)
    private String propertyId;

    @Column(name = "rating_score", nullable = false)
    private Integer ratingScore;

    // 점수 수정 메서드 //
    public void updateScore(Integer newScore) {
        this.ratingScore = newScore;
    }
}