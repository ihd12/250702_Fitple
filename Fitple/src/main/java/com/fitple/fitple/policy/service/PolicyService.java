package com.fitple.fitple.policy.service;

import com.fitple.fitple.policy.dto.YouthPolicyResponse;
import com.fitple.fitple.policy.dto.YouthPolicyDetailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
public class PolicyService {

    private final WebClient webClient;

    @Value("${youth.api.key}")
    private String apiKey;

    public PolicyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://www.youthcenter.go.kr")
                .build();
    }

    public YouthPolicyResponse getPolicies(int page, int size, String zipCd) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/go/ythip/getPlcy")
                        .queryParam("pageIndex", page)
                        .queryParam("display", size)
                        .queryParam("apiKeyNm", apiKey)
                        .queryParam("rtnType", "json")
                        .queryParamIfPresent("zipCd",
                                (zipCd == null || zipCd.isBlank())
                                        ? Optional.empty()
                                        : Optional.of(zipCd))
                        .build())
                .retrieve()
                .bodyToMono(YouthPolicyResponse.class)
                .block();
    }

    public YouthPolicyDetailResponse getPolicyDetail(String plcyNo) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/go/ythip/getPlcy")
                        .queryParam("apiKeyNm", apiKey)
                        .queryParam("rtnType", "json")
                        .queryParam("bizId", plcyNo)
                        .build())
                .retrieve()
                .bodyToMono(YouthPolicyDetailResponse.class)
                .block();
    }
}
