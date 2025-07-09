package com.fitple.fitple.scrap.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class HousingScrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // HousingInfo 객체 대신 housing_info_id를 저장
    @Column(name = "housing_info_id", nullable = false)
    private Long housingInfoId;  // housing_info_id만 저장 (외래키 제약 없음)

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "is_scrapped", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isScrapped;

    // 요청하신 필드들
    @Column(name = "hsmp_sn", nullable = false)
    private Long hsmpSn; // 단지 식별자

    @Column(name = "brtc_code", nullable = false)
    private String brtcCode; // 광역시도 코드

    @Column(name = "signgu_code", nullable = false)
    private String signguCode; // 시군구 코드

    @Column(name = "brtc_nm", nullable = false)
    private String brtcNm; // 광역시도 명

    @Column(name = "signgu_nm", nullable = false)
    private String signguNm; // 시군구 명
}
