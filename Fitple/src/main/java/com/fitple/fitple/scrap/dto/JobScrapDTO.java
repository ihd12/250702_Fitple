package com.fitple.fitple.scrap.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder // ✅ builder() 메서드를 사용하려면 반드시 필요
public class JobScrapDTO {

    private Long id;

    private Long userId;

    private Long jobId;

    private Long jobPostId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String jobTitle;

    private String salary;

    private String companyName;
}
