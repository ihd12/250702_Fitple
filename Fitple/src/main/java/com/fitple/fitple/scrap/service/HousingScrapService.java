package com.fitple.fitple.scrap.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitple.fitple.scrap.domain.HousingScrap;
import com.fitple.fitple.scrap.dto.HousingInfoAPIResponse;
import com.fitple.fitple.scrap.dto.HousingScrapDTO;
import com.fitple.fitple.scrap.repository.HousingScrapRepository;
import com.fitple.fitple.base.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.net.URLEncoder;


import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HousingScrapService {

    private final HousingScrapRepository housingScrapRepository;
    private final RestTemplate restTemplate;

    @Value("${lh.api.service.key}")
    private String apiServiceKey;

    public HousingScrapService(HousingScrapRepository housingScrapRepository, RestTemplate restTemplate) {
        this.housingScrapRepository = housingScrapRepository;
        this.restTemplate = restTemplate;
    }

    // 스크랩 추가
    public HousingScrap addScrap(Long userId, Long housingInfoId, Long hsmpSn, String brtcCode, String signguCode, String brtcNm, String signguNm) {
        HousingScrap existing = housingScrapRepository.findByUserIdAndHousingInfoId(userId, housingInfoId);

        if (existing != null) {
            existing.setIsScrapped(true);
            return housingScrapRepository.save(existing);
        }

        HousingScrap scrap = new HousingScrap();
        scrap.setUserId(userId);
        scrap.setHousingInfoId(housingInfoId);  // housing_info_id만 저장
        scrap.setIsScrapped(true);
        scrap.setHsmpSn(hsmpSn);                // 단지 식별자
        scrap.setBrtcCode(brtcCode);           // 광역시도 코드
        scrap.setSignguCode(signguCode);       // 시군구 코드
        scrap.setBrtcNm(brtcNm);               // 광역시도 명
        scrap.setSignguNm(signguNm);           // 시군구 명
        return housingScrapRepository.save(scrap);
    }

    // 스크랩 해제
    public HousingScrap removeScrap(Long userId, Long housingInfoId) {
        HousingScrap existing = housingScrapRepository.findByUserIdAndHousingInfoId(userId, housingInfoId);
        if (existing != null) {
            existing.setIsScrapped(false);
            return housingScrapRepository.save(existing);
        }
        return null;
    }


    // 로그인한 사용자의 찜한 주택 목록
    public List<HousingScrapDTO> getScrapList(User user) {
        List<HousingScrap> scraps = housingScrapRepository.findByUserIdAndIsScrappedTrue(user.getId());

        // HousingScrapDTO로 변환
        List<HousingScrapDTO> dtoList = scraps.stream()
                .map(scrap -> {
                    // housingScrap 객체를 그대로 넘겨서 API에서 HousingInfo 데이터를 가져옵니다.
                    HousingInfoAPIResponse apiResponse = fetchHousingInfoFromAPI(scrap);  // 수정된 부분

                    if (apiResponse == null || apiResponse.getHsmpList() == null || apiResponse.getHsmpList().isEmpty()) {
                        return null;  // hsmpList가 없거나 빈 경우에는 DTO 생성하지 않음
                    }

                    // hsmpList에서 첫 번째 항목을 사용하여 DTO 생성
                    HousingInfoAPIResponse.HousingData housingData = apiResponse.getHsmpList().get(0);

                    // DTO에 HousingInfo 데이터를 설정
                    HousingScrapDTO dto = new HousingScrapDTO(
                            housingData.getHsmpNm(),  // 단지명
                            housingData.getRnAdres(), // 도로명 주소
                            housingData.getHouseTyNm(), // 주택 유형명
                            scrap.getHousingInfoId()  // housing_info_id를 DTO에 포함
                    );

                    return dto;
                })
                .filter(dto -> dto != null)  // null 값 필터링
                .collect(Collectors.toList());

        return dtoList;
    }

    // API에서 HousingInfo 데이터를 가져오는 메서드
    private HousingInfoAPIResponse fetchHousingInfoFromAPI(HousingScrap housingScrap) {
        try {
            // HousingScrap 객체에서 필드 값들을 가져옴
            String hsmpSn = housingScrap.getHsmpSn().toString();  // housing_info_id를 hsmpSn으로 사용
            String brtcCode = housingScrap.getBrtcCode();         // 광역시도 코드
            String signguCode = housingScrap.getSignguCode();     // 시군구 코드
            String brtcNm = URLEncoder.encode(housingScrap.getBrtcNm(), "UTF-8");  // 광역시도 명 (URL 인코딩)
            String signguNm = URLEncoder.encode(housingScrap.getSignguNm(), "UTF-8");  // 시군구 명 (URL 인코딩)

            // 필수 요청 파라미터를 추가하여 동적으로 URL을 생성
            String apiUrl = String.format(
                    "https://data.myhome.go.kr:443/rentalHouseList?serviceKey=%s&hsmpSn=%s&brtcCode=%s&signguCode=%s&brtcNm=%s&signguNm=%s&pageNo=1&numOfRows=10&rtnType=JSON",
                    apiServiceKey, hsmpSn, brtcCode, signguCode, brtcNm, signguNm
            );

            // 디버깅을 위해 URL 출력
            System.out.println("Generated API URL: " + apiUrl);

            // RestTemplate을 사용하여 API 호출
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

            // 응답 상태 코드 및 응답 본문 출력
            System.out.println("API Response Status: " + response.getStatusCode());
            String responseBody = response.getBody();
            System.out.println("API Response Body: " + responseBody);  // 응답 본문 로깅

            if (response.getStatusCode().is2xxSuccessful() && responseBody != null && !responseBody.isEmpty()) {
                // 응답 본문을 HousingInfoAPIResponse로 파싱하여 반환
                return parseHousingInfoResponse(responseBody);
            } else {
                // API 호출 실패 시 로깅
                System.out.println("API 호출 실패 또는 빈 응답: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // HTTP 오류에 대한 세부 처리 (4xx, 5xx 상태 코드)
            System.out.println("HTTP Error: " + e.getStatusCode() + " - " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            // URL 인코딩 오류 처리
            System.out.println("URL 인코딩 오류: " + e.getMessage());
        } catch (Exception e) {
            // 그 외의 예외 처리
            System.out.println("API 호출 중 예외 발생: " + e.getMessage());
        }

        return null;  // 실패 시 null 반환
    }


    // HousingInfoAPIResponse를 JSON으로 파싱
    private HousingInfoAPIResponse parseHousingInfoResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, HousingInfoAPIResponse.class);
        } catch (Exception e) {
            System.out.println("API 응답 파싱 실패: " + e.getMessage());
            return null;
        }
    }
}
