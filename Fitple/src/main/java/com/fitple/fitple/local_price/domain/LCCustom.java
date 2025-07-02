package com.fitple.fitple.local_price.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "lc_custom")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class LCCustom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="custom_price_id", nullable = false) // 커스텀 항목 전용 id
    private Long customPriceId;

    @Column(name = "user_id", nullable = false) // user_id 참고해서 그 id만 사용 가능
    private Long userId;

    @Column(name = "custom_name", length = 50, nullable = false) // VARCHAR(50)
    private String customName;

    @Column(name = "custom_price", nullable = false) // 커스텀 항목 가격
    private Integer customPrice;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}


