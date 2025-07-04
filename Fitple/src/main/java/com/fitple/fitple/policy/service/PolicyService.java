package com.fitple.fitple.policy.service;

import com.fitple.fitple.policy.dto.YouthPolicyResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
public class PolicyService {

    private final WebClient webClient;

    public PolicyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://www.youthcenter.go.kr")
                .build();
    }

    // 지역 코드(zipCd) 기반 정책 조회
    public YouthPolicyResponse getPolicies(int page, int size, String zipCd) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/go/ythip/getPlcy")
                        .queryParam("pageIndex", page)
                        .queryParam("display", size)
                        .queryParam("apiKeyNm", "13464484-9c91-4cf0-bb2a-812087e0c6df")
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
}
