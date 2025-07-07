package com.fitple.fitple.recommend.service;

import com.fitple.fitple.recommend.dto.HousingTestDTO;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RentScoreService {

    public void calculateScores(List<HousingTestDTO> list, int wantedRent) {
        // wantedRent는 만 원 단위 → 원 단위로 변환
        int wanted = wantedRent * 10000;

        for (HousingTestDTO dto : list) {
            int rent = dto.getRent(); // 실제 월세

            int diff = wanted - rent;

            int score;
            if (diff >= 100000) {
                score = 10; // 10만 원 이상 저렴하면 최고점
            } else if (diff >= 0) {
                score = 7; // 희망 월세 이하
            } else if (diff >= -100000) {
                score = 4; // 10만 원 초과 ~ 20만 원 이하 비쌈
            } else {
                score = 1; // 많이 비쌈
            }

            dto.setScore(score);
        }

        // 점수 높은 순 정렬
        list.sort(Comparator.comparingInt(HousingTestDTO::getScore).reversed());
    }
}
