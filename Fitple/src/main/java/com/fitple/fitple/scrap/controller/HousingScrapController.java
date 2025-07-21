package com.fitple.fitple.scrap.controller;

import com.fitple.fitple.base.user.security.CustomUserDetails;
import com.fitple.fitple.scrap.dto.HousingScrapRequest;
import com.fitple.fitple.scrap.service.HousingScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scrap")
public class HousingScrapController {

    private final HousingScrapService housingScrapService;

    /**
     * 사용자 스크랩 추가
     * JavaScript의 fetch('/scrap/add', ...) 요청을 처리합니다.
     */
    @PostMapping("/add")
    public ResponseEntity<String> addScrap(@RequestBody HousingScrapRequest housingScrapRequest,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        Long userId = userDetails.getUser().getId();

        // 서비스 계층으로 DTO 객체 자체를 전달
        housingScrapService.addScrap(userId, housingScrapRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("찜하기가 완료되었습니다.");
    }

    /**
     * 사용자 스크랩 해제 (삭제)
     * JavaScript의 fetch('/scrap/delete/{propertyId}', ...) 요청을 처리합니다.
     */
    @DeleteMapping("/delete/{propertyId}")
    public ResponseEntity<Void> removeScrap(@PathVariable Long propertyId,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userDetails.getUser().getId();

        // propertyId와 userId를 기준으로 스크랩 삭제
        housingScrapService.removeScrap(userId, propertyId);

        // 성공적으로 삭제되면 204 No Content 상태 코드 반환
        return ResponseEntity.noContent().build();
    }

    /**
     * 로그인된 사용자의 찜한 주택 property_id 목록 조회
     * JavaScript의 fetch('/scrap/list') 요청을 처리합니다.
     */
    @GetMapping("/list")
    public ResponseEntity<List<Long>> getScrapList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Long userId = userDetails.getUser().getId();

        // 서비스에서 property_id 목록을 가져와 반환
        List<Long> scrapPropertyIds = housingScrapService.getScrapPropertyIds(userId);

        return ResponseEntity.ok(scrapPropertyIds);
    }
}