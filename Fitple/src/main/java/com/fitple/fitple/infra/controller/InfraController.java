package com.fitple.fitple.infra.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class InfraController {

    @GetMapping("/infra")
    public String test(){
        return "/infra/infra";
    }

}
