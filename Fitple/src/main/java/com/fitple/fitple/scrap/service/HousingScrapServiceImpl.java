package com.fitple.fitple.scrap.service;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.base.user.repository.UserRepository;
import com.fitple.fitple.scrap.domain.HousingScrap;
import com.fitple.fitple.scrap.dto.HousingScrapDTO;
import com.fitple.fitple.scrap.dto.HousingScrapRequest;
import com.fitple.fitple.scrap.repository.HousingScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HousingScrapServiceImpl implements HousingScrapService {

    private final HousingScrapRepository housingScrapRepository;
    private final UserRepository userRepository;

    @Override
    public void addScrap(Long userId, HousingScrapRequest requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        housingScrapRepository.findByUserAndPropertyId(user, requestDto.getPropertyId())
                .ifPresent(scrap -> {
                    throw new IllegalStateException("이미 찜한 매물입니다.");
                });

        // ==================================================================
        // ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ 이 부분이 완성된 코드입니다 ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
        HousingScrap newScrap = HousingScrap.builder()
                .user(user)
                .propertyId(requestDto.getPropertyId())
                .housingInfoId(requestDto.getHousingInfoId())
                .hsmpSn(requestDto.getHsmpSn())
                .brtcCode(requestDto.getBrtcCode())
                .signguCode(requestDto.getSignguCode())
                .brtcNm(requestDto.getBrtcNm())
                .signguNm(requestDto.getSignguNm())
                .insttNm(requestDto.getInsttNm())
                .hsmpNm(requestDto.getHsmpNm())
                .rnAdres(requestDto.getRnAdres())
                .competDe(requestDto.getCompetDe())
                .hshldCo(requestDto.getHshldCo())
                .suplyTyNm(requestDto.getSuplyTyNm())
                .styleNm(requestDto.getStyleNm())
                .suplyPrvuseAr(requestDto.getSuplyPrvuseAr())
                .suplyCmnuseAr(requestDto.getSuplyCmnuseAr())
                .houseTyNm(requestDto.getHouseTyNm())
                .heatMthdDetailNm(requestDto.getHeatMthdDetailNm())
                .buldStleNm(requestDto.getBuldStleNm())
                .elvtrInstlAtNm(requestDto.getElvtrInstlAtNm())
                .parkngCo(requestDto.getParkngCo())
                .bassRentGtn(requestDto.getBassRentGtn())
                .bassMtRntchrg(requestDto.getBassMtRntchrg())
                .bassCnvrsGtnLmt(requestDto.getBassCnvrsGtnLmt())
                .msg(requestDto.getMsg())
                .isScrapped(true)
                .build();
        // ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
        // ==================================================================

        housingScrapRepository.save(newScrap);
    }

    @Override
    public void removeScrap(Long userId, Long propertyId) {
        housingScrapRepository.deleteByUserIdAndPropertyId(userId, propertyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getScrapPropertyIds(Long userId) {
        return housingScrapRepository.findPropertyIdsByUserIdAndIsScrappedTrue(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HousingScrapDTO> getScrapList(User user) {
        return housingScrapRepository.findByUserAndIsScrappedTrue(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private HousingScrapDTO convertToDTO(HousingScrap scrap) {
        return new HousingScrapDTO(
                scrap.getPropertyId(),
                scrap.getHsmpNm(),
                scrap.getRnAdres(),
                scrap.getHouseTyNm(),
                scrap.getHousingInfoId(),
                scrap.getBrtcNm(),
                scrap.getSignguNm(),
                scrap.getHshldCo(),
                scrap.getSuplyTyNm(),
                scrap.getSuplyPrvuseAr(),
                scrap.getSuplyCmnuseAr(),
                scrap.getHeatMthdDetailNm(),
                scrap.getElvtrInstlAtNm(),
                scrap.getParkngCo(),
                scrap.getBassRentGtn(),
                scrap.getBassMtRntchrg(),
                scrap.getMsg()
        );
    }
}