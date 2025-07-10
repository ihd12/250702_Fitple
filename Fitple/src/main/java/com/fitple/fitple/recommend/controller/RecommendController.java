package com.fitple.fitple.recommend.controller;

import com.fitple.fitple.job.repository.JobPostRepository;
import com.fitple.fitple.recommend.dto.HousingTestDTO;
import com.fitple.fitple.recommend.dto.JobRecommendDTO;
import com.fitple.fitple.recommend.service.RentScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendController {

    private final JobPostRepository jobPostRepository;
    private final RentalApiTest rentalApiTest;
    private final RentScoreService rentScoreService;

    @GetMapping
    public String recommend(@RequestParam(defaultValue = "3500") int salary,
                            @RequestParam(defaultValue = "60") int rent,
                            Model model) {

        // ✅ 테스트용 채용 데이터 (향후 실제 추천 로직 대체)
        List<Long> fakeJobIds = List.of(101L, 102L, 103L);
        List<JobRecommendDTO> jobList = jobPostRepository.findByIdIn(fakeJobIds);

        for (JobRecommendDTO dto : jobList) {
            int diff = dto.getSalary() - salary;
            int score;
            if (diff >= 1000) score = 10;
            else if (diff >= 500) score = 7;
            else if (diff >= 0) score = 5;
            else if (diff >= -500) score = 2;
            else score = -3;
            dto.setScore(score);
        }

        jobList.sort(Comparator.comparingInt(JobRecommendDTO::getScore).reversed());

        // ✅ 임대주택 추천
        List<HousingTestDTO> housingList = rentalApiTest.fetchHousingList();
        rentScoreService.calculateScores(housingList, rent);

        model.addAttribute("recommendList", jobList);
        model.addAttribute("userSalary", salary);
        model.addAttribute("housingList", housingList);
        model.addAttribute("wantedRent", rent);

        return "recommend/recommend";
    }
}
