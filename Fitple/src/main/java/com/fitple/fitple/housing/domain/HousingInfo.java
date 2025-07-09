package com.fitple.fitple.housing.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "home_sigungu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Builder
public class HousingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "property_id", unique = true, nullable = false)
    private String propertyId;

    @Column(name = "brtc", nullable = false)
    private String brtc;

    @Column(name = "local_code", nullable = false)
    private Long localCode;

    @Column(name = "sigungu", nullable = false)
    private String sigungu;

    @Column(name = "signgu_nm", nullable = false)
    private String signguNm;

    @Column(name = "house_nm", nullable = false)
    private String houseNm;

    @Column(name = "pnu", nullable = false)
    private String pnu;

    @Column(name = "hsmp_nm", nullable = false)
    private String hsmpNm;

    @Column(name = "house_ty_nm", nullable = false)
    private String houseTyNm;

    @Column(name = "hshld_co", nullable = false)
    private long hshldCo;

    @Column(name = "bass_mt_rntchrg", nullable = false)
    private long bassMtRntchrg;

    @Column(name = "bass_rent_gtn", nullable = false)
    private long bassRentGtn;

    @Column(name = "suply_prvuse_ar", nullable = false)
    private long suplyPrvuseAr;

    @Column(name = "suply_cmnusear", nullable = false)
    private long suplyCmnuseAr;

    @Column(name = "rn_adres", nullable = false)
    private String rnAdres;

    @Column(name = "btrc_nm", nullable = false)
    private String btrcNm;

    // --- Nullable 필드들 ---
    @Column(name = "compet_de")
    private String competDe;

    @Column(name = "entrps_tel")
    private String entrpsTel;

    @Column(name = "instt_nm")
    private String insttNm;

    @Column(name = "mngt_entrps_nm")
    private String mngtEntrpsNm;

    @Column(name = "buld_stle_nm")
    private String buldStleNm;

    @Column(name = "elvt_instl_at")
    private String elvtInstlAt;

    @Column(name = "parking_co")
    private String parkingCo;

    @Column(name = "score")
    private Integer score;

    @Column(name = "is_scrapped")
    private Boolean isScrapped;

}