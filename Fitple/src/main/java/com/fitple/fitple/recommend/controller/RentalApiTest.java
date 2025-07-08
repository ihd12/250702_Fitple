package com.fitple.fitple.recommend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitple.fitple.recommend.dto.HousingTestDTO;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Component
public class RentalApiTest {

    // 디코딩된 원본 키
    private final String serviceKey = "sGd1gU4DHYuJ2ivcraGO5+uBqk+tiY135P/T2+zIKR0QkrS2/8cUr5noy9Mhybw+FoP+EOzMfcEpjQqW7c5tMg==";

    public List<HousingTestDTO> fetchHousingList() {
        List<HousingTestDTO> list = new ArrayList<>();

        try {
            String encodedKey = URLEncoder.encode(serviceKey, "UTF-8");

            String urlStr = "https://data.myhome.go.kr:443/rentalHouseList"
                    + "?ServiceKey=" + encodedKey
                    + "&brtcCode=11"
                    + "&signguCode=140"
                    + "&pageNo=1"
                    + "&numOfRows=10";

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = conn.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    responseCode == 200 ? conn.getInputStream() : conn.getErrorStream(), "UTF-8"
            ));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();

            String json = sb.toString();

//            System.out.println("==== API 응답 원문(JSON) ====");
//            System.out.println(json);
//            System.out.println("=================================");

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            String code = root.path("code").asText();
            if (!"000".equals(code)) {
                System.out.println("🚨 API 오류 응답: code=" + code + ", msg=" + root.path("msg").asText());
                return list;
            }

            JsonNode items = root.path("hsmpList");
            if (items.isArray()) {
                for (JsonNode item : items) {
                    String uid = item.path("hsmpSn").asText();
                    String addr = item.path("rnAdres").asText();
                    int rent = parseIntSafe(item.path("bassMtRntchrg").asText());
                    int deposit = parseIntSafe(item.path("bassRentGtn").asText());

                    list.add(new HousingTestDTO(uid, addr, rent, deposit, 0));
                }
            }

        } catch (Exception e) {
            System.out.println("API 호출 실패: " + e.getMessage());
        }

        return list;
    }

    private int parseIntSafe(String value) {
        try {
            return (value == null || value.isBlank()) ? 0 : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
