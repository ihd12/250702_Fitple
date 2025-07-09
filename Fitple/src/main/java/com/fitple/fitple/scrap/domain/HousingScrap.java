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

    // 추가된 필드들
    @Column(name = "instt_nm")
    private String insttNm; // 기관 명

    @Column(name = "hsmp_nm")
    private String hsmpNm; // 단지 명

    @Column(name = "rn_adres")
    private String rnAdres; // 도로명 주소

    @Column(name = "compet_de")
    private String competDe; // 준공 일자

    @Column(name = "hshld_co")
    private Integer hshldCo; // 세대 수

    @Column(name = "suply_ty_nm")
    private String suplyTyNm; // 공급 유형 명

    @Column(name = "style_nm")
    private String styleNm; // 형 명

    @Column(name = "suply_prvuse_ar")
    private Double suplyPrvuseAr; // 공급 전용 면적

    @Column(name = "suply_cmnuse_ar")
    private Double suplyCmnuseAr; // 공급 공용 면적

    @Column(name = "house_ty_nm")
    private String houseTyNm; // 주택 유형 명

    @Column(name = "heat_mthd_detail_nm")
    private String heatMthdDetailNm; // 난방 방식

    @Column(name = "buld_stle_nm")
    private String buldStleNm; // 건물 형태

    @Column(name = "elvtr_instl_at_nm")
    private String elvtrInstlAtNm; // 승강기 설치여부

    @Column(name = "parkng_co")
    private Integer parkngCo; // 주차수

    @Column(name = "bass_rent_gtn")
    private Long bassRentGtn; // 기본 임대보증금

    @Column(name = "bass_mt_rntchrg")
    private Long bassMtRntchrg; // 기본 월임대료

    @Column(name = "bass_cnvrs_gtn_lmt")
    private Long bassCnvrsGtnLmt; // 기본 전환보증금

    @Column(name = "msg")
    private String msg; // 메시지
}
