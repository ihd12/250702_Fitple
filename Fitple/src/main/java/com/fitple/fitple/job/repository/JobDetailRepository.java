package com.fitple.fitple.job.repository;

import com.fitple.fitple.job.domain.JobDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobDetailRepository extends JpaRepository<JobDetail, Long> {
}
