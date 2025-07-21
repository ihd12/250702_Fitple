package com.fitple.fitple.scrap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HousingScrapDTO {
    private Long propertyId;
    private String hsmpNm;
    private String rnAdres;
    private String houseTyNm;
    private Long housingInfoId;
    private String brtcNm;
    private String signguNm;
    private Integer hshldCo;
    private String suplyTyNm;
    private Double suplyPrvuseAr;
    private Double suplyCmnuseAr;
    private String heatMthdDetailNm;
    private String elvtrInstlAtNm;
    private Integer parkngCo;
    private Long bassRentGtn;
    private Long bassMtRntchrg;
    private String msg;
}