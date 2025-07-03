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
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dday;

    @Column(name = "detail_url", length = 1000, nullable = false)
    private String detailUrl;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "job_type", length = 100, nullable = false)
    private String jobType;

    @Column(name = "org_name", length = 200, nullable = false)
    private String orgName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(length = 100, nullable = false)
    private String status;

    private String title;

    @Column(length = 200, nullable = false)
    private String location;

    private String ncs;

    @OneToOne(mappedBy = "jobPost", cascade = CascadeType.ALL)
    private JobDetail jobDetail;
}

