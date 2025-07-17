package com.fitple.fitple.housing.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
public class HousingApiController {

    static {
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                (hostname, session) -> true
        );

        try {
            javax.net.ssl.SSLContext ctx = javax.net.ssl.SSLContext.getInstance("TLS");
            ctx.init(null, new javax.net.ssl.TrustManager[]{
                    new javax.net.ssl.X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
                    }
            }, new java.security.SecureRandom());
            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping(value = "/api/housing", produces = "application/json; charset=UTF-8")
    public String getRentalHouseList(
            @RequestParam String brtcCode,
            @RequestParam String signguCode,
            @RequestParam String numOfRows) throws IOException { // 최대 100개까지 호출이기는 하나, 매번 100번씩 호출하면 느려져서 일부러 5 10 25로 제한함.

        // --- 1. 정부 API에서 원본 데이터 가져오기 ---
        // 여기서 numOfRows 값을 getHousingDataFromGov 메소드로 전달
        String housingDataJson = getHousingDataFromGov(brtcCode, signguCode, numOfRows);
        if (housingDataJson == null || housingDataJson.isEmpty()) {
            return "{\"error\":\"Failed to get data from government API\"}";
        }

        // --- 2. JSON을 Java 객체로 변환 ---
        Map<String, Object> housingDataMap = objectMapper.readValue(housingDataJson, new TypeReference<>() {});
        List<Map<String, Object>> hsmpList = (List<Map<String, Object>>) housingDataMap.get("hsmpList");

        if (hsmpList != null && !hsmpList.isEmpty()) {
            // ▼▼▼ 이름 변경 로직 추가 ▼▼▼
            Map<String, String> nameFixMap = Map.of(
                    "강원도", "강원특별자치도",
                    "전라북도", "전북특별자치도"
            );

            for (Map<String, Object> item : hsmpList) {
                String brtcNm = (String) item.get("brtcNm");
                if (nameFixMap.containsKey(brtcNm)) {
                    item.put("brtcNm", nameFixMap.get(brtcNm));
                }
            }
        }

        // --- 3. 수정된 데이터를 다시 JSON 문자열로 변환하여 반환 ---
        return objectMapper.writeValueAsString(housingDataMap);
    }

    // 정부 API에서 데이터를 가져오는 기존 로직 (numOfRows 인자 추가 및 사용)
    private String getHousingDataFromGov(String brtcCode, String signguCode, String numOfRows) throws IOException {
        String serviceKey = "/muR9hnQHPp2eCu/lLRpq2/XeHUS3uAZ4kAX1qQDBd+jyVHF9JMyDVdo2M6CUAArUU1eKIpLJACxJr4Qc/Pb9w=="; // 실제 키는 보안에 유의하세요.
        StringBuilder urlBuilder = new StringBuilder("https://data.myhome.go.kr:443/rentalHouseList");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("brtcCode", "UTF-8") + "=" + URLEncoder.encode(brtcCode, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("signguCode", "UTF-8") + "=" + URLEncoder.encode(signguCode, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8")); // <--- 이 부분이 수정되었습니다.
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(
                conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300 ? conn.getInputStream() : conn.getErrorStream(),
                StandardCharsets.UTF_8
        ))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } finally {
            conn.disconnect();
        }
    }
}