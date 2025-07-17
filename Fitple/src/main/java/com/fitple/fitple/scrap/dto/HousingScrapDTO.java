package com.fitple.fitple.scrap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HousingScrapDTO {

    private Long scrapId; // DTO에 추가할 필드

    private String hsmpNm;          // 단지명
    private String rnAdres;         // 도로명 주소
    private String houseTyNm;       // 주택 유형명
    private Long housingInfoId;     // housingInfoId 필드 추가

    // 추가된 필드들
    private String brtcNm;          // 광역시도 명
    private String signguNm;        // 시군구 명
    private Integer hshldCo;        // 세대 수
    private String suplyTyNm;       // 공급 유형 명
    private Double suplyPrvuseAr;   // 공급 전용 면적
    private Double suplyCmnuseAr;   // 공급 공용 면적
    private String heatMthdDetailNm; // 난방 방식
    private String elvtrInstlAtNm;  // 승강기 설치여부
    private Integer parkngCo;       // 주차수
    private Long bassRentGtn;       // 기본 임대보증금
    private Long bassMtRntchrg;     // 기본 월임대료
    private String msg;             // 메시지

    // 필요한 경우 다른 필드들도 추가할 수 있습니다.
}
