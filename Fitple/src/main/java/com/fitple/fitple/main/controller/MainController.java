package com.fitple.fitple.main.controller;

import com.fitple.fitple.base.user.security.CustomUserDetails;
import com.fitple.fitple.common.dto.PageRequestDTO;
import com.fitple.fitple.notice.domain.Notice;
import com.fitple.fitple.notice.dto.NoticeDTO;
import com.fitple.fitple.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final NoticeService noticeService;

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal CustomUserDetails userDetails){
        if (userDetails != null) {
            model.addAttribute("nickname", userDetails.getNickname());
        }

        List<NoticeDTO> recentNotices = noticeService.getRecentNotices();
        model.addAttribute("notices", recentNotices);

        return "index";
    }
}