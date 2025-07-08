package com.fitple.fitple.infra.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitple.fitple.infra.dto.InfraDTO;
import com.fitple.fitple.infra.service.InfraService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/sigun")
    public ResponseEntity<String> getSigungu(@RequestParam String sidoCd) throws UnsupportedEncodingException {
        String baseUrl = "https://api.vworld.kr/req/data";

        // 와일드카드로 % 사용 (NOT *, NOT 인코딩)
        String attrFilter = "sig_cd:LIKE:" + sidoCd + "%";

        String url = baseUrl + "?service=data" +
                "&request=GetFeature&type=json" +
                "&data=LT_C_ADSIGG_INFO" +
                "&attrFilter=" + attrFilter +
                "&key=" + API_KEY;

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

//    @GetMapping("/emd")
//    public ResponseEntity<String> getEmdPolygon(@RequestParam String emdName) {
//        String url = "https://api.vworld.kr/req/data" +
//                "?service=data" +
//                "&request=GetFeature" +
//                "&data=LT_C_ADEMD_INFO" +
//                "&key=" + API_KEY +
//                "&type=json" +
//                "&attrFilter=emd_kor_nm:LIKE:%" + emdName + "%";
//
//        RestTemplate restTemplate = new RestTemplate();
//        String response = restTemplate.getForObject(url, String.class);
//        return ResponseEntity.ok(response);
//    }

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


    @GetMapping("/emdPolygon")
    public ResponseEntity<String> getEmdPolygon(@RequestParam String emdCode) {
        String url = "https://api.vworld.kr/req/data" +
                "?service=data" +
                "&request=GetFeature" +
                "&data=LT_C_ADEMD_INFO" +
                "&key=" + API_KEY +
                "&type=json" +
                "&numOfRows=100" +
                "&attrFilter=emd_cd:EQ:" + emdCode +
                "&domain=localhost:8080";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }
    @Autowired
    private InfraService addressService;

    @GetMapping("/api/sigCdByAddress")
    public ResponseEntity<Map<String, String>> getSigCdByAddress(@RequestParam String address) {
        String sigCd = addressService.getSigCdFromAddress(address); // 주소로 sigCd 찾기

        if (sigCd == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "해당 주소의 sigCd를 찾을 수 없습니다."));
        }

        Map<String, String> response = new HashMap<>();
        response.put("sigCd", sigCd);
        return ResponseEntity.ok(response);
    }
}
