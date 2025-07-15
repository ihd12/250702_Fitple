package com.fitple.fitple.scrap.service;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.job.domain.JobDetail;
import com.fitple.fitple.job.domain.JobPost;
import com.fitple.fitple.job.repository.JobDetailRepository;
import com.fitple.fitple.job.repository.JobPostRepository;
import com.fitple.fitple.scrap.domain.JobScrap;
import com.fitple.fitple.scrap.dto.JobScrapDTO;
import com.fitple.fitple.scrap.repository.JobScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobScrapServiceImpl implements JobScrapService {

    private final JobScrapRepository jobScrapRepository;
    private final JobPostRepository jobPostRepository;
    private final JobDetailRepository jobDetailRepository;

    @Override
    public void scrap(Long userId, Long jobPostId) {
        if (isScrapped(userId, jobPostId)) return;

        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공고가 존재하지 않습니다."));
        JobDetail jobDetail = jobDetailRepository.findByJobPost(jobPost)
                .orElseThrow(() -> new IllegalArgumentException("해당 공고에 대한 상세 정보가 없습니다."));

        User user = User.builder().id(userId).build();

        JobScrap scrap = JobScrap.builder()
                .user(user)
                .jobPost(jobPost)
                .job(jobDetail) // JobDetail 설정
                .build();

        jobScrapRepository.save(scrap);
    }

    @Override
    public void unscrap(Long userId, Long jobPostId) {
        JobPost jobPost = JobPost.builder().id(jobPostId).build();
        User user = User.builder().id(userId).build();
        jobScrapRepository.deleteByUserAndJobPost(user, jobPost);
    }

    @Override
    public boolean isScrapped(Long userId, Long jobPostId) {
        JobPost jobPost = JobPost.builder().id(jobPostId).build();
        User user = User.builder().id(userId).build();
        return jobScrapRepository.existsByUserAndJobPost(user, jobPost);
    }

    @Override
    public boolean isScrapped(User user, Long jobPostId) {
        JobPost jobPost = JobPost.builder().id(jobPostId).build();
        return jobScrapRepository.existsByUserAndJobPost(user, jobPost);
    }

    @Override
    public List<JobScrapDTO> getScrapList(User user) {
        return jobScrapRepository.findAllByUser(user).stream()
                .map(scrap -> JobScrapDTO.builder()
                        .id(scrap.getId())
                        .userId(scrap.getUser().getId())
                        .jobPostId(scrap.getJobPost().getId())
                        .jobId(scrap.getJob().getJobId())
                        .createdAt(scrap.getCreatedAt())
                        .updatedAt(scrap.getUpdatedAt())
                        .jobTitle(scrap.getJobPost().getTitle())
                        .salary(String.valueOf(scrap.getJob().getSalary()))
                        .companyName(scrap.getJobPost().getOrgName())
                        .build())
                .collect(Collectors.toList());

    }

    @Override
    public List<Long> getScrappedJobIdsByUser(User user) {
        List<JobScrap> scraps = jobScrapRepository.findByUser(user);
        return scraps.stream()
                .map(scrap -> scrap.getJob().getJobId())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelScrap(Long jobId, User user) {
        JobPost jobPost = JobPost.builder().id(jobId).build(); // ID만 사용
        jobScrapRepository.deleteByUserAndJobPost(user, jobPost);
    }
}
