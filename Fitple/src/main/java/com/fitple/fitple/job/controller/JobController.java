package com.fitple.fitple.job.controller;

import com.fitple.fitple.common.dto.PageRequestDTO;
import com.fitple.fitple.common.dto.PageResponseDTO;
import com.fitple.fitple.job.dto.JobDetailDTO;
import com.fitple.fitple.job.dto.JobListDTO;
import com.fitple.fitple.job.service.JobService;
import com.fitple.fitple.scrap.service.JobScrapService;
import com.fitple.fitple.base.user.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.propertyeditors.CustomNumberEditor;

import java.util.List;

@Controller
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final JobScrapService jobScrapService; // ✅ 찜 서비스 추가

    // 숫자 파라미터 바인딩 처리 (빈 값 허용)
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    // 채용 전체 목록 조회 (JSON 응답)
    @GetMapping("/list")
    public List<JobListDTO> getJobList() {
        return jobService.getList();
    }

    // 단건 상세 조회 (JSON 응답용)
    @GetMapping("/{id}")
    public JobDetailDTO getJobDetail(@PathVariable Long id) {
        return jobService.getDetail(id);
    }

    // 필터 검색 (지역/NCS/연봉)
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

    // 혼합 검색 + 페이징 (PageRequestDTO 활용)
    @GetMapping("/search/advanced")
    public PageResponseDTO<JobListDTO> advancedSearch(PageRequestDTO requestDTO) {
        return jobService.advancedSearch(requestDTO);
    }

    // 테스트용 리스트 페이지 (페이징 포함)
    @GetMapping("/test")
    public String jobTestPage(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<JobListDTO> response = jobService.advancedSearch(pageRequestDTO);
        model.addAttribute("pageResponse", response);
        return "job/job_test";
    }

    // 상세 페이지 (job_test에서 진입) + 찜 여부 확인
    @GetMapping("/test/{id}")
    public String jobDetailTest(@PathVariable Long id,
                                @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                Model model) {

        JobDetailDTO dto = jobService.getDetail(id);
        model.addAttribute("job", dto);
        model.addAttribute("requestDTO", requestDTO);

        boolean isScrapped = false;
        if (userDetails != null) {
            isScrapped = jobScrapService.isScrapped(userDetails.getUser(), id);
        }
        model.addAttribute("isScrapped", isScrapped);

        return "job/job_detail_test";
    }

    // 상세 페이지 (일반 상세 뷰) + 찜 여부 확인
    @GetMapping("/detail/{id}")
    public String jobDetail(@PathVariable("id") Long id,
                            @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
                            @AuthenticationPrincipal CustomUserDetails userDetails,
                            Model model) {

        JobDetailDTO dto = jobService.getDetail(id);
        model.addAttribute("job", dto);
        model.addAttribute("requestDTO", pageRequestDTO);

        boolean isScrapped = false;
        if (userDetails != null) {
            isScrapped = jobScrapService.isScrapped(userDetails.getUser(), id);
        }
        model.addAttribute("isScrapped", isScrapped);

        return "job/job_detail_test";
    }
}
