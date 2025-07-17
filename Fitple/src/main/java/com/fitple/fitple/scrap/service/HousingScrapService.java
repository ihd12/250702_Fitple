package com.fitple.fitple.scrap.service;

import com.fitple.fitple.scrap.domain.HousingScrap;
import com.fitple.fitple.scrap.dto.HousingScrapDTO;
import com.fitple.fitple.scrap.repository.HousingScrapRepository;
import com.fitple.fitple.base.user.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HousingScrapService {

    private final HousingScrapRepository housingScrapRepository;

    @Autowired
    public HousingScrapService(HousingScrapRepository housingScrapRepository) {
        this.housingScrapRepository = housingScrapRepository;
    }

    // 스크랩 추가
    public HousingScrap addScrap(Long userId, Long housingInfoId, Long hsmpSn, String brtcCode, String signguCode, String brtcNm, String signguNm,
                                 String insttNm, String hsmpNm, String rnAdres, String competDe, Integer hshldCo, String suplyTyNm, String styleNm,
                                 Double suplyPrvuseAr, Double suplyCmnuseAr, String houseTyNm, String heatMthdDetailNm, String buldStleNm,
                                 String elvtrInstlAtNm, Integer parkngCo, Long bassRentGtn, Long bassMtRntchrg, Long bassCnvrsGtnLmt, String msg) {
        HousingScrap existing = housingScrapRepository.findByUserIdAndHousingInfoId(userId, housingInfoId);

        if (existing != null) {
            existing.setIsScrapped(true);
            return housingScrapRepository.save(existing);
        }

        HousingScrap scrap = new HousingScrap();
        scrap.setUserId(userId);
        scrap.setHousingInfoId(housingInfoId);  // housing_info_id만 저장
        scrap.setIsScrapped(true);
        scrap.setHsmpSn(hsmpSn);                // 단지 식별자
        scrap.setBrtcCode(brtcCode);           // 광역시도 코드
        scrap.setSignguCode(signguCode);       // 시군구 코드
        scrap.setBrtcNm(brtcNm);               // 광역시도 명
        scrap.setSignguNm(signguNm);           // 시군구 명

        // 추가된 필드 설정
        scrap.setInsttNm(insttNm);             // 기관 명
        scrap.setHsmpNm(hsmpNm);               // 단지 명
        scrap.setRnAdres(rnAdres);             // 도로명 주소
        scrap.setCompetDe(competDe);           // 준공 일자
        scrap.setHshldCo(hshldCo);             // 세대 수
        scrap.setSuplyTyNm(suplyTyNm);         // 공급 유형 명
        scrap.setStyleNm(styleNm);             // 형 명
        scrap.setSuplyPrvuseAr(suplyPrvuseAr); // 공급 전용 면적
        scrap.setSuplyCmnuseAr(suplyCmnuseAr); // 공급 공용 면적
        scrap.setHouseTyNm(houseTyNm);         // 주택 유형 명
        scrap.setHeatMthdDetailNm(heatMthdDetailNm); // 난방 방식
        scrap.setBuldStleNm(buldStleNm);       // 건물 형태
        scrap.setElvtrInstlAtNm(elvtrInstlAtNm); // 승강기 설치여부
        scrap.setParkngCo(parkngCo);           // 주차수
        scrap.setBassRentGtn(bassRentGtn);     // 기본 임대보증금
        scrap.setBassMtRntchrg(bassMtRntchrg); // 기본 월임대료
        scrap.setBassCnvrsGtnLmt(bassCnvrsGtnLmt); // 기본 전환보증금
        scrap.setMsg(msg);                     // 메시지

        return housingScrapRepository.save(scrap);
    }

    // 스크랩 해제
    public HousingScrap removeScrap(Long userId, Long housingInfoId) {
        HousingScrap existing = housingScrapRepository.findByUserIdAndHousingInfoId(userId, housingInfoId);
        if (existing != null) {
            existing.setIsScrapped(false);
            return housingScrapRepository.save(existing);
        }
        return null;
    }

    // 로그인한 사용자의 찜한 주택 목록
    public List<HousingScrapDTO> getScrapList(User user) {
        List<HousingScrap> scraps = housingScrapRepository.findByUserIdAndIsScrappedTrue(user.getId());

        // HousingScrapDTO로 변환
        return scraps.stream()
                .map(scrap -> new HousingScrapDTO(
                        scrap.getHsmpNm(),                // 단지명
                        scrap.getRnAdres(),               // 도로명 주소
                        scrap.getHouseTyNm(),             // 주택 유형명
                        scrap.getHousingInfoId(),         // housingInfoId 필드 추가

                        scrap.getBrtcNm(),                // 광역시도 명
                        scrap.getSignguNm(),              // 시군구 명
                        scrap.getHshldCo(),               // 세대 수
                        scrap.getSuplyTyNm(),             // 공급 유형 명
                        scrap.getSuplyPrvuseAr(),         // 공급 전용 면적
                        scrap.getSuplyCmnuseAr(),         // 공급 공용 면적
                        scrap.getHeatMthdDetailNm(),      // 난방 방식
                        scrap.getElvtrInstlAtNm(),        // 승강기 설치여부
                        scrap.getParkngCo(),              // 주차수
                        scrap.getBassRentGtn(),           // 기본 임대보증금
                        scrap.getBassMtRntchrg(),         // 기본 월임대료
                        scrap.getMsg()                    // 메시지
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelScrap(Long housingInfoId, User user) {
        housingScrapRepository.deleteByUserIdAndHousingInfoId(user.getId(), housingInfoId);
    }

}
