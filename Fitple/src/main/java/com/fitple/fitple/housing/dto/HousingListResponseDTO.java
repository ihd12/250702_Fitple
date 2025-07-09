package com.fitple.fitple.housing.dto;

import com.fitple.fitple.housing.domain.HousingInfo;
import lombok.Getter;

@Getter
public class HousingListResponseDTO {

    private final Long houseId;
    private final String propertyName; // 단지명 (hsmpNm)
    private final String propertyType; // 주택 유형명 (houseTyNm)
    private final String address;      // 도로명 주소 (rnAdres)
    private final long monthlyRent;    // 기준 월 임대료 (bassMtRntchrg)

    private final double averageRating;
    private final long ratingCount;

    public HousingListResponseDTO(HousingInfo housingInfo, HousingRatingResponseDTO ratingInfo) {
        this.houseId = housingInfo.getId();
        this.propertyName = housingInfo.getHsmpNm();
        this.propertyType = housingInfo.getHouseTyNm();
        this.address = housingInfo.getRnAdres();
        this.monthlyRent = housingInfo.getBassMtRntchrg();

        this.averageRating = ratingInfo.getAverageRating();
        this.ratingCount = ratingInfo.getRatingCount();
    }
}