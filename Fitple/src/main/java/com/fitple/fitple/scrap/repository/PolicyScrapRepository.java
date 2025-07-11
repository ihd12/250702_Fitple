package com.fitple.fitple.scrap.repository;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.scrap.domain.PolicyScrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PolicyScrapRepository extends JpaRepository<PolicyScrap, Long> {

    boolean existsByUserIdAndPolicyId(Long userId, String policyId);

    boolean existsByUserAndPolicyId(User user, String policyId);

    Optional<PolicyScrap> findByUserIdAndPolicyId(Long userId, String policyId);

    void deleteByUserIdAndPolicyId(Long userId, String policyId);

    List<PolicyScrap> findByUserAndPolicyIdIn(User user, List<String> policyIds);

    List<PolicyScrap> findByUser(User user);
}
