package com.fitple.fitple.scrap.service;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.base.user.repository.UserRepository;
import com.fitple.fitple.scrap.domain.PolicyScrap;
import com.fitple.fitple.scrap.dto.PolicyScrapDTO;
import com.fitple.fitple.scrap.repository.PolicyScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PolicyScrapServiceImpl implements PolicyScrapService {

    private final PolicyScrapRepository repository;
    private final UserRepository userRepository;

    @Override
    public void scrap(Long userId, String policyId, String policyName) {
        // User 객체 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 중복된 찜이 존재하는지 확인
        if (repository.existsByUserAndPolicyId(user, policyId)) {
            // 이미 찜한 정책이므로 저장하지 않음
            return;
        }

        // 새로운 PolicyScrap 객체 생성
        PolicyScrap scrap = new PolicyScrap();
        scrap.setUser(user);
        scrap.setPolicyId(policyId);
        scrap.setPolicyName(policyName);

        // 새로 찜하기 저장
        repository.save(scrap);
    }


    @Override
    public void cancelScrap(Long userId, String policyId) {
        repository.deleteByUserIdAndPolicyId(userId, policyId);  // 찜 취소
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isScrapped(Long userId, String policyId) {
        // userId로 확인
        return repository.existsByUserIdAndPolicyId(userId, policyId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isScrapped(User user, String policyId) {
        // User 객체와 policyId로 확인
        return repository.existsByUserAndPolicyId(user, policyId);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> getScrappedPlcyNoSet(User user, List<String> policyIds) {
        return repository.findByUserAndPolicyIdIn(user, policyIds)
                .stream()
                .map(PolicyScrap::getPolicyId)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyScrapDTO> getScrapList(User user) {
        // 페이징 처리 없이 모든 데이터 조회
        List<PolicyScrap> scraps = repository.findByUser(user);

        return scraps.stream()
                .map(s -> {
                    PolicyScrapDTO dto = new PolicyScrapDTO();
                    dto.setId(s.getId());
                    dto.setPolicyId(s.getPolicyId());
                    dto.setPolicyName(s.getPolicyName());
                    dto.setCreatedAt(s.getCreatedAt());
                    dto.setUpdatedAt(s.getUpdatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
