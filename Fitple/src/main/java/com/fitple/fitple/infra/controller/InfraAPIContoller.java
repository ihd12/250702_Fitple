package com.fitple.fitple.infra.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InfraAPIContoller {

    private final String API_KEY = "696E63A0-9FC6-36DC-8BAA-9C9E2B6B5384";

    @GetMapping("/sido")
    public ResponseEntity<String> getSidoList() {
        String url = "https://api.vworld.kr/req/data?service=data" +
                "&request=GetFeature" +
                "&data=LT_C_ADSIDO_INFO" +
                "&key=" + API_KEY +
                "&domain=localhost:8080" +
                "&type=json" +
                "&numOfRows=30" +
                "&size=30" +
                "&geomFilter=BOX(115,33,135,43)";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sigungu")
    public ResponseEntity<String> getSigunguList(@RequestParam String sidoCd) {
        String baseUrl = "https://api.vworld.kr/req/data";
        String url = baseUrl + "?service=data"
                + "&request=GetFeature"
                + "&data=LT_C_ADSIGG_INFO"
                + "&key=" + API_KEY
                + "&domain=localhost:8080"
                + "&type=json"
                + "&numOfRows=30"
                + "&size=30"
                + "&attrFilter=sig_cd:LIKE:" + sidoCd + "%";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/emdList")
    public ResponseEntity<String> getEmdList(@RequestParam String sigunguCd) {
        String url = "https://api.vworld.kr/req/data" +
                "?service=data" +
                "&request=GetFeature" +
                "&data=LT_C_ADEMD_INFO" +
                "&key=" + API_KEY +
                "&type=json" +
                "&numOfRows=30" +
                "&size=30" +
                "&attrFilter=emd_cd:LIKE:" + sigunguCd + "%";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/polygon")
    public ResponseEntity<String> getPolygonData(@RequestParam String sigCd) {
        String baseUrl = "https://api.vworld.kr/req/data";

        // attrFilter는 콜론(:)을 반드시 포함한 형식으로 작성 (인코딩하지 않음)
        String attrFilter = "sig_cd:=:" + sigCd;

        String url = baseUrl + "?service=data" +
                "&request=GetFeature" +
                "&data=LT_C_ADSIGG_INFO" +
                "&key=" + API_KEY +
                "&type=json" +
                "&attrFilter=" + attrFilter;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        return ResponseEntity.ok(response);
    }

}
