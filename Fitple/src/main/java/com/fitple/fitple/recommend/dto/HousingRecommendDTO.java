package com.fitple.fitple.recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HousingRecommendDTO {

    private Long housingInfoId;  // HousingScrap.housingInfoId
    private String hsmpNm;       // HousingScrap.hsmpNm (단지명)
    private String rnAdres;      // HousingScrap.rnAdres (도로명 주소)
    private Long bassRentGtn;    // HousingScrap.bassRentGtn (보증금)
    private Long bassMtRntchrg;  // HousingScrap.bassMtRntchrg (월세)
    private int score;           // 추천 점수 (20점 만점)
}
