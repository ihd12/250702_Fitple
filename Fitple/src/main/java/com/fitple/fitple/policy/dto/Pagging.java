package com.fitple.fitple.policy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Pagging {

    @JsonProperty("totCount")
    private int totCount;

    @JsonProperty("pageNum")
    private int pageNum;

    @JsonProperty("pageSize")
    private int pageSize;

    // getters & setters
    public int getTotCount() {
        return totCount;
    }
    public void setTotCount(int totCount) {
        this.totCount = totCount;
    }
    public int getPageNum() {
        return pageNum;
    }
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
