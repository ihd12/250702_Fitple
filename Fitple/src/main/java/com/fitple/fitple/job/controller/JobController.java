package com.fitple.fitple.job.controller;

import com.fitple.fitple.common.dto.PageRequestDTO;
import com.fitple.fitple.common.dto.PageResponseDTO;
import com.fitple.fitple.job.dto.JobDetailDTO;
import com.fitple.fitple.job.dto.JobListDTO;
import com.fitple.fitple.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;


import java.util.List;

@Controller
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    private final JobService jobService;

    // 전체 목록 조회
    @GetMapping("/list")
    public List<JobListDTO> getJobList() {
        return jobService.getList();
    }

    // 상세 조회
    @GetMapping("/{id}")
    public JobDetailDTO getJobDetail(@PathVariable Long id) {
        return jobService.getDetail(id);
    }

    // 필터 검색
    @GetMapping("/search")
    public List<JobListDTO> searchJobs(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String ncs,
            @RequestParam(required = false) Integer salary
    ) {
        return jobService.searchJobs(location, ncs, salary);
    }

    // 키워드 검색
    @GetMapping("/search/keyword")
    public List<JobListDTO> searchByKeyword(@RequestParam String keyword) {
        return jobService.searchByKeyword(keyword);
    }

    // 혼합 검색 + 페이징 (PageRequestDTO 기반)
    @GetMapping("/search/advanced")
    public PageResponseDTO<JobListDTO> advancedSearch(PageRequestDTO requestDTO) {
        return jobService.advancedSearch(requestDTO);
    }

    @GetMapping("/test")
    public String jobTestPage(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<JobListDTO> response = jobService.advancedSearch(pageRequestDTO);
        model.addAttribute("pageResponse", response);
        return "job/job_test";
    }

    @GetMapping("/test/{id}")
    public String jobDetailTest(@PathVariable Long id,
                                @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                                Model model) {
        JobDetailDTO dto = jobService.getDetail(id);
        model.addAttribute("job", dto);
        model.addAttribute("requestDTO", requestDTO);
        return "job/job_detail_test";
    }

    @GetMapping("/detail/{id}")
    public String jobDetail(@PathVariable("id") Long id,
                            @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
                            Model model) {
        JobDetailDTO dto = jobService.getDetail(id);
        model.addAttribute("job", dto);
        model.addAttribute("requestDTO", pageRequestDTO); // ✅ 목록 복귀용 데이터 전달
        return "job/job_detail_test";
    }
}
