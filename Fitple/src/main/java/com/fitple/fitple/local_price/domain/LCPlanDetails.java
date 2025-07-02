package com.fitple.fitple.local_price.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "lc_plan_details")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class LCPlanDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long detaislId; // 🔑

    @Column(name= "plan_no", nullable = false)
    private Integer planNo; // 특정 플랜 기준으로 열람 및 플랜 삭제시 삭제 연동

    @Column(name = "item_name", length = 50, nullable = false) // LCPrice 혹은 LCCumstom 제목 가져옴
    private String itemName;

    @Column(name = "item_price")
    private Integer itemPrice;

    @Column(name = "item_quant")
    private Integer itemQuant;

    @Column(name = "total_item_count")
    private Integer totalItemCount;
}
