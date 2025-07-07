package com.fitple.fitple.policy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class YouthPolicy {

    @JsonProperty("plcyNo")
    private String plcyNo;

    @JsonProperty("plcyNm")
    private String plcyNm;

    @JsonProperty("plcyKywdNm")
    private String plcyKywdNm;

    @JsonProperty("plcyExplnCn")
    private String plcyExplnCn;

    @JsonProperty("lclsfNm")
    private String lclsfNm;

    @JsonProperty("mclsfNm")
    private String mclsfNm;

    @JsonProperty("plcySprtCn")
    private String plcySprtCn;

    @JsonProperty("sprvsnInstCdNm")
    private String sprvsnInstCdNm;

    @JsonProperty("aplyPrdSeCd")
    private String aplyPrdSeCd;

    @JsonProperty("bizPrdSeCd")
    private String bizPrdSeCd;

    @JsonProperty("bizPrdBgngYmd")
    private String bizPrdBgngYmd;

    @JsonProperty("bizPrdEndYmd")
    private String bizPrdEndYmd;

    @JsonProperty("earnMinAmt")
    private String earnMinAmt;

    @JsonProperty("earnMaxAmt")
    private String earnMaxAmt;

    @JsonProperty("rgtrInstCdNm")
    private String rgtrInstCdNm;

    @JsonProperty("zipCd")
    private String zipCd;

    @JsonProperty("aplyYmd")
    private String aplyYmd;

    @JsonProperty("plcyAplyMthdCn")
    private String plcyAplyMthdCn;

    @JsonProperty("srngMthdCn")
    private String srngMthdCn;

    @JsonProperty("sbmsnDcmntCn")
    private String sbmsnDcmntCn;

    @JsonProperty("refUrlAddr1")
    private String refUrlAddr1;
}
