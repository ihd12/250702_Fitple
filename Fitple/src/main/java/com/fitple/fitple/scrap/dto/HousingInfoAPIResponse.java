package com.fitple.fitple.scrap.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)  // 응답에서 정의되지 않은 필드를 무시
public class HousingInfoAPIResponse {

    // 필수 항목
    private String code;       // 메시지 코드
    private Integer numOfRows; // 페이지당 데이터 개수
    private Integer pageNo;    // 페이지 번호
    private Integer totalCount; // 전체 결과수
    private String hsmpSn;     // 단지 식별자
    private String insttNm;    // 기관 명
    private String brtcCode;   // 광역시도 코드
    private String brtcNm;     // 광역시도 명
    private String signguCode; // 시군구 코드
    private String signguNm;   // 시군구 명
    private String hsmpNm;     // 단지 명
    private String rnAdres;    // 도로명 주소
    private String pnu;        // pnu
    private String competDe;   // 준공 일자
    private Integer hshldCo;   // 세대 수
    private String suplyTyNm;  // 공급 유형 명
    private String styleNm;    // 형 명
    private Double suplyPrvuseAr; // 공급 전용 면적
    private Double suplyCmnuseAr; // 공급 공용 면적
    private String houseTyNm; // 주택 유형 명
    private String heatMthdDetailNm; // 난방 방식
    private String buldStleNm; // 건물 형태
    private String elvtrInstlAtNm; // 승강기 설치여부
    private Integer parkngCo;  // 주차수
    private Long bassRentGtn;  // 기본 임대보증금
    private Long bassMtRntchrg; // 기본 월임대료
    private Long bassCnvrsGtnLmt; // 기본 전환보증금
    private String msg;        // 메시지

    // data 필드 추가 - 실제 API 응답에 맞춰 변경
    private List<HousingData> hsmpList;  // hsmpList로 수정

    // HousingData 클래스를 내부 클래스로 정의하여 hsmpList 필드에 맞게 매핑
    @Getter
    @Setter
    @NoArgsConstructor
    public static class HousingData {
        private String hsmpNm; // 단지명
        private String rnAdres; // 도로명 주소
        private String houseTyNm; // 주택유형
        private Long bassRentGtn; // 기본 임대보증금
        private Long bassMtRntchrg; // 기본 월임대료
    }
}
