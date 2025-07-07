package com.fitple.fitple.scrap.domain;

import com.fitple.fitple.base.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "policy_scrap",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "policy_id"})
)
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(of = "id")
public class PolicyScrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "policy_id", nullable = false, length = 30)
    private String policyId;

    @Column(name = "policy_name", nullable = false, length = 200)
    private String policyName;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
