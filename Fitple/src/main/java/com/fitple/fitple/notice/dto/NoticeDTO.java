package com.fitple.fitple.notice.dto;

import com.fitple.fitple.notice.domain.Notice;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {
    private Long id;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String adminName; // 선택사항: 목록/상세에서 관리자의 이름 출력용

    public static NoticeDTO toDTO(Notice notice){
        return NoticeDTO.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .viewCount(notice.getViewCount())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .adminName(notice.getAdmin() != null ? notice.getAdmin().getNickname() : null) // admin 출력용
                .build();
    }
    public Notice toEntity(){
        return Notice.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .viewCount(this.viewCount)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
