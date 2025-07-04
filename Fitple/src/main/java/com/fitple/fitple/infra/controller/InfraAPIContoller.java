package com.fitple.fitple.infra.controller;


import com.fitple.fitple.infra.dto.InfraDTO;
import com.fitple.fitple.infra.service.InfraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InfraAPIContoller {

    private final InfraService infraService;

    @GetMapping("/test")
    public List<InfraDTO> getPlaces(
            @RequestParam String category,
            @RequestParam double x,
            @RequestParam double y
    ) {
        System.out.println("== 컨트롤러 호출됨 ==");
        System.out.println("category = " + category + ", x = " + x + ", y = " + y);
        return infraService.searchPlaces(category, x, y);
    }


}
