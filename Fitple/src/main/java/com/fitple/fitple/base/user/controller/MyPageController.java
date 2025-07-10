package com.fitple.fitple.base.user.controller;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.base.user.security.CustomUserDetails;
import com.fitple.fitple.scrap.dto.HousingScrapDTO;
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

        // 로그인되지 않은 사용자는 로그인 페이지로 리다이렉트
        if (userDetails == null || userDetails.getUser() == null) {
            return "redirect:/user/login";
        }

        User user = userDetails.getUser();

        // 찜한 항목들 가져오기 (빈 리스트를 반환할 수 있음)
        List<JobScrapDTO> jobScraps = jobScrapService.getScrapList(user);
        List<PolicyScrapDTO> policyScraps = policyScrapService.getScrapList(user);
        List<HousingScrapDTO> housingScraps = housingScrapService.getScrapList(user);

        // 빈 리스트를 빈 값으로 넘길 때 오류를 방지하기 위한 처리
        if (jobScraps == null) jobScraps = List.of();
        if (policyScraps == null) policyScraps = List.of();
        if (housingScraps == null) housingScraps = List.of();

        // 모델에 데이터 추가
        model.addAttribute("user", user);
        model.addAttribute("jobScraps", jobScraps);
        model.addAttribute("policyScraps", policyScraps);
        model.addAttribute("housingScraps", housingScraps);

        return "user/mypage";
    }
}
