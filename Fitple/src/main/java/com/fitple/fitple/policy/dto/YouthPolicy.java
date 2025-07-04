package com.fitple.fitple.policy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    // Getters and Setters

    public String getPlcyNo() {
        return plcyNo;
    }

    public void setPlcyNo(String plcyNo) {
        this.plcyNo = plcyNo;
    }

    public String getPlcyNm() {
        return plcyNm;
    }

    public void setPlcyNm(String plcyNm) {
        this.plcyNm = plcyNm;
    }

    public String getPlcyKywdNm() {
        return plcyKywdNm;
    }

    public void setPlcyKywdNm(String plcyKywdNm) {
        this.plcyKywdNm = plcyKywdNm;
    }

    public String getPlcyExplnCn() {
        return plcyExplnCn;
    }

    public void setPlcyExplnCn(String plcyExplnCn) {
        this.plcyExplnCn = plcyExplnCn;
    }

    public String getLclsfNm() {
        return lclsfNm;
    }

    public void setLclsfNm(String lclsfNm) {
        this.lclsfNm = lclsfNm;
    }

    public String getMclsfNm() {
        return mclsfNm;
    }

    public void setMclsfNm(String mclsfNm) {
        this.mclsfNm = mclsfNm;
    }

    public String getPlcySprtCn() {
        return plcySprtCn;
    }

    public void setPlcySprtCn(String plcySprtCn) {
        this.plcySprtCn = plcySprtCn;
    }

    public String getSprvsnInstCdNm() {
        return sprvsnInstCdNm;
    }

    public void setSprvsnInstCdNm(String sprvsnInstCdNm) {
        this.sprvsnInstCdNm = sprvsnInstCdNm;
    }

    public String getAplyPrdSeCd() {
        return aplyPrdSeCd;
    }

    public void setAplyPrdSeCd(String aplyPrdSeCd) {
        this.aplyPrdSeCd = aplyPrdSeCd;
    }

    public String getBizPrdSeCd() {
        return bizPrdSeCd;
    }

    public void setBizPrdSeCd(String bizPrdSeCd) {
        this.bizPrdSeCd = bizPrdSeCd;
    }

    public String getBizPrdBgngYmd() {
        return bizPrdBgngYmd;
    }

    public void setBizPrdBgngYmd(String bizPrdBgngYmd) {
        this.bizPrdBgngYmd = bizPrdBgngYmd;
    }

    public String getBizPrdEndYmd() {
        return bizPrdEndYmd;
    }

    public void setBizPrdEndYmd(String bizPrdEndYmd) {
        this.bizPrdEndYmd = bizPrdEndYmd;
    }

    public String getEarnMinAmt() {
        return earnMinAmt;
    }

    public void setEarnMinAmt(String earnMinAmt) {
        this.earnMinAmt = earnMinAmt;
    }

    public String getEarnMaxAmt() {
        return earnMaxAmt;
    }

    public void setEarnMaxAmt(String earnMaxAmt) {
        this.earnMaxAmt = earnMaxAmt;
    }

    public String getRgtrInstCdNm() {
        return rgtrInstCdNm;
    }

    public void setRgtrInstCdNm(String rgtrInstCdNm) {
        this.rgtrInstCdNm = rgtrInstCdNm;
    }

    public String getZipCd() {
        return zipCd;
    }

    public void setZipCd(String zipCd) {
        this.zipCd = zipCd;
    }

    public String getAplyYmd() {
        return aplyYmd;
    }

    public void setAplyYmd(String aplyYmd) {
        this.aplyYmd = aplyYmd;
    }
}
