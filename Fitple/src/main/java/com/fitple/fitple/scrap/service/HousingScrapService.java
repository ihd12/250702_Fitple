package com.fitple.fitple.scrap.service;

import com.fitple.fitple.housing.domain.HousingInfo;
import com.fitple.fitple.housing.repository.HousingInfoRepository;
import com.fitple.fitple.scrap.domain.HousingScrap;
import com.fitple.fitple.scrap.dto.HousingInfoDTO;
import com.fitple.fitple.scrap.dto.HousingScrapDTO;
import com.fitple.fitple.scrap.repository.HousingScrapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fitple.fitple.base.user.domain.User;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class HousingScrapService {

    private final HousingScrapRepository housingScrapRepository;
    private final HousingInfoRepository housingInfoRepository;

    @Autowired
    public HousingScrapService(HousingScrapRepository housingScrapRepository,
                               HousingInfoRepository housingInfoRepository) {
        this.housingScrapRepository = housingScrapRepository;
        this.housingInfoRepository = housingInfoRepository;
    }

    // 스크랩 추가
    public HousingScrap addScrap(Long userId, Long housingInfoId) {
        HousingScrap existing = housingScrapRepository.findByUserIdAndHousingInfoId(userId, housingInfoId);

        if (existing != null) {
            existing.setIsScrapped(true);
            return housingScrapRepository.save(existing);
        }

        HousingScrap scrap = new HousingScrap();
        scrap.setUserId(userId);
        scrap.setHousingInfoId(housingInfoId);
        scrap.setIsScrapped(true);
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
    public List<HousingInfoDTO> getScrappedHousingList(Long userId) {
        List<HousingScrap> scraps = housingScrapRepository.findByUserIdAndIsScrappedTrue(userId);

        return scraps.stream()
                .map(scrap -> housingInfoRepository.findById(scrap.getHousingInfoId()).orElse(null))
                .filter(info -> info != null)
                .map(info -> {
                    HousingInfoDTO dto = new HousingInfoDTO();
                    dto.setId(info.getHouseId());  // getId() → getHouseId()로 수정
                    dto.setHsmpNm(info.getHsmpNm());
                    dto.setRnAdres(info.getRnAdres());
                    dto.setHouseTyNm(info.getHouseTyNm());
                    dto.setSuplyPrvuseAr(info.getSuplyPrvuseAr());  // 타입 long으로 그대로
                    dto.setBassRentGtn(info.getBassRentGtn());
                    dto.setBassMtRntchrg(info.getBassMtRntchrg());
                    dto.setInsttNm(info.getInsttNm());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // MyPage용 DTO 반환 메서드
    public List<HousingScrapDTO> getScrapList(User user) {
        List<HousingScrap> scraps = housingScrapRepository.findByUserIdAndIsScrappedTrue(user.getId());

        List<HousingScrapDTO> dtoList = scraps.stream()
                .map(scrap -> {
                    HousingInfo info = housingInfoRepository.findById(scrap.getHousingInfoId()).orElse(null);
                    if (info == null) return null;
                    return new HousingScrapDTO(
                            info.getHsmpNm(),
                            info.getRnAdres(),
                            info.getHouseTyNm()
                    );
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());

        System.out.println("주거 스크랩 리스트 개수: " + dtoList.size());
        dtoList.forEach(dto -> System.out.println(dto));

        return dtoList;
    }

}
