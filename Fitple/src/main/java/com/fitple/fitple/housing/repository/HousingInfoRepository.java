package com.fitple.fitple.housing.repository;

import com.fitple.fitple.housing.domain.HousingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HousingInfoRepository extends JpaRepository<HousingInfo, Long> {
}
