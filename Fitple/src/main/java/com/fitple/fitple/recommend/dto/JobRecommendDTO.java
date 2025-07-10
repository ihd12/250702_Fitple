package com.fitple.fitple.recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRecommendDTO {

    private Long jobId;         // JobDetail.jobId
    private String title;       // JobDetail.title
    private String orgName;     // JobDetail.orgName (기관명)
    private Integer salary;     // JobDetail.salary
    private int score;          // 추천 점수 (10점 만점)
}
