package com.fitple.fitple.recommend.controller;

import com.fitple.fitple.recommend.dto.HousingTestDTO;
import com.fitple.fitple.recommend.service.RentScoreService;

import java.util.ArrayList;
import java.util.List;

public class RentRecommendTest {

    public static void main(String[] args) {
        // 희망 임대료
        int wantedRent = 60;

        // 임시 주거지 목록
        List<HousingTestDTO> housingList = new ArrayList<>();
        housingList.add(new HousingTestDTO("H001", "서울 강남구", 80, 0, 0));
        housingList.add(new HousingTestDTO("H002", "서울 성북구", 60, 0, 0));
        housingList.add(new HousingTestDTO("H003", "경기도 수원시", 50, 0, 0));
        housingList.add(new HousingTestDTO("H004", "인천 미추홀구", 45, 0, 0));

        // 점수 계산
        RentScoreService service = new RentScoreService();
        service.calculateScores(housingList, wantedRent);

        // 결과 출력
        System.out.println("== 임대료 기반 추천 결과 ==");
        for (HousingTestDTO dto : housingList) {
            System.out.printf("UID: %s | 지역: %s | 임대료: %d만원 | 점수: %d점%n",
                    dto.getUid(), dto.getArea(), dto.getRent(), dto.getScore());
        }
    }
}
