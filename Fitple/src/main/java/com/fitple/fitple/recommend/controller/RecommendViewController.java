package com.fitple.fitple.recommend.controller;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.base.user.security.CustomUserDetails;
import com.fitple.fitple.job.domain.JobDetail;
import com.fitple.fitple.job.repository.JobDetailRepository;
import com.fitple.fitple.recommend.dto.HousingRecommendDTO;
import com.fitple.fitple.recommend.dto.JobRecommendDTO;
import com.fitple.fitple.recommend.service.RecommendService;
import com.fitple.fitple.recommend.util.ScoreCalculator;
import com.fitple.fitple.scrap.domain.HousingScrap;
import com.fitple.fitple.scrap.repository.HousingScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller // 템플릿 반환을 위한 컨트롤러
@RequiredArgsConstructor
public class RecommendViewController {

    private final RecommendService recommendService;
    private final JobDetailRepository jobDetailRepository;
    private final HousingScrapRepository housingScrapRepository;

    @GetMapping("/recommend")
    public String showRecommendPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        // 로그인 여부 먼저 검사
        if (userDetails == null || userDetails.getUser() == null) {
            return "redirect:/login";
        }

        User user = userDetails.getUser();

        // 채용 추천
        double avgSalary = recommendService.getAverageSalary(user);
        List<JobRecommendDTO> recommendedJobs = jobDetailRepository.findAll().stream()
                .map(job -> new JobRecommendDTO(
                        job.getJobId(),
                        job.getTitle(),
                        job.getOrgName(),
                        job.getSalary(),
                        ScoreCalculator.calculateJobScore(
                                job.getSalary() != null ? job.getSalary() : 0,
                                avgSalary
                        )))
                .sorted((a, b) -> Integer.compare(b.getScore(), a.getScore()))
                .collect(Collectors.toList());

        // 주거 추천
        double[] avgCost = recommendService.getAverageHousingCost(user);
        double avgDeposit = avgCost[0];
        double avgRent = avgCost[1];
        List<HousingRecommendDTO> recommendedHousings = housingScrapRepository.findAll().stream()
                .map(h -> new HousingRecommendDTO(
                        h.getHousingInfoId(),
                        h.getHsmpNm(),
                        h.getRnAdres(),
                        h.getBassRentGtn(),
                        h.getBassMtRntchrg(),
                        ScoreCalculator.calculateHousingScore(
                                h.getBassRentGtn() != null ? h.getBassRentGtn().intValue() : 0,
                                h.getBassMtRntchrg() != null ? h.getBassMtRntchrg().intValue() : 0,
                                avgDeposit,
                                avgRent
                        )))
                .sorted((a, b) -> Integer.compare(b.getScore(), a.getScore()))
                .collect(Collectors.toList());

        // 모델에 담기
        model.addAttribute("recommendedJobs", recommendedJobs);
        model.addAttribute("recommendedHousings", recommendedHousings);

        return "recommend/recommend";
    }

}
