package com.fitple.fitple.housing.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "home_sigungu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class HousingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "house_id")  // DB 컬럼명 그대로 유지
    private Long houseId;  // 자바 필드명은 houseId로 변경

    @Column(name = "hsmpNm", nullable = false)
    private String hsmpNm; // 단지 명

    @Column(name = "houseTyNm", nullable = false)
    private String houseTyNm; // 주택 유형명

    @Column(name = "hshldCo", nullable = false)
    private long hshldCo; // 세대수

    @Column(name = "bassMtRntchrg", nullable = false)
    private long bassMtRntchrg; // 기준 월 임대료

    @Column(name = "bassRentGtn", nullable = false)
    private long bassRentGtn; // 기준 임대보증금

    @Column(name = "suplyPrvuseAr", nullable = false)
    private long suplyPrvuseAr; // 공급 전용 면적

    @Column(name = "suplyCmnuseAR", nullable = false)
    private long suplyCmnuseAR; // 공급 공용 면적

    @Column(name = "rnAdres", nullable = false)
    private String rnAdres; // 도로명 주소

    @Column(name = "btrcNm", nullable = false)
    private String btrcNm; // 광역시도 명칭

    @Column(name = "signguNm", nullable = false)
    private String sigungu; // 시군구 명칭

    @Column(name = "local_code", nullable = false, updatable = false, unique = true)
    private Long localCode;

    @Column
    private String insttNm; // 기관명
    private String mngtEntrpsNm; // 관리사무소 or 관리부동산 명
    private String entrpsTel; // 연락처
    private String competDe; // 준공일자
    private String buldStleNm; // 건물 형태
    private String elvtrInstlAtNm; // 승강기 설치 여부
    private String parkngCo; // 주차 가능 수

    @Column(name = "score", nullable = false)
    private Integer score; // 추천 점수

    @Column(name = "isScrapped", nullable = false)
    private Boolean isScrapped; // 스크랩 여부

    // 매물 정보를 받아오는 생성자 추가
    public HousingInfo(Long houseId) {
        this.houseId = houseId;
    }
}
