package com.fitple.fitple.housing.repository;

import com.fitple.fitple.housing.domain.HousingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HousingInfoRepository extends JpaRepository<HousingInfo, Long> {

    Optional<HousingInfo> findByPropertyId(String propertyId);
}