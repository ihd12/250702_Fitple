package com.fitple.fitple.notice.service;

import com.fitple.fitple.base.user.dto.UserDTO;
import com.fitple.fitple.common.dto.PageRequestDTO;
import com.fitple.fitple.common.dto.PageResponseDTO;
import com.fitple.fitple.notice.domain.Notice;
import com.fitple.fitple.notice.dto.NoticeDTO;
import com.fitple.fitple.notice.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public PageResponseDTO<NoticeDTO> getNoticeList(PageRequestDTO pageRequestDTO){
        return noticeRepository.searchList(pageRequestDTO);
    }

    @Transactional
    public void addViewCount(Long id){
        Notice notice =  noticeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("not found"+id)
        );
        notice.increaseViewCount();
    }

    public NoticeDTO getNotice(Long id){
        Notice notice =  noticeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("not found"+id)
        );
        return NoticeDTO.toDTO(notice);
    }

    public Long add(NoticeDTO dto){
        return noticeRepository.save(dto.toEntity()).getId();
    }

    @Transactional
    public void modify(NoticeDTO dto){
        Notice notice = noticeRepository.findById(dto.getId()).orElseThrow(
                () -> new IllegalArgumentException("not found"+dto.getId())
        );
        notice.chageTitle(dto.getTitle());
        notice.chageContent(dto.getContent());
    }

    public Long remove(Long id){
        noticeRepository.deleteById(id);
        return id;
    }
}
