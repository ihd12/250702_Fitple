package com.fitple.fitple.policy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class YouthPolicyResponse {

    @JsonProperty("resultCode")
    private int resultCode;

    @JsonProperty("resultMessage")
    private String resultMessage;

    @JsonProperty("result")
    private YouthPolicyResult result;

    // getters & setters
    public int getResultCode() {
        return resultCode;
    }
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
    public String getResultMessage() {
        return resultMessage;
    }
    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }
    public YouthPolicyResult getResult() {
        return result;
    }
    public void setResult(YouthPolicyResult result) {
        this.result = result;
    }
}
