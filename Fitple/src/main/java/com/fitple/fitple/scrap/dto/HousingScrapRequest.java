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
    private Long hsmpSn;                // 단지 식별자
    private String brtcCode;            // 광역시도 코드
    private String signguCode;          // 시군구 코드
    private String brtcNm;              // 광역시도 명
    private String signguNm;            // 시군구 명

    // 추가된 필드들
    private String insttNm;             // 기관 명
    private String hsmpNm;              // 단지 명
    private String rnAdres;             // 도로명 주소
    private String competDe;            // 준공 일자
    private Integer hshldCo;            // 세대 수
    private String suplyTyNm;           // 공급 유형 명
    private String styleNm;             // 형 명
    private Double suplyPrvuseAr;       // 공급 전용 면적
    private Double suplyCmnuseAr;       // 공급 공용 면적
    private String houseTyNm;           // 주택 유형 명
    private String heatMthdDetailNm;    // 난방 방식
    private String buldStleNm;          // 건물 형태
    private String elvtrInstlAtNm;      // 승강기 설치여부
    private Integer parkngCo;           // 주차수
    private Long bassRentGtn;           // 기본 임대보증금
    private Long bassMtRntchrg;         // 기본 월임대료
    private Long bassCnvrsGtnLmt;       // 기본 전환보증금
    private String msg;                 // 메시지
}
