package com.fitple.fitple.scrap.controller;

import com.fitple.fitple.base.user.security.CustomUserDetails;
import com.fitple.fitple.scrap.service.PolicyScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/policy/scrap/web")
public class PolicyScrapWebController {

    private final PolicyScrapService scrapService;

    @PostMapping
    public String scrap(@RequestParam List<String> plcyNos,  // 여러 정책 ID 목록
                        @RequestParam List<String> plcyNms,  // 여러 정책 이름 목록
                        @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails != null) {
            // 여러 정책을 한 번에 DB에 저장
            scrapService.scrap(userDetails.getUser().getId(), plcyNos, plcyNms);
        }

        return "redirect:/policy/list";  // 정책 목록 페이지로 리다이렉트
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable String id,
                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails != null) {
            scrapService.cancelScrap(userDetails.getUser().getId(), id);
        }

        return "redirect:/policy/detail?plcyNo=" + id;  // 찜 취소 후, 해당 정책 상세 페이지로 리다이렉트
    }
}
