package com.fitple.fitple.job.repository;

import com.fitple.fitple.job.domain.JobDetail;
import com.fitple.fitple.job.domain.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobDetailRepository extends JpaRepository<JobDetail, Long> {

    // JobPost를 기준으로 JobDetail 조회
    Optional<JobDetail> findByJobPost(JobPost jobPost);
}
