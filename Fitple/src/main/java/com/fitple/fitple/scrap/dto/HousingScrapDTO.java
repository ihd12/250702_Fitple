package com.fitple.fitple.scrap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HousingScrapDTO {
    private String hsmpNm;      // 주택명
    private String rnAdres;     // 도로명주소
    private String houseTyNm;   // 주택 유형명 ← 수정됨
}
