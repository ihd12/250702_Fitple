package com.fitple.fitple.recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HousingTestDTO {
    private String uid;      // 단지명
    private String area;     // 주소
    private int rent;        // 월임대료
    private int deposit;     // 보증금
    private int score;       // 계산된 점수
}
