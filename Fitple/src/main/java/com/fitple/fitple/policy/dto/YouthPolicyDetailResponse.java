package com.fitple.fitple.policy.dto;

import lombok.Data;
import java.util.List;

@Data
public class YouthPolicyDetailResponse {

    private DetailResult result;

    @Data
    public static class DetailResult {
        private List<YouthPolicy> youthPolicyList;
    }
}
