package com.fitple.fitple.recommend.controller;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.base.user.security.CustomUserDetails;
import com.fitple.fitple.recommend.dto.HousingRecommendDTO;
import com.fitple.fitple.recommend.dto.JobRecommendDTO;
import com.fitple.fitple.recommend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RecommendViewController {

    private final RecommendService recommendService;

    @GetMapping("/recommend")
    public String showRecommendPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null || userDetails.getUser() == null) {
            return "redirect:user/login";
        }

        User user = userDetails.getUser();

        // 채용: 스크랩한 것만 추천 점수 계산
        List<JobRecommendDTO> recommendedJobs = recommendService.getRecommendedScrappedJobs(user);

        // 주거: 기존 방식 유지 (스크랩 기반)
        List<HousingRecommendDTO> recommendedHousings = recommendService.getRecommendedScrappedHousings(user);

        // 주거 JSON 문자열 리스트 추가 (모달용)
        List<String> housingJsonList = recommendedHousings.stream()
                .map(h -> {
                    try {
                        return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(h);
                    } catch (Exception e) {
                        return "{}";
                    }
                })
                .toList();

        model.addAttribute("recommendedJobs", recommendedJobs);
        model.addAttribute("recommendedHousings", recommendedHousings);
        model.addAttribute("housingJsonList", housingJsonList);

        return "recommend/recommend";
    }

}
