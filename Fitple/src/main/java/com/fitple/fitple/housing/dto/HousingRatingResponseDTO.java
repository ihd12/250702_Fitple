package com.fitple.fitple.housing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HousingRatingResponseDTO {

    private double averageRating;
    private long ratingCount;

}