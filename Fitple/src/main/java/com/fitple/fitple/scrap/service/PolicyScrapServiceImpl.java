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
    public boolean scrap(Long userId, List<String> policyIds, List<String> policyNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        boolean saved = false;

        for (int i = 0; i < policyIds.size(); i++) {
            String policyId = policyIds.get(i);
            String policyName = policyNames.get(i);

            if (!repository.existsByUserAndPolicyId(user, policyId)) {
                PolicyScrap scrap = new PolicyScrap();
                scrap.setUser(user);
                scrap.setPolicyId(policyId);
                scrap.setPolicyName(policyName);
                repository.save(scrap);
                saved = true;
            }
        }

        return saved;
    }

    @Override
    public void cancelScrap(Long userId, String policyId) {
        repository.deleteByUserIdAndPolicyId(userId, policyId);
    }

    @Override
    public boolean isScrapped(Long userId, String policyId) {
        return repository.existsByUserIdAndPolicyId(userId, policyId);
    }

    @Override
    public boolean isScrapped(User user, String policyId) {
        return repository.existsByUserAndPolicyId(user, policyId);
    }

    @Override
    public Set<String> getScrappedPlcyNoSet(User user, List<String> policyIds) {
        return repository.findByUserAndPolicyIdIn(user, policyIds)
                .stream()
                .map(PolicyScrap::getPolicyId)
                .collect(Collectors.toSet());
    }

    @Override
    public List<PolicyScrapDTO> getScrapList(User user) {
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
