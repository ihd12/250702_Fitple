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

        // 추가된 필드들 가져오기
        String insttNm = housingScrapRequest.getInsttNm(); // 기관 명
        String hsmpNm = housingScrapRequest.getHsmpNm(); // 단지 명
        String rnAdres = housingScrapRequest.getRnAdres(); // 도로명 주소
        String competDe = housingScrapRequest.getCompetDe(); // 준공 일자
        Integer hshldCo = housingScrapRequest.getHshldCo(); // 세대 수
        String suplyTyNm = housingScrapRequest.getSuplyTyNm(); // 공급 유형 명
        String styleNm = housingScrapRequest.getStyleNm(); // 형 명
        Double suplyPrvuseAr = housingScrapRequest.getSuplyPrvuseAr(); // 공급 전용 면적
        Double suplyCmnuseAr = housingScrapRequest.getSuplyCmnuseAr(); // 공급 공용 면적
        String houseTyNm = housingScrapRequest.getHouseTyNm(); // 주택 유형 명
        String heatMthdDetailNm = housingScrapRequest.getHeatMthdDetailNm(); // 난방 방식
        String buldStleNm = housingScrapRequest.getBuldStleNm(); // 건물 형태
        String elvtrInstlAtNm = housingScrapRequest.getElvtrInstlAtNm(); // 승강기 설치여부
        Integer parkngCo = housingScrapRequest.getParkngCo(); // 주차수
        Long bassRentGtn = housingScrapRequest.getBassRentGtn(); // 기본 임대보증금
        Long bassMtRntchrg = housingScrapRequest.getBassMtRntchrg(); // 기본 월임대료
        Long bassCnvrsGtnLmt = housingScrapRequest.getBassCnvrsGtnLmt(); // 기본 전환보증금
        String msg = housingScrapRequest.getMsg(); // 메시지

        // HousingScrap 서비스에서 스크랩 추가 메서드를 호출, 요청 파라미터와 함께 전달
        return housingScrapService.addScrap(userId, housingInfoId, hsmpSn, brtcCode, signguCode, brtcNm, signguNm,
                insttNm, hsmpNm, rnAdres, competDe, hshldCo, suplyTyNm, styleNm,
                suplyPrvuseAr, suplyCmnuseAr, houseTyNm, heatMthdDetailNm,
                buldStleNm, elvtrInstlAtNm, parkngCo, bassRentGtn, bassMtRntchrg,
                bassCnvrsGtnLmt, msg);  // 모든 파라미터 전달
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
