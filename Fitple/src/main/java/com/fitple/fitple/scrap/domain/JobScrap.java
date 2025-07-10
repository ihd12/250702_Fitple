package com.fitple.fitple.scrap.domain;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.job.domain.JobDetail;
import com.fitple.fitple.job.domain.JobPost;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_scrap", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "job_post_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobScrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private JobDetail job;  // Job → JobDetail 로 변경

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_post_id", nullable = false)
    private JobPost jobPost;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
