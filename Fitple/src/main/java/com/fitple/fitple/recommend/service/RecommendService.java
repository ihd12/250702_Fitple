package com.fitple.fitple.recommend.service;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.job.repository.JobDetailRepository;
import com.fitple.fitple.recommend.dto.HousingRecommendDTO;
import com.fitple.fitple.recommend.dto.JobRecommendDTO;
import com.fitple.fitple.recommend.util.ScoreCalculator;
import com.fitple.fitple.scrap.domain.HousingScrap;
import com.fitple.fitple.scrap.domain.JobScrap;
import com.fitple.fitple.scrap.repository.HousingScrapRepository;
import com.fitple.fitple.scrap.repository.JobScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final JobScrapRepository jobScrapRepository;
    private final HousingScrapRepository housingScrapRepository;
    private final JobDetailRepository jobDetailRepository;

    // 평균 연봉 계산
    public double getAverageSalary(User user) {
        List<JobScrap> scraps = jobScrapRepository.findByUser(user);
        return scraps.stream()
                .mapToDouble(s -> s.getJob().getSalary())
                .average()
                .orElse(0);
    }

    // 평균 주거비 계산
    public double[] getAverageHousingCost(User user) {
        List<HousingScrap> scraps = housingScrapRepository.findByUserId(user.getId());
        double avgDeposit = scraps.stream()
                .mapToDouble(HousingScrap::getBassRentGtn)
                .average()
                .orElse(0);
        double avgRent = scraps.stream()
                .mapToDouble(HousingScrap::getBassMtRntchrg)
                .average()
                .orElse(0);
        return new double[]{avgDeposit, avgRent};
    }

    // 추천 채용공고 리스트 반환
    public List<JobRecommendDTO> getRecommendedJobs(User user) {
        double avgSalary = getAverageSalary(user);

        return jobDetailRepository.findAll().stream()
                .map(job -> {
                    int score = ScoreCalculator.calculateJobScore(
                            job.getSalary() != null ? job.getSalary() : 0,
                            avgSalary
                    );
                    return new JobRecommendDTO(
                            job.getJobId(),
                            job.getTitle(),
                            job.getOrgName(),
                            job.getSalary(),
                            score
                    );
                })
                .sorted((a, b) -> Integer.compare(b.getScore(), a.getScore()))
                .collect(Collectors.toList());
    }

    // 추천 주거지 리스트 반환
    public List<HousingRecommendDTO> getRecommendedHousings(User user) {
        double[] avgCost = getAverageHousingCost(user);
        double avgDeposit = avgCost[0];
        double avgRent = avgCost[1];

        return housingScrapRepository.findAll().stream()
                .map(h -> {
                    int score = ScoreCalculator.calculateHousingScore(
                            h.getBassRentGtn() != null ? h.getBassRentGtn().intValue() : 0,
                            h.getBassMtRntchrg() != null ? h.getBassMtRntchrg().intValue() : 0,
                            avgDeposit,
                            avgRent
                    );
                    return new HousingRecommendDTO(
                            h.getHousingInfoId(),
                            h.getHsmpNm(),
                            h.getRnAdres(),
                            h.getBassRentGtn(),
                            h.getBassMtRntchrg(),
                            score
                    );
                })
                .sorted((a, b) -> Integer.compare(b.getScore(), a.getScore()))
                .collect(Collectors.toList());
    }
}
