package com.fitple.fitple.policy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class YouthPolicyResult {

    @JsonProperty("pagging")
    private Pagging pagging;

    @JsonProperty("youthPolicyList")
    private List<YouthPolicy> youthPolicyList;

    // getters & setters
    public Pagging getPagging() {
        return pagging;
    }
    public void setPagging(Pagging pagging) {
        this.pagging = pagging;
    }
    public List<YouthPolicy> getYouthPolicyList() {
        return youthPolicyList;
    }
    public void setYouthPolicyList(List<YouthPolicy> youthPolicyList) {
        this.youthPolicyList = youthPolicyList;
    }
}
