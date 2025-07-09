package com.fitple.fitple.scrap.repository;

import com.fitple.fitple.scrap.domain.HousingScrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HousingScrapRepository extends JpaRepository<HousingScrap, Long> {

    // 사용자와 스크랩된 주택 정보를 조회할 때 housing_info_id를 기반으로 가져오기
    List<HousingScrap> findByUserIdAndIsScrappedTrue(Long userId);

    // 사용자 ID와 housing_info_id를 통해 특정 스크랩 정보를 조회
    HousingScrap findByUserIdAndHousingInfoId(Long userId, Long housingInfoId);  // 수정된 부분
}
