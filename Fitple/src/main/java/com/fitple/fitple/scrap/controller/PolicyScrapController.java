package com.fitple.fitple.scrap.controller;

import com.fitple.fitple.base.user.security.CustomUserDetails;
import com.fitple.fitple.scrap.service.PolicyScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/policy/scrap/api")  // URL을 다르게 설정
public class PolicyScrapController {

    private final PolicyScrapService scrapService;

    // 여러 개의 정책을 찜하는 메서드
    @PostMapping
    public ResponseEntity<Void> scrap(@RequestParam List<String> plcyNos,  // 정책 ID 목록
                                      @RequestParam List<String> plcyNms,  // 정책 이름 목록
                                      @AuthenticationPrincipal CustomUserDetails user) {
        // 서비스 호출
        scrapService.scrap(user.getUser().getId(), plcyNos, plcyNms);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{plcyNo}")
    public ResponseEntity<Void> cancelScrap(@PathVariable String plcyNo,
                                            @AuthenticationPrincipal CustomUserDetails user) {
        scrapService.cancelScrap(user.getUser().getId(), plcyNo);
        return ResponseEntity.ok().build();
    }
}
