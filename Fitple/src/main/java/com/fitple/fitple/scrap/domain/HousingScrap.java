package com.fitple.fitple.scrap.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class HousingScrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 외래키 없이 housingInfoId로 처리
    @Column(name = "housing_info_id")
    private Long housingInfoId;

    // 외래키 없이 userId로 처리
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "is_scrapped", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isScrapped;

}
