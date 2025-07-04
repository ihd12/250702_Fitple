package com.fitple.fitple.job.repository;

import com.fitple.fitple.job.domain.JobPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {

    @Query("""
        SELECT jp FROM JobPost jp
        JOIN FETCH jp.jobDetail jd
        WHERE (:location IS NULL OR jp.location LIKE %:location%)
          AND (:ncs IS NULL OR jp.ncs LIKE %:ncs%)
          AND (:salary IS NULL OR jd.salary >= :salary)
    """)
    List<JobPost> searchJobs(
            @Param("location") String location,
            @Param("ncs") String ncs,
            @Param("salary") Integer salary
    );

    @Query("""
    SELECT jp FROM JobPost jp
    JOIN FETCH jp.jobDetail jd
    WHERE jp.title LIKE %:keyword%
""")
    List<JobPost> searchByKeyword(@Param("keyword") String keyword);

    @Query("""
    SELECT jp FROM JobPost jp
    JOIN FETCH jp.jobDetail jd
    WHERE (:location IS NULL OR jp.location LIKE %:location%)
      AND (:ncs IS NULL OR jp.ncs LIKE %:ncs%)
      AND (:salary IS NULL OR jd.salary >= :salary)
      AND (:keyword IS NULL OR jp.title LIKE %:keyword%)
""")
    List<JobPost> advancedSearch(
            @Param("location") String location,
            @Param("ncs") String ncs,
            @Param("salary") Integer salary,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
    SELECT COUNT(jp) FROM JobPost jp
    JOIN jp.jobDetail jd
    WHERE (:location IS NULL OR jp.location LIKE %:location%)
      AND (:ncs IS NULL OR jp.ncs LIKE %:ncs%)
      AND (:salary IS NULL OR jd.salary >= :salary)
      AND (:keyword IS NULL OR jp.title LIKE %:keyword%)
""")
    int advancedSearchCount(
            @Param("location") String location,
            @Param("ncs") String ncs,
            @Param("salary") Integer salary,
            @Param("keyword") String keyword
    );




}
