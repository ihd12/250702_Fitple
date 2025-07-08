package com.fitple.fitple.housing.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "home_sigungu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
// 광역시도 및 시군구 데이터를 바탕으로 임대주택 API와 연결 및 필요시 지역 구분 가능할 수 있도록
public class HousingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long house_id;

    @Column (name = "hsmpNm", nullable = false)
    private String hsmpNm; // 단지 명 = 예시 : 서울역 센트럴자이(만리2구역)

    @Column (name = "houseTyNm", nullable = false)
    private String houseTyNm; // 주택 유형명, 예시 : 아파트

    @Column (name = "hshldCo", nullable = false)
    private long hshldCo; // 세대수

    @Column (name = "bassMtRntchrg", nullable = false)
    private long bassMtRntchrg; // 기준 월 임대료(단위 : 원)

    @Column (name = "bassRentGtn", nullable = false)
    private long bassRentGtn; // 기준 임대보증금(단위 : 원)

    @Column (name = "suplyPrvuseAr", nullable = false)
    private long suplyPrvuseAr; // 공급 전용 면적 (단위 : ㎡)

    @Column (name = "suplyCmnuseAR", nullable = false)
    private long suplyCmnuseAR; // 공급 공용 면적 (단위 : ㎡)

    @Column (name = "rnAdres", nullable = false)
    private String rnAdres; // 도로명 주소

    @Column (name = "btrcNm", nullable = false)
    private String btrcNm; // 광역시도 명칭 (예시 : 서울특별시)

    @Column (name = "signguNm", nullable = false)
    private String sigungu; // 시군구 명칭

    @Column (name = "local_code", nullable = false, updatable = false, unique = true)
    private Long localCode;

    // 상세정보용 추가

    @Column
    private String insttNm; // 기관명
    private String mngtEntrpsNm; // 관리사무소 or 관리부동산 명
    private String entrpsTel; // 사무소 or 부동산 연락처
    private String competDe; // 준공일자
    private String buldStleNm; // 건물 형태

    // 추후 기능 대비
    @Column(name = "score", nullable = false)
    private Integer score; // 추천 점수

    @Column(name = "isScrapped", nullable = false)
    private Boolean isScrapped; // 스크랩 여부
}
