package com.fitple.fitple.base.user.controller;

import com.fitple.fitple.base.user.dto.UserDTO;
import com.fitple.fitple.base.user.service.UserService;
import com.fitple.fitple.common.dto.PageRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

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
//    @GetMapping("/{id}")
//    public String getUser(@PathVariable Long id, Model model, PageRequestDTO pageRequestDTO) {
//        model.addAttribute("user", userService.getUser(id));
//        return "user/mypage";
//    }
    @PostMapping("/modify")
    public String modifyUser(UserDTO dto, @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
        if(!userDetails.getUsername().equals(dto.getEmail())){
            throw new AccessDeniedException("권한이 없습니다.");
        }
        userService.modify(dto);
        return "redirect:/user/edit";
    }
    @PostMapping("/remove")
    public String deleteUser(@PathVariable Long id){
        userService.delete(id);
        return "redirect:/";
    }
}
