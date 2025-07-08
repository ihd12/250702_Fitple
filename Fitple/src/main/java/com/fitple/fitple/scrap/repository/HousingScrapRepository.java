package com.fitple.fitple.scrap.repository;

import com.fitple.fitple.scrap.domain.HousingScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HousingScrapRepository extends JpaRepository<HousingScrap, Long> {

    HousingScrap findByUserIdAndHousingInfoId(Long userId, Long housingInfoId);

    @Query("SELECT h FROM HousingScrap h WHERE h.userId = :userId AND h.isScrapped = true")
    List<HousingScrap> findByUserIdAndIsScrappedTrue(@Param("userId") Long userId);
}
