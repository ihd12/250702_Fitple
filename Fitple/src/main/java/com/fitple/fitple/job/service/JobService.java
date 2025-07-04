package com.fitple.fitple.job.service;

import com.fitple.fitple.common.dto.PageRequestDTO;
import com.fitple.fitple.common.dto.PageResponseDTO;
import com.fitple.fitple.job.domain.JobDetail;
import com.fitple.fitple.job.domain.JobPost;
import com.fitple.fitple.job.dto.JobDetailDTO;
import com.fitple.fitple.job.dto.JobListDTO;
import com.fitple.fitple.job.repository.JobPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobPostRepository jobPostRepository;

    // 전체 공고 리스트 조회 (페이징/조건 없음)
    public List<JobListDTO> getList() {
        return jobPostRepository.findAll().stream()
                .map(jobPost -> JobListDTO.builder()
                        .id(jobPost.getId())
                        .title(jobPost.getTitle())
                        .orgName(jobPost.getOrgName())
                        .location(jobPost.getLocation())
                        .jobType(jobPost.getJobType())
                        .salary(jobPost.getJobDetail() != null ? jobPost.getJobDetail().getSalary() : null)
                        .ncs(jobPost.getNcs())
                        .startDate(jobPost.getStartDate())
                        .endDate(jobPost.getEndDate())
                        .build()
                ).collect(Collectors.toList());
    }

    // 공고 상세 보기
    public JobDetailDTO getDetail(Long id) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공고가 없습니다."));

        JobDetail detail = jobPost.getJobDetail();

        return JobDetailDTO.builder()
                .id(jobPost.getId())
                .title(jobPost.getTitle())
                .orgName(jobPost.getOrgName())
                .location(jobPost.getLocation())
                .jobType(jobPost.getJobType())
                .ncs(jobPost.getNcs())
                .salary(detail != null ? detail.getSalary() : null)
                .startDate(jobPost.getStartDate())
                .endDate(jobPost.getEndDate())
                .detailUrl(jobPost.getDetailUrl())
                .education(detail != null ? detail.getEducation() : null)
                .requirement(detail != null ? detail.getRequirement() : null)
                .preferred(detail != null ? detail.getPreferred() : null)
                .procedureDetail(detail != null ? detail.getProcedureDetail() : null)
                .advantageDetail(detail != null ? detail.getAdvantageDetail() : null)
                .build();
    }

    // 조건 필터 검색 (location, ncs, salary)
    public List<JobListDTO> searchJobs(String location, String ncs, Integer salary) {
        List<JobPost> results = jobPostRepository.searchJobs(location, ncs, salary);

        return results.stream()
                .map(jobPost -> JobListDTO.builder()
                        .id(jobPost.getId())
                        .title(jobPost.getTitle())
                        .orgName(jobPost.getOrgName())
                        .location(jobPost.getLocation())
                        .jobType(jobPost.getJobType())
                        .salary(jobPost.getJobDetail() != null ? jobPost.getJobDetail().getSalary() : null)
                        .ncs(jobPost.getNcs())
                        .startDate(jobPost.getStartDate())
                        .endDate(jobPost.getEndDate())
                        .build()
                ).collect(Collectors.toList());
    }

    // 제목 기반 키워드 검색
    public List<JobListDTO> searchByKeyword(String keyword) {
        List<JobPost> results = jobPostRepository.searchByKeyword(keyword);

        return results.stream()
                .map(jobPost -> JobListDTO.builder()
                        .id(jobPost.getId())
                        .title(jobPost.getTitle())
                        .orgName(jobPost.getOrgName())
                        .location(jobPost.getLocation())
                        .jobType(jobPost.getJobType())
                        .salary(jobPost.getJobDetail() != null ? jobPost.getJobDetail().getSalary() : null)
                        .ncs(jobPost.getNcs())
                        .startDate(jobPost.getStartDate())
                        .endDate(jobPost.getEndDate())
                        .build()
                ).collect(Collectors.toList());
    }

    // 혼합 조건 검색 + 페이징 + 총 건수 포함 응답
    public PageResponseDTO<JobListDTO> advancedSearch(PageRequestDTO requestDTO) {
        Pageable pageable = PageRequest.of(requestDTO.getPage() - 1, requestDTO.getSize());

        List<JobPost> results = jobPostRepository.advancedSearch(
                requestDTO.getLocation(),
                requestDTO.getNcs(),
                requestDTO.getSalary(),
                requestDTO.getKeyword(),
                pageable
        );

        List<JobListDTO> dtoList = results.stream()
                .map(jobPost -> JobListDTO.builder()
                        .id(jobPost.getId())
                        .title(jobPost.getTitle())
                        .orgName(jobPost.getOrgName())
                        .location(jobPost.getLocation())
                        .jobType(jobPost.getJobType())
                        .salary(jobPost.getJobDetail() != null ? jobPost.getJobDetail().getSalary() : null)
                        .ncs(jobPost.getNcs())
                        .startDate(jobPost.getStartDate())
                        .endDate(jobPost.getEndDate())
                        .build()
                ).collect(Collectors.toList());

        int total = jobPostRepository.advancedSearchCount(
                requestDTO.getLocation(),
                requestDTO.getNcs(),
                requestDTO.getSalary(),
                requestDTO.getKeyword()
        );

        return new PageResponseDTO<>(requestDTO, dtoList, total);

    }
}
