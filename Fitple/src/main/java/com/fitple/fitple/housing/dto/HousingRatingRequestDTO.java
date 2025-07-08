package com.fitple.fitple.housing.dto; // DTO 패키지는 맞게 수정해주세요.

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HousingRatingRequestDTO {
    private String userId;
    private String propertyId;
    private Integer ratingScore;
}