package com.fitple.fitple.job.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class JobDetailDTO {

    private Long id;
    private String title;
    private String orgName;
    private String location;
    private String jobType;
    private String ncs;
    private Integer salary;
    private LocalDate startDate;
    private LocalDate endDate;
    private String detailUrl;
    private String education;
    private String requirement;
    private String preferred;
    private String procedureDetail;
    private String advantageDetail;
}
