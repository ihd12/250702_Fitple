package com.fitple.fitple.scrap.service;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.scrap.dto.JobScrapDTO;

import java.util.List;

public interface JobScrapService {

    void scrap(Long userId, Long jobPostId);

    void unscrap(Long userId, Long jobPostId);

    boolean isScrapped(Long userId, Long jobPostId);

    boolean isScrapped(User user, Long jobPostId);

    // 마이페이지용: 로그인 사용자의 채용 찜 목록 조회
    List<JobScrapDTO> getScrapList(User user);

    List<Long> getScrappedJobIdsByUser(User user);

    void cancelScrap(Long jobId, User user);

}
