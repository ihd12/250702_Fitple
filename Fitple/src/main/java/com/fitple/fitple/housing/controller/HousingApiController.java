package com.fitple.fitple.housing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@RestController
public class HousingApiController {

    @GetMapping("/api/housing")
    public String getRentalHouseList() throws IOException {
        String serviceKey = "/muR9hnQHPp2eCu/lLRpq2/XeHUS3uAZ4kAX1qQDBd+jyVHF9JMyDVdo2M6CUAArUU1eKIpLJACxJr4Qc/Pb9w==";

        // ✅ 사용자님이 제공한 샘플 코드 로직을 그대로 적용합니다.
        StringBuilder urlBuilder = new StringBuilder("https://data.myhome.go.kr:443/rentalHouseList");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("brtcCode", "UTF-8") + "=" + URLEncoder.encode("11", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("signguCode", "UTF-8") + "=" + URLEncoder.encode("140", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("50", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        String response = sb.toString();

        System.out.println("---------- API 서버 원본 응답 ----------");
        System.out.println(response);
        System.out.println("------------------------------------");

        return response;
    }
}
