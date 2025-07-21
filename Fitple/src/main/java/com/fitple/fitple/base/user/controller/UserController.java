package com.fitple.fitple.base.user.controller;

import com.fitple.fitple.base.user.dto.UserDTO;
import com.fitple.fitple.base.user.security.CustomUserDetails;
import com.fitple.fitple.base.user.security.CustomUserDetailsService;
import com.fitple.fitple.base.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;

@Controller
@RequestMapping("/user") // 디렉토리 구조에 맞춤
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping("/login")
    public String loginForm() {
        return "user/login";  // templates/user/login.html
    }

    @GetMapping("/register")
    public String registerForm(){

        return "user/register";
    }
//
//    @PostMapping("/register")
//    public String register(UserDTO dto) {
//        userService.register(dto);
//        return "redirect:/user/login";  // 경로 일치
//    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserDTO dto, RedirectAttributes redirectAttributes) {

        if (dto.getEmail() == null || dto.getEmail().isBlank() ||
                dto.getNickname() == null || dto.getNickname().isBlank() ||
                dto.getPassword() == null || dto.getPassword().isBlank() ||
                dto.getConfirmPassword() == null || dto.getConfirmPassword().isBlank()) {

            redirectAttributes.addFlashAttribute("error", "모든 필수 항목을 입력해주세요.");
            return "redirect:/user/register";
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/user/register";
        }

        try {
            userService.register(dto);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/register";
        }
        redirectAttributes.addAttribute("success", true);
        return "redirect:/user/login";
    }



    //    @GetMapping("/{id}")
//    public String getUser(@PathVariable Long id, Model model, PageRequestDTO pageRequestDTO) {
//        model.addAttribute("user", userService.getUser(id));
//        return "user/mypage";
//    }
    @GetMapping("/modify")
    public String getModifyUser(@AuthenticationPrincipal UserDetails userDetails, Model model) throws AccessDeniedException {
        String email = userDetails.getUsername();
        UserDTO user = userService.getUser(email);
        if(!email.equals(user.getEmail())) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
        model.addAttribute("user", user);
        return "user/edit";
    }
    @PostMapping("/modify")
    public String modifyUser(UserDTO dto, @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
        if(!userDetails.getUsername().equals(dto.getEmail())){
            throw new AccessDeniedException("권한이 없습니다.");
        }
        userService.modify(dto);

        UserDetails mainName = customUserDetailsService.loadUserByUsername(userDetails.getUsername());
        Authentication newAuth = new UsernamePasswordAuthenticationToken(mainName, mainName.getPassword(), mainName.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "redirect:/user/modify";
    }
    @PostMapping("/remove")
    public String deleteUser(UserDTO userDTO,
                             @AuthenticationPrincipal UserDetails userDetails,
                             HttpServletRequest request) throws AccessDeniedException {
        if (!userDetails.getUsername().equals(userDTO.getEmail())) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        // 1. 회원 삭제
        userService.delete(userDetails.getUsername());
        // 2. 시큐리티 인증정보 + 세션 무효화
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        return "redirect:/user/login";  // 또는 "redirect:/"도 가능
    }


}
