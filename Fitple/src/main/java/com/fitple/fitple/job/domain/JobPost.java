package com.fitple.fitple.job.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "job_post")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "org_name", length = 200, nullable = false)
    private String orgName;

    @Column(length = 200, nullable = false)
    private String region;

    @Column(name = "job_type", length = 100, nullable = false)
    private String jobType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(length = 10, nullable = false)
    private String dday;

    @Column(length = 100, nullable = false)
    private String status;

    @Column(name = "detail_url", length = 1000, nullable = false)
    private String detailUrl;
}