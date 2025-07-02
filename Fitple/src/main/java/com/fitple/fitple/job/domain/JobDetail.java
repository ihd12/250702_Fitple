package com.fitple.fitple.job.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "job_detail")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JobDetail {

    @Id
    @Column(name = "job_id")
    private Long jobId;

    @OneToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id", insertable = false, updatable = false)
    private JobPost jobPost;

    @Column(name = "org_name", length = 200, nullable = false)
    private String orgName;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(name = "detail_url", length = 1000, nullable = false)
    private String detailUrl;

    private String ncs;
    private String education;
    private String field;
    private String category;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "is_substitute")
    private String isSubstitute;

    private String location;
    private String salary;
    private Integer headcount;

    @Column(columnDefinition = "TEXT")
    private String preferred;

    private String period;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(columnDefinition = "TEXT")
    private String requirement;

    @Column(columnDefinition = "TEXT")
    private String restriction;

    @Column(name = "advantage_detail", columnDefinition = "TEXT")
    private String advantageDetail;

    @Column(name = "procedure_detail", columnDefinition = "TEXT")
    private String procedureDetail;
}