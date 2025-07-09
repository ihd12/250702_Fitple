package com.fitple.fitple.scrap.controller;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.base.user.security.CustomUserDetails;
import com.fitple.fitple.scrap.domain.HousingScrap;
import com.fitple.fitple.scrap.dto.HousingScrapRequest;
import com.fitple.fitple.scrap.service.HousingScrapService;
import com.fitple.fitple.scrap.dto.HousingScrapDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/scrap")
public class HousingScrapController {

    private final HousingScrapService housingScrapService;

    @Autowired
    public HousingScrapController(HousingScrapService housingScrapService) {
        this.housingScrapService = housingScrapService;
    }

    /**
     * 사용자 스크랩 추가
     */
    @PostMapping("/add/{housingInfoId}")
    @ResponseStatus(HttpStatus.CREATED)  // 성공적으로 생성되면 201 상태 코드 반환
    public HousingScrap addScrap(@PathVariable Long housingInfoId,
                                 @RequestBody HousingScrapRequest housingScrapRequest, // @RequestBody 사용
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 로그인된 사용자 확인
        if (userDetails == null || userDetails.getUser() == null) {
            throw new RuntimeException("로그인된 사용자가 없습니다.");
        }

        Long userId = userDetails.getUser().getId();

        // housingScrapRequest에서 파라미터 가져오기
        Long hsmpSn = housingScrapRequest.getHsmpSn();
        String brtcCode = housingScrapRequest.getBrtcCode();
        String signguCode = housingScrapRequest.getSignguCode();
        String brtcNm = housingScrapRequest.getBrtcNm();
        String signguNm = housingScrapRequest.getSignguNm();

        // HousingScrap 서비스에서 스크랩 추가 메서드를 호출, 요청 파라미터와 함께 전달
        return housingScrapService.addScrap(userId, housingInfoId, hsmpSn, brtcCode, signguCode, brtcNm, signguNm);  // housing_info_id와 추가 파라미터들 사용
    }


    /**
     * 사용자 스크랩 해제
     */
    @PostMapping("/remove/{housingInfoId}")
    public void removeScrap(@PathVariable Long housingInfoId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 로그인된 사용자 확인
        if (userDetails == null || userDetails.getUser() == null) {
            throw new RuntimeException("로그인된 사용자가 없습니다.");
        }

        Long userId = userDetails.getUser().getId();
        housingScrapService.removeScrap(userId, housingInfoId);  // housing_info_id를 사용
    }


    /**
     * 로그인된 사용자의 찜한 주택 목록 조회
     */
    @GetMapping("/list")
    public List<HousingScrapDTO> getScrapList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // 로그인된 사용자 확인
        if (userDetails == null || userDetails.getUser() == null) {
            throw new RuntimeException("로그인된 사용자가 없습니다.");
        }

        User user = userDetails.getUser();
        return housingScrapService.getScrapList(user);  // housingScrapService에서 housing_info_id를 사용
    }
}
