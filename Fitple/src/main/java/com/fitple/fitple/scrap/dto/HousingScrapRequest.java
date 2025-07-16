package com.fitple.fitple.scrap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HousingScrapRequest {

    // ▼▼▼▼▼ 추가된 필수 필드 ▼▼▼▼▼
    @JsonProperty("property_id")
    private Long propertyId;

    @JsonProperty("housing_info_id")
    private Long housingInfoId;

    @JsonProperty("userId")
    private String userId;

    private boolean isScrapped;
    // ▲▲▲▲▲ 추가된 필수 필드 ▲▲▲▲▲


    // --- 기존 필드 ---
    private Long hsmpSn;
    private String brtcCode;
    private String signguCode;
    private String brtcNm;
    private String signguNm;
    private String insttNm;
    private String hsmpNm;
    private String rnAdres;
    private String competDe;
    private Integer hshldCo;
    private String suplyTyNm;
    private String styleNm;
    private Double suplyPrvuseAr;
    private Double suplyCmnuseAr;
    private String houseTyNm;
    private String heatMthdDetailNm;
    private String buldStleNm;
    private String elvtrInstlAtNm;
    private Integer parkngCo;
    private Long bassRentGtn;
    private Long bassMtRntchrg;
    private Long bassCnvrsGtnLmt;
    private String msg;
}