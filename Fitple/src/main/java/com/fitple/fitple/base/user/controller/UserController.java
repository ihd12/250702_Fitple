package com.fitple.fitple.base.user.controller;

import com.fitple.fitple.base.user.dto.UserDTO;
import com.fitple.fitple.base.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user") // 디렉토리 구조에 맞춤
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "user/login";  // templates/user/login.html
    }

    @GetMapping("/register")
    public String registerForm() {
        return "user/register";
    }

    @PostMapping("/register")
    public String register(UserDTO dto) {
        userService.register(dto);
        return "redirect:/user/login";  // 경로 일치
    }
}
