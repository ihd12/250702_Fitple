package com.fitple.fitple.notice.repository.dsl;

import com.fitple.fitple.common.dto.PageRequestDTO;
import com.fitple.fitple.common.dto.PageResponseDTO;
import com.fitple.fitple.notice.dto.NoticeDTO;

public interface NoticeDslRepository {
    PageResponseDTO<NoticeDTO> searchList(PageRequestDTO pageRequestDTO);
}
