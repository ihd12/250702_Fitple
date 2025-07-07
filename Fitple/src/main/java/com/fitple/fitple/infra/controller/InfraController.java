package com.fitple.fitple.infra.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/infra")
public class InfraController {

    @GetMapping("/test")
    public String test(){
        return "infra/test1";
    }

    @GetMapping("/test2")
    public String test4(){
        return "infra/test4";
    }

}
