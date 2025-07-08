package com.fitple.fitple.housing.repository;

import com.fitple.fitple.housing.domain.HousingRating;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface HousingRatingRepository extends JpaRepository<HousingRating, Long> {

    Optional<HousingRating> findByUserIdAndPropertyId(String userId, String propertyId);

    List<HousingRating> findAllByPropertyId(String propertyId);
}