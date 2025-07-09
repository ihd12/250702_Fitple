package com.fitple.fitple.housing.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitple.fitple.housing.domain.HousingInfo;
import com.fitple.fitple.housing.dto.HousingRatingResponseDTO;
import com.fitple.fitple.housing.repository.HousingInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class HousingInfoService {

    private final HousingInfoRepository housingInfoRepository;
    private final HousingRatingService housingRatingService;
    private final ObjectMapper objectMapper;

    @Transactional
    public Map<String, Object> getIntegratedHousingList(String brtcCode, String signguCode) throws IOException {

        String housingDataJson = getHousingDataFromGovApi(brtcCode, signguCode);
        Map<String, Object> responseMap = objectMapper.readValue(housingDataJson, new TypeReference<>() {});
        List<Map<String, Object>> externalApiDataList = (List<Map<String, Object>>) responseMap.get("hsmpList");

        if (externalApiDataList == null || externalApiDataList.isEmpty()) {
            return responseMap;
        }

        for (Map<String, Object> item : externalApiDataList) {
            String propertyId = String.format("%s-%s-%s",
                    getStringValueOrDefault(item, "hsmpSn", "N/A"),
                    getStringValueOrDefault(item, "styleNm", "N/A"),
                    getStringValueOrDefault(item, "suplyCmnuseAr", "N/A"));

            housingInfoRepository.findByPropertyId(propertyId)
                    .ifPresentOrElse(
                            info -> log.info("Exists: {}", info.getPropertyId()),
                            () -> {
                                HousingInfo newInfo = mapToHousingInfoEntity(item, propertyId, brtcCode, signguCode);
                                housingInfoRepository.save(newInfo);
                            }
                    );

            HousingRatingResponseDTO ratingInfo = housingRatingService.getAverageRating(propertyId);
            item.put("averageRating", ratingInfo.getAverageRating());
            item.put("ratingCount", ratingInfo.getRatingCount());
        }

        return responseMap;
    }

    private HousingInfo mapToHousingInfoEntity(Map<String, Object> item, String propertyId, String brtcCode, String signguCode) {
        Long localCodeValue = Long.parseLong(brtcCode + signguCode);
        String hsmpNmValue = getStringValueOrDefault(item, "hsmpNm", "정보 없음");
        String sigunguNmValue = getStringValueOrDefault(item, "signguNm", "");

        return HousingInfo.builder()
                .propertyId(propertyId)
                .brtc(brtcCode)
                .localCode(localCodeValue)
                .sigungu(sigunguNmValue)
                .signguNm(sigunguNmValue)
                .houseNm(hsmpNmValue)
                .pnu(getStringValueOrDefault(item, "pnu", "0"))
                .hsmpNm(hsmpNmValue)
                .houseTyNm(getStringValueOrDefault(item, "houseTyNm", "정보 없음"))
                .hshldCo(getLongValueOrDefault(item, "hshldCo"))
                .bassMtRntchrg(getLongValueOrDefault(item, "bassMtRntchrg"))
                .bassRentGtn(getLongValueOrDefault(item, "bassRentGtn"))
                .suplyPrvuseAr(getLongValueOrDefault(item, "suplyPrvuseAr"))
                .suplyCmnuseAr(getLongValueOrDefault(item, "suplyCmnuseAr"))
                .rnAdres(getStringValueOrDefault(item, "rnAdres", "정보 없음"))
                .btrcNm(getStringValueOrDefault(item, "brtcNm", ""))
                .competDe(getStringValueOrDefault(item, "competDe", null))
                .entrpsTel(getStringValueOrDefault(item, "entrpsTel", null))
                .insttNm(getStringValueOrDefault(item, "insttNm", null))
                .mngtEntrpsNm(getStringValueOrDefault(item, "mngtEntrpsNm", null))
                .buldStleNm(getStringValueOrDefault(item, "buldStleNm", null))
                .elvtInstlAt(getStringValueOrDefault(item, "elvtInstlAt", null))
                .parkingCo(getStringValueOrDefault(item, "parkingCo", null))
                .isScrapped(false)
                .score(0)
                .build();
    }

    private long getLongValueOrDefault(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null || String.valueOf(value).isEmpty()) return 0L;
        if (value instanceof String) return Long.parseLong(((String) value).split("\\.")[0]);
        return ((Number) value).longValue();
    }
    private String getStringValueOrDefault(Map<String, Object> map, String key, String defaultValue) {
        Object value = map.get(key);
        if (value == null) return defaultValue;
        return String.valueOf(value);
    }

    private String getHousingDataFromGovApi(String brtcCode, String signguCode) throws IOException {
        String serviceKey = "/muR9hnQHPp2eCu/lLRpq2/XeHUS3uAZ4kAX1qQDBd+jyVHF9JMyDVdo2M6CUAArUU1eKIpLJACxJr4Qc/Pb9w==";
        StringBuilder urlBuilder = new StringBuilder("https://data.myhome.go.kr:443/rentalHouseList");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("brtcCode", "UTF-8") + "=" + URLEncoder.encode(brtcCode, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("signguCode", "UTF-8") + "=" + URLEncoder.encode(signguCode, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300 ? conn.getInputStream() : conn.getErrorStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) { sb.append(line); }
            return sb.toString();
        } finally { conn.disconnect(); }
    }
}