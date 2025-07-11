package com.fitple.fitple.scrap.repository;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.job.domain.JobPost;
import com.fitple.fitple.scrap.domain.JobScrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobScrapRepository extends JpaRepository<JobScrap, Long> {

    Optional<JobScrap> findByUserAndJobPost(User user, JobPost jobPost);

    void deleteByUserAndJobPost(User user, JobPost jobPost);

    boolean existsByUserAndJobPost(User user, JobPost jobPost);

    // 마이페이지용: 로그인 사용자의 찜 목록 조회
    List<JobScrap> findAllByUser(User user);
    // 추천페이지용
    List<JobScrap> findByUser(User user);

}
