package com.fitple.fitple.policy.controller;

import com.fitple.fitple.policy.dto.YouthPolicy;
import com.fitple.fitple.policy.dto.YouthPolicyResponse;
import com.fitple.fitple.policy.service.PolicyService;
import com.fitple.fitple.scrap.service.PolicyScrapService;
import com.fitple.fitple.base.user.security.CustomUserDetails;
import com.fitple.fitple.common.dto.PageRequestDTO;
import com.fitple.fitple.common.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;
    private final PolicyScrapService policyScrapService;

    @GetMapping("/policy/list")
    public String policyList(PageRequestDTO requestDTO,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             @ModelAttribute("message") String message,
                             Model model) {

        int page = requestDTO.getPage();
        int size = requestDTO.getSize();
        String[] zipCds = requestDTO.getZipCds();
        String zipCdParam = (zipCds != null && zipCds.length > 0) ? String.join(",", zipCds) : null;

        YouthPolicyResponse response = policyService.getPolicies(page, size, zipCdParam);

        List<YouthPolicy> policyList = policyService.getFilteredPolicies(requestDTO);
        int totalCount = policyList.size();

        Set<String> scrappedSet = Collections.emptySet();
        if (userDetails != null && !policyList.isEmpty()) {
            List<String> plcyNos = policyList.stream()
                    .map(YouthPolicy::getPlcyNo)
                    .collect(Collectors.toList());
            scrappedSet = policyScrapService.getScrappedPlcyNoSet(
                    userDetails.getUser(), plcyNos);
        }

        PageResponseDTO<YouthPolicy> pageResponse =
                new PageResponseDTO<>(requestDTO, policyList, totalCount);

        model.addAttribute("policies", policyList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("responseDTO", pageResponse);
        model.addAttribute("scrappedSet", scrappedSet);

        // 지역 필터
        model.addAttribute("zipCds", zipCds != null ? zipCds : new String[0]);
        model.addAttribute("zipCdsParam", zipCds != null
                ? java.util.Arrays.stream(zipCds)
                .map(cd -> "&zipCds=" + cd)
                .collect(Collectors.joining())
                : "");

        // 키워드 필터
        model.addAttribute("keywords", requestDTO.getKeywords());
        model.addAttribute("keywordsParam", requestDTO.getKeywords() != null
                ? requestDTO.getKeywords().stream()
                .map(kw -> "&keywords=" + kw)
                .collect(Collectors.joining())
                : "");

        model.addAttribute("keyword", requestDTO.getKeyword());
        model.addAttribute("zipCodeMap", policyService.getZipCodeMap());
        model.addAttribute("keywordList", policyService.getKeywordList());

        return "/policy/list";
    }

    @GetMapping("/policy/detail")
    public String policyDetail(@RequestParam String plcyNo,
                               @RequestParam(required = false) Integer page,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String[] zipCds,
                               @RequestParam(required = false) List<String> keywords,
                               @AuthenticationPrincipal CustomUserDetails userDetails,
                               Model model) {

        var response = policyService.getPolicyDetail(plcyNo);

        if (response == null ||
                response.getResult() == null ||
                response.getResult().getYouthPolicyList() == null ||
                response.getResult().getYouthPolicyList().isEmpty()) {

            model.addAttribute("message", "해당 정책 정보를 불러올 수 없습니다.");
            return "/policy/detail";
        }

        var policy = response.getResult().getYouthPolicyList().get(0);
        model.addAttribute("policy", policy);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("zipCds", zipCds);
        model.addAttribute("keywords", keywords);

        // ✅ 필터 파라미터를 문자열로 가공해서 전달
        String zipCdsParam = (zipCds != null && zipCds.length > 0)
                ? java.util.Arrays.stream(zipCds)
                .map(cd -> "&zipCds=" + cd)
                .collect(Collectors.joining())
                : "";

        String keywordsParam = (keywords != null && !keywords.isEmpty())
                ? keywords.stream()
                .map(k -> "&keywords=" + k)
                .collect(Collectors.joining())
                : "";

        model.addAttribute("zipCdsParam", zipCdsParam);
        model.addAttribute("keywordsParam", keywordsParam);

        boolean isScrapped = false;
        if (userDetails != null) {
            isScrapped = policyScrapService.isScrapped(userDetails.getUser(), plcyNo);
        }
        model.addAttribute("isScrapped", isScrapped);

        return "/policy/detail";
    }

}
