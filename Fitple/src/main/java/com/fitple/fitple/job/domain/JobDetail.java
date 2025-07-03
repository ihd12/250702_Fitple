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
public class JobDetail {

    @Id
    @Column(name = "job_id")
    private Long jobId;  // = job_post.id (외래키처럼 사용)

    @OneToOne
    @MapsId
    @JoinColumn(name = "job_id")
    private JobPost jobPost;

    @Column(columnDefinition = "TEXT")
    private String advantageDetail;

    private String category;
    private LocalDate createdDate;

    @Column(name = "detail_url", length = 1000, nullable = false)
    private String detailUrl;

    private String education;
    private String field;
    private Integer headcount;

    @Column(name = "is_substitute")
    private String isSubstitute;

    private String jobType;
    private String location;
    private String ncs;

    @Column(name = "org_name", length = 200, nullable = false)
    private String orgName;

    private String period;

    @Column(columnDefinition = "TEXT")
    private String preferred;

    @Column(name = "procedure_detail", columnDefinition = "TEXT")
    private String procedureDetail;

    @Column(columnDefinition = "TEXT")
    private String requirement;

    @Column(columnDefinition = "TEXT")
    private String restriction;

    private String salary;

    @Column(length = 500, nullable = false)
    private String title;
}
