package com.fitple.fitple.scrap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HousingScrapDTO {

    private String hsmpNm;  // 단지명
    private String rnAdres; // 도로명 주소
    private String houseTyNm; // 주택 유형명
    private Long housingInfoId; // housingInfoId 필드 추가

    // 필요한 경우 다른 필드들도 추가할 수 있습니다.
}
