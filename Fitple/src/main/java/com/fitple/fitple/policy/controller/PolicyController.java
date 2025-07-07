package com.fitple.fitple.policy.controller;

import com.fitple.fitple.policy.dto.YouthPolicy;
import com.fitple.fitple.policy.dto.YouthPolicyResponse;
import com.fitple.fitple.policy.service.PolicyService;
import com.fitple.fitple.common.dto.PageRequestDTO;
import com.fitple.fitple.common.dto.PageResponseDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping("/policy/list")
    public String policyList(PageRequestDTO requestDTO, Model model) {
        int page = requestDTO.getPage();    // 기본 1
        int size = requestDTO.getSize();    // 기본 10
        String[] zipCds = requestDTO.getZipCds();  // 지역 다중 선택 체크박스

        // zipCd 파라미터는 "11000,26000,..." 형식으로 구성
        String zipCdParam = (zipCds != null && zipCds.length > 0)
                ? String.join(",", zipCds)
                : null;

        YouthPolicyResponse response = policyService.getPolicies(page, size, zipCdParam);

        List<YouthPolicy> policyList = response != null ? response.getResult().getYouthPolicyList() : null;
        int totalCount = response != null ? response.getResult().getPagging().getTotCount() : 0;

        PageResponseDTO<YouthPolicy> pageResponse = new PageResponseDTO<>(requestDTO, policyList, totalCount);

        model.addAttribute("policies", policyList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("responseDTO", pageResponse);
        model.addAttribute("zipCds", zipCds); // 체크박스 선택 유지용
        model.addAttribute("zipCdsParam", zipCds != null ?
                java.util.Arrays.stream(zipCds).map(cd -> "&zipCds=" + cd).collect(java.util.stream.Collectors.joining()) : "");
        model.addAttribute("keyword", requestDTO.getKeyword());

        return "/policy/list";
    }

    @GetMapping("/policy/detail")
    public String policyDetail(String plcyNo,
                               @RequestParam(required = false) Integer page,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String[] zipCds,
                               Model model) {
        var response = policyService.getPolicyDetail(plcyNo);
        var policy = response.getResult().getYouthPolicyList().get(0);

        model.addAttribute("policy", policy);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("zipCds", zipCds);

        return "/policy/detail";
    }

}
