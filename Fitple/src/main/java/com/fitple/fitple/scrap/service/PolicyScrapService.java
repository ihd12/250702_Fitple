package com.fitple.fitple.scrap.service;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.scrap.dto.PolicyScrapDTO;

import java.util.List;
import java.util.Set;

public interface PolicyScrapService {

    void scrap(Long userId, String policyId, String policyName);

    void cancelScrap(Long userId, String policyId);

    boolean isScrapped(Long userId, String policyId);

    boolean isScrapped(User user, String policyId);

    Set<String> getScrappedPlcyNoSet(User user, List<String> policyIds);

    List<PolicyScrapDTO> getScrapList(User user);
}
