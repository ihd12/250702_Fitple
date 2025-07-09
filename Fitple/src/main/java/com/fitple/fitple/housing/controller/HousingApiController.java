// HousingApiController.java

package com.fitple.fitple.housing.controller;

import com.fitple.fitple.housing.service.HousingInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/housing")
public class HousingApiController {

    private final HousingInfoService housingInfoService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getIntegratedHousingList(
            @RequestParam String brtcCode,
            @RequestParam String signguCode) throws IOException {

        Map<String, Object> result = housingInfoService.getIntegratedHousingList(brtcCode, signguCode);
        return ResponseEntity.ok(result);
    }
}