package com.fitple.fitple.admin.controller;

import com.fitple.fitple.base.user.dto.UserDTO;
import com.fitple.fitple.base.user.service.UserService;
import com.fitple.fitple.common.dto.PageRequestDTO;
import com.fitple.fitple.notice.dto.NoticeDTO;
import com.fitple.fitple.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    public final UserService userService;
    private final NoticeService noticeService;

    @GetMapping("/member")
    public String getUserList(Model model, PageRequestDTO pageRequestDTO) {
        model.addAttribute("responseDTO",userService.getUserList(pageRequestDTO));
        return "admin/list";
    }
    @DeleteMapping("/member")
    public String deleteUser(UserDTO userDTO) {
        userService.delete(userDTO.getEmail());
        return "redirect:/admin/member/";
    }

    @GetMapping("/notice/{id}/edit")
    public String editNotice(@PathVariable Long id, PageRequestDTO pageRequestDTO, @AuthenticationPrincipal UserDetails userDetails, Model model){
        model.addAttribute("notice",noticeService.getNotice(id));
        return "notice/edit";
    }

    @GetMapping("/notice/register")
    public String registerNotice(){
        return "notice/register";
    }
    @PostMapping("/notice/register")
    public String registerNotice(NoticeDTO dto){
        Long id = noticeService.add(dto);
        return "redirect:/notice/"+id+"/read";
    }
    @PostMapping("/notice/modify")
    public String modifyNotice(NoticeDTO dto){
        noticeService.modify(dto);
        return "redirect:/notice/"+dto.getId()+"/read";
    }

    @PostMapping("/notice/remove")
    public String removeNotice(Long id){
        noticeService.remove(id);
        return "redirect:/notice";
    }
}
