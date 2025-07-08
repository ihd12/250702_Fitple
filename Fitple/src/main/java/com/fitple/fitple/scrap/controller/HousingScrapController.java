package com.fitple.fitple.scrap.controller;

import com.fitple.fitple.base.user.security.CustomUserDetails;
import com.fitple.fitple.scrap.domain.HousingScrap;
import com.fitple.fitple.scrap.service.HousingScrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;


@RestController
@RequestMapping("/scrap")
public class HousingScrapController {

    private final HousingScrapService housingScrapService;

    @Autowired
    public HousingScrapController(HousingScrapService housingScrapService) {
        this.housingScrapService = housingScrapService;
    }

    // 스크랩 추가
    @PostMapping("/add/{housingId}")
    public HousingScrap addScrap(@PathVariable Long housingId, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        if (userDetails == null || userDetails.getUser() == null) {
            throw new RuntimeException("로그인된 사용자가 없습니다.");
        }

        Long userId = userDetails.getUser().getId();  // userId만 추출
        return housingScrapService.addScrap(userId, housingId);
    }

    // 스크랩 해제
    @PostMapping("/remove/{housingId}")
    public HousingScrap removeScrap(@PathVariable Long housingId, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        if (userDetails == null || userDetails.getUser() == null) {
            throw new RuntimeException("로그인된 사용자가 없습니다.");
        }

        Long userId = userDetails.getUser().getId();  // userId만 추출
        return housingScrapService.removeScrap(userId, housingId);
    }

    @GetMapping("/list")
    public List<Long> getScrapList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getUser() == null) {
            throw new RuntimeException("로그인된 사용자가 없습니다.");
        }

        Long userId = userDetails.getUser().getId();

        return housingScrapService.getScrappedHousingList(userId).stream()
                .map(dto -> dto.getId()) // HousingInfoDTO.getId()를 사용해야 합니다
                .collect(Collectors.toList());
    }


}

