package com.fitple.fitple.local_price.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "lc_plan")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class LCPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="plan_id", nullable = false) // 소비 계획 id
    private Long planId;

    @Column(name = "user_id", nullable = false) // user_id 참고해서 그 id만 입력, 삭제, 수정
    private Long userId;

    @Column(name = "local_name", length = 20, nullable = false) // 지역 구분 - LCPrice(localName) 저장
    private String localName;

    @Column(name = "total_cost", nullable = false) // 총 계획 합산 비용
    private Long totalCost;

    @Column(name = "plan_title", length = 20, nullable = false) // 계획 이름
    private String planTitle;

    @Column(name = "plan_memo", length = 50) // 계획 메모, Null 가능
    private String planMemo;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}


