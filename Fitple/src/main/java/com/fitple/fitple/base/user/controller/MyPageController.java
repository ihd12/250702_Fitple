package com.fitple.fitple.base.user.controller;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.base.user.security.CustomUserDetails;
import com.fitple.fitple.scrap.dto.JobScrapDTO;
import com.fitple.fitple.scrap.dto.PolicyScrapDTO;
import com.fitple.fitple.scrap.service.HousingScrapService;
import com.fitple.fitple.scrap.service.JobScrapService;
import com.fitple.fitple.scrap.service.PolicyScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fitple.fitple.scrap.dto.HousingScrapDTO;


import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {

    private final JobScrapService jobScrapService;
    private final PolicyScrapService policyScrapService;
    private final HousingScrapService housingScrapService;


    @GetMapping
    public String myPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        if (userDetails == null) {
            return "redirect:/user/login";
        }

        User user = userDetails.getUser();

        List<JobScrapDTO> jobScraps = jobScrapService.getScrapList(user);
        List<PolicyScrapDTO> policyScraps = policyScrapService.getScrapList(user);
        List<HousingScrapDTO> housingScraps = housingScrapService.getScrapList(user);

        model.addAttribute("user", user);
        model.addAttribute("jobScraps", jobScraps);
        model.addAttribute("policyScraps", policyScraps);
        model.addAttribute("housingScraps", housingScraps);

        return "user/mypage";
    }

}
