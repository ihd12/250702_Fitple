package com.fitple.fitple.housing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HousingViewController {

    @GetMapping("/housing")
    public String housingMapPage() {
        // templates/housing/housing_map_test.html 파일을 찾아 보여줌
        return "housing/housing_map_test";
    }
}