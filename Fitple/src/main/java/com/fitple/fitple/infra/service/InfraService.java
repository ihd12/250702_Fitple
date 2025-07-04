package com.fitple.fitple.infra.service;


import com.fitple.fitple.infra.dto.InfraDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InfraService {

    @Value("${kakao.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<InfraDTO> searchPlaces(String category, double x, double y) {
        List<InfraDTO> places = new ArrayList<>();

        for (int page = 1; page <= 3; page++) {
            String url = "https://dapi.kakao.com/v2/local/search/category.json"
                    + "?category_group_code=" + category
                    + "&x=" + x
                    + "&y=" + y
                    + "&radius=1000"
                    + "&page=" + page;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + apiKey);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            List<Map<String, Object>> documents = (List<Map<String, Object>>) response.getBody().get("documents");

            System.out.println("카카오 응답 원문 = " + response.getBody());
            System.out.println("카카오 응답 코드 = " + response.getStatusCode());

            for (Map<String, Object> doc : documents) {
                String name = (String) doc.get("place_name");
                String address = (String) doc.get("address_name");
                double lat = Double.parseDouble((String) doc.get("y"));
                double lng = Double.parseDouble((String) doc.get("x"));

                places.add(new InfraDTO(name, address, lat, lng, null));
            }

            boolean isEnd = (boolean) ((Map<String, Object>) response.getBody().get("meta")).get("is_end");
            if (isEnd) break;
        }

        return places;
    }
    @PostConstruct
    public void init() {
        System.out.println("kakao api key = " + apiKey);
    }

    }
