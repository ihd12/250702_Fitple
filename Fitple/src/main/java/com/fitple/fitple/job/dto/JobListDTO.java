package com.fitple.fitple.job.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class JobListDTO {

    private Long id;
    private String title;
    private String orgName;
    private String location;
    private String jobType;
    private Integer salary;
    private String ncs;
    private LocalDate startDate;
    private LocalDate endDate;
}
