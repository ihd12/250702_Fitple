package com.fitple.fitple.scrap.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PolicyScrapDTO {
    private Long id;
    private String policyId;
    private String policyName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
