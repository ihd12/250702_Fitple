package com.fitple.fitple.scrap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HousingScrapRequest {
    private Long hsmpSn;
    private String brtcCode;
    private String signguCode;
    private String brtcNm;
    private String signguNm;
}
