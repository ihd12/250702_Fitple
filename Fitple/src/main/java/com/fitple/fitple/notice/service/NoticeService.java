package com.fitple.fitple.notice.service;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.base.user.dto.UserDTO;
import com.fitple.fitple.base.user.repository.UserRepository;
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
    private final UserRepository userRepository;


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

    public void saveNotice(NoticeDTO dto, String writerEmail) {
        User admin = userRepository.findByEmail(writerEmail)
                .orElseThrow(() -> new RuntimeException("관리자 정보 없음"));

        Notice notice = Notice.builder()
                .title(dto.getTitle())
                .content(dto.getContent().replaceAll("(\r\n|\r|\n)", "<br/>"))
                .admin(admin)
                .viewCount(0)
                .build();

        noticeRepository.save(notice);
    }


}
