package com.fitple.fitple.notice.controller;

import com.fitple.fitple.common.dto.PageRequestDTO;
import com.fitple.fitple.notice.domain.Notice;
import com.fitple.fitple.notice.dto.NoticeDTO;
import com.fitple.fitple.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;
    @GetMapping({"","/"})
    public String getNoticeList(PageRequestDTO pageRequestDTO, Model model){
        if(pageRequestDTO == null){
            pageRequestDTO = PageRequestDTO.builder().build();
            System.out.println(pageRequestDTO);
        }
        model.addAttribute("responseDTO", noticeService.getNoticeList(pageRequestDTO));
        return "notice/list";
    }
    @GetMapping("/{id}/read")
    public String readNotice(@PathVariable Long id, PageRequestDTO pageRequestDTO, Model model){
        model.addAttribute("notice",noticeService.getNotice(id));
        noticeService.addViewCount(id);
        return "notice/view";
    }

}
