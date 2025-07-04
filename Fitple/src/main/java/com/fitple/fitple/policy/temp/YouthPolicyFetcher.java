package com.fitple.fitple.policy.temp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitple.fitple.policy.dto.YouthPolicyResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class YouthPolicyFetcher {
    public static void main(String[] args) {
        try {
            String urlStr = "https://www.youthcenter.go.kr/go/ythip/getPlcy";
            String params = "apiKeyNm=13464484-9c91-4cf0-bb2a-812087e0c6df&pageNum=1&pageSize=3&rtnType=json";

            URL url = new URL(urlStr + "?" + params);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseStrBuilder.append(line);
                }
                reader.close();

                String jsonResponse = responseStrBuilder.toString();

                // JSON을 DTO로 파싱
                ObjectMapper mapper = new ObjectMapper();
                YouthPolicyResponse response = mapper.readValue(jsonResponse, YouthPolicyResponse.class);

                // 결과 출력 (예시)
                System.out.println("Result Code: " + response.getResultCode());
                System.out.println("Result Message: " + response.getResultMessage());
                System.out.println("Total Count: " + response.getResult().getPagging().getTotCount());
                System.out.println("첫 번째 정책명: " + response.getResult().getYouthPolicyList().get(0).getPlcyNm());
            } else {
                System.out.println("Error: " + responseCode);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
