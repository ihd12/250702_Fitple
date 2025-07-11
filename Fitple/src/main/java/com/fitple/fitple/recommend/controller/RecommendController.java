//package com.fitple.fitple.recommend.controller;
//
//import com.fitple.fitple.base.user.domain.User;
//import com.fitple.fitple.base.user.security.CustomUserDetails;
//import com.fitple.fitple.recommend.dto.HousingRecommendDTO;
//import com.fitple.fitple.recommend.dto.JobRecommendDTO;
//import com.fitple.fitple.recommend.service.RecommendService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//public class RecommendController {
//
//    private final RecommendService recommendService;
//
//    // 채용 추천 (스크랩 기반)
//    @GetMapping("/api/recommend/jobs")
//    public List<JobRecommendDTO> recommendJobs(@AuthenticationPrincipal CustomUserDetails userDetails) {
//        if (userDetails == null || userDetails.getUser() == null) {
//            return List.of();
//        }
//        User user = userDetails.getUser();
//        return recommendService.getRecommendedScrappedJobs(user);
//    }
//
//    // 주거 추천 (스크랩 기반)
//    @GetMapping("/api/recommend/housings")
//    public List<HousingRecommendDTO> recommendHousings(@AuthenticationPrincipal CustomUserDetails userDetails) {
//        if (userDetails == null || userDetails.getUser() == null) {
//            return List.of();
//        }
//        User user = userDetails.getUser();
//        return recommendService.getRecommendedScrappedHousings(user);
//    }
//}
