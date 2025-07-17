package com.fitple.fitple.scrap.domain;

import com.fitple.fitple.base.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "housing_scrap",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_user_property", columnNames = {"user_id", "property_id"})
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HousingScrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "property_id", nullable = false)
    private Long propertyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "housing_info_id", nullable = false)
    private Long housingInfoId;

    @Column(name = "is_scrapped", nullable = false)
    private boolean isScrapped;

    @Column(name = "hsmp_sn", nullable = false)
    private Long hsmpSn;

    @Column(name = "brtc_code", nullable = false)
    private String brtcCode;

    @Column(name = "signgu_code", nullable = false)
    private String signguCode;

    @Column(name = "brtc_nm", nullable = false)
    private String brtcNm;

    @Column(name = "signgu_nm", nullable = false)
    private String signguNm;

    @Column(name = "instt_nm")
    private String insttNm;

    @Column(name = "hsmp_nm")
    private String hsmpNm;

    @Column(name = "rn_adres")
    private String rnAdres;

    @Column(name = "compet_de")
    private String competDe;

    @Column(name = "hshld_co")
    private Integer hshldCo;

    @Column(name = "suply_ty_nm")
    private String suplyTyNm;

    @Column(name = "style_nm")
    private String styleNm;

    @Column(name = "suply_prvuse_ar")
    private Double suplyPrvuseAr;

    @Column(name = "suply_cmnuse_ar")
    private Double suplyCmnuseAr;

    @Column(name = "house_ty_nm")
    private String houseTyNm;

    @Column(name = "heat_mthd_detail_nm")
    private String heatMthdDetailNm;

    @Column(name = "buld_stle_nm")
    private String buldStleNm;

    @Column(name = "elvtr_instl_at_nm")
    private String elvtrInstlAtNm;

    @Column(name = "parkng_co")
    private Integer parkngCo;

    @Column(name = "bass_rent_gtn")
    private Long bassRentGtn;

    @Column(name = "bass_mt_rntchrg")
    private Long bassMtRntchrg;

    @Column(name = "bass_cnvrs_gtn_lmt")
    private Long bassCnvrsGtnLmt;

    @Column(name = "msg")
    private String msg;
}
