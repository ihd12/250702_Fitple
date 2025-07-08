package com.fitple.fitple.housing.service;

import com.fitple.fitple.housing.domain.HousingRating;
import com.fitple.fitple.housing.dto.HousingRatingResponseDTO;
import com.fitple.fitple.housing.repository.HousingRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HousingRatingService {

    private final HousingRatingRepository housingRatingRepository;

    @Transactional
    public HousingRatingResponseDTO rateProperty(String userId, String propertyId, Integer score) {
        // ... (기존 로직 동일)
        housingRatingRepository.findByUserIdAndPropertyId(userId, propertyId)
                .ifPresentOrElse(
                        existingRating -> existingRating.updateScore(score),
                        () -> {
                            HousingRating newRating = HousingRating.builder()
                                    .userId(userId)
                                    .propertyId(propertyId)
                                    .ratingScore(score)
                                    .build();
                            housingRatingRepository.save(newRating);
                        }
                );
        return getAverageRating(propertyId);
    }

    public Optional<HousingRating> getUserRatingForProperty(String userId, String propertyId) {
        return housingRatingRepository.findByUserIdAndPropertyId(userId, propertyId);
    }

    // public으로 변경하여 다른 서비스에서 재사용 가능하도록 함
    public HousingRatingResponseDTO getAverageRating(String propertyId) {
        List<HousingRating> ratings = housingRatingRepository.findAllByPropertyId(propertyId);
        if (ratings.isEmpty()) {
            return new HousingRatingResponseDTO(0.0, 0L);
        }
        double sum = ratings.stream().mapToInt(HousingRating::getRatingScore).sum();
        double avg = sum / (double) ratings.size();
        return new HousingRatingResponseDTO(avg, (long) ratings.size());
    }
}