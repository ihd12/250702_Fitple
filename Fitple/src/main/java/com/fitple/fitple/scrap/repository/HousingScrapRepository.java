package com.fitple.fitple.scrap.repository;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.scrap.domain.HousingScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface HousingScrapRepository extends JpaRepository<HousingScrap, Long> {

    Optional<HousingScrap> findByUserAndPropertyId(User user, Long propertyId);

    Optional<HousingScrap> findByUserIdAndPropertyId(Long userId, Long propertyId);

    List<HousingScrap> findByUserAndIsScrappedTrue(User user);

    void deleteByUserIdAndPropertyId(Long userId, Long propertyId);

    List<HousingScrap> findByUserId(Long userId);

    @Query("SELECT hs.propertyId FROM HousingScrap hs WHERE hs.user.id = :userId AND hs.isScrapped = true")
    List<Long> findPropertyIdsByUserIdAndIsScrappedTrue(@Param("userId") Long userId);
}