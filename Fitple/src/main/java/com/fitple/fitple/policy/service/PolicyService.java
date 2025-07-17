package com.fitple.fitple.policy.service;

import com.fitple.fitple.policy.dto.YouthPolicyResponse;
import com.fitple.fitple.policy.dto.YouthPolicyDetailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;
import java.util.Map;
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
                        .queryParam("pageNum", page)
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
                        .queryParam("plcyNo", plcyNo)  // 수정
                        .build())
                .retrieve()
                .bodyToMono(YouthPolicyDetailResponse.class)
                .block();
    }

    public Map<String, String> getZipCodeMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("11000", "서울특별시");
        map.put("26000", "부산광역시");
        map.put("27000", "대구광역시");
        map.put("28000", "인천광역시");
        map.put("29000", "광주광역시");
        map.put("30000", "대전광역시");
        map.put("31000", "울산광역시");
        map.put("36000", "세종특별자치시");
        map.put("41000", "경기도");
        map.put("42000", "강원특별자치도");
        map.put("43000", "충청북도");
        map.put("44000", "충청남도");
        map.put("45000", "전라북도");
        map.put("46000", "전라남도");
        map.put("47000", "경상북도");
        map.put("48000", "경상남도");
        map.put("50000", "제주특별자치도");
        return map;
    }


}
