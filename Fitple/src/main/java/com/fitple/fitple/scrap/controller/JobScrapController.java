package com.fitple.fitple.scrap.controller;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.scrap.service.JobScrapService;
import com.fitple.fitple.base.user.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/job/scrap")
public class JobScrapController {

    private final JobScrapService jobScrapService;

    @PostMapping("/{jobPostId}")
    public String scrap(@PathVariable Long jobPostId,
                        @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userDetails.getUser();
        jobScrapService.scrap(user.getId(), jobPostId);
        return "redirect:/job/test/" + jobPostId;
    }

    @PostMapping("/{jobPostId}/cancel")
    public String unscrap(@PathVariable Long jobPostId,
                          @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userDetails.getUser();
        jobScrapService.unscrap(user.getId(), jobPostId);
        return "redirect:/job/test/" + jobPostId;
    }
}
