package com.fitple.fitple.scrap.controller;

import com.fitple.fitple.base.user.security.CustomUserDetails;
import com.fitple.fitple.scrap.service.PolicyScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/policy/scrap/web")
public class PolicyScrapWebController {

    private final PolicyScrapService scrapService;

    @PostMapping
    public String scrap(@RequestParam String plcyNos,
                        @RequestParam String plcyNms,
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        RedirectAttributes redirectAttributes) {

        if (userDetails != null && plcyNos != null && !plcyNos.isBlank()) {
            boolean result = scrapService.scrap(userDetails.getUser().getId(), List.of(plcyNos), List.of(plcyNms));

            if (result) {
                redirectAttributes.addFlashAttribute("message", "찜 완료!");
            } else {
                redirectAttributes.addFlashAttribute("message", "이미 찜한 정책입니다.");
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "로그인이 필요하거나 잘못된 요청입니다.");
        }

        return "redirect:/policy/list";
    }



    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable String id,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         RedirectAttributes redirectAttributes) {

        System.out.println(">> [CANCEL SCRAP] 정책 ID: " + id);

        if (userDetails != null) {
            scrapService.cancelScrap(userDetails.getUser().getId(), id);
            redirectAttributes.addFlashAttribute("message", "찜이 취소되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
        }

        return "redirect:/policy/detail?plcyNo=" + id;
    }

    @PostMapping("/ajax")
    @ResponseBody
    public String scrapAjax(@RequestParam String plcyNos,
                            @RequestParam String plcyNms,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails != null && plcyNos != null && !plcyNos.isBlank()) {
            boolean result = scrapService.scrap(userDetails.getUser().getId(), List.of(plcyNos), List.of(plcyNms));
            return result ? "찜 완료!" : "이미 찜한 정책입니다.";
        } else {
            return "로그인이 필요하거나 잘못된 요청입니다.";
        }
    }

    @PostMapping("/ajax/{id}/cancel")
    @ResponseBody
    public String cancelAjax(@PathVariable String id,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails != null) {
            scrapService.cancelScrap(userDetails.getUser().getId(), id);
            return "찜이 취소되었습니다.";
        } else {
            return "로그인이 필요합니다.";
        }
    }


}
