package com.fitple.fitple.scrap.controller;

import com.fitple.fitple.base.user.security.CustomUserDetails;
import com.fitple.fitple.scrap.service.PolicyScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/policy/{plcyNo}/scrap")
public class PolicyScrapController {

    private final PolicyScrapService scrapService;

    @PostMapping
    public ResponseEntity<Void> scrap(@PathVariable String plcyNo,
                                      @RequestParam String plcyNm,
                                      @AuthenticationPrincipal CustomUserDetails user) {
//        System.out.println("==== [정책 찜 요청] ====");
//        System.out.println("User ID: " + user.getUser().getId());
//        System.out.println("Policy ID: " + plcyNo);
//        System.out.println("Policy Name: " + plcyNm);

        scrapService.scrap(user.getUser().getId(), plcyNo, plcyNm);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping
    public ResponseEntity<Void> cancelScrap(@PathVariable String plcyNo,
                                            @AuthenticationPrincipal CustomUserDetails user) {
        scrapService.cancelScrap(user.getUser().getId(), plcyNo);
        return ResponseEntity.ok().build();
    }
}
