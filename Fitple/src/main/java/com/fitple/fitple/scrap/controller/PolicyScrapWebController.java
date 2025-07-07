package com.fitple.fitple.scrap.controller;

import com.fitple.fitple.base.user.security.CustomUserDetails;
import com.fitple.fitple.scrap.service.PolicyScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/policy/scrap")
public class PolicyScrapWebController {

    private final PolicyScrapService scrapService;

    @PostMapping("/{id}")
    public String scrap(@PathVariable String id,
                        @RequestParam String plcyNm,
                        @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails != null) {
            scrapService.scrap(userDetails.getUser().getId(), id, plcyNm);
        }

        return "redirect:/policy/detail?plcyNo=" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable String id,
                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails != null) {
            scrapService.cancelScrap(userDetails.getUser().getId(), id);
        }

        return "redirect:/policy/detail?plcyNo=" + id;
    }
}
