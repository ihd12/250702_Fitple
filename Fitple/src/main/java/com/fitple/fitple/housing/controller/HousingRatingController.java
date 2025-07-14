package com.fitple.fitple.housing.controller;

import com.fitple.fitple.housing.domain.HousingRating;
import com.fitple.fitple.housing.dto.HousingRatingRequestDTO;
import com.fitple.fitple.housing.dto.HousingRatingResponseDTO;
import com.fitple.fitple.housing.service.HousingRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class HousingRatingController {

    private final HousingRatingService housingRatingService;

    @GetMapping
    public ResponseEntity<HousingRating> getUserRating(
            @RequestParam String userId,
            @RequestParam String propertyId) {

        Optional<HousingRating> ratingOptional = housingRatingService.getUserRatingForProperty(userId, propertyId);

        return ratingOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping
    public ResponseEntity<HousingRatingResponseDTO> rateProperty(@RequestBody HousingRatingRequestDTO request) {
        HousingRatingResponseDTO newRatingInfo = housingRatingService.rateProperty(
                request.getUserId(),
                request.getPropertyId(),
                request.getRatingScore()
        );
        return ResponseEntity.ok(newRatingInfo);
    }
}