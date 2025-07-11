package com.fitple.fitple.notice.repository.dsl;

import com.fitple.fitple.common.dto.PageRequestDTO;
import com.fitple.fitple.common.dto.PageResponseDTO;
import com.fitple.fitple.notice.domain.Notice;
import com.fitple.fitple.notice.domain.QNotice;
import com.fitple.fitple.notice.dto.NoticeDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class NoticeDslRepositoryImpl extends QuerydslRepositorySupport implements NoticeDslRepository {
    public NoticeDslRepositoryImpl() {super(Notice.class);}
    @Override
    public PageResponseDTO<NoticeDTO> searchList(PageRequestDTO pageRequestDTO) {
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("id");
        QNotice qNotice = QNotice.notice;
        JPQLQuery<Notice> query = from(qNotice);
        if(keyword!=null && !keyword.isEmpty()){
            BooleanBuilder builder = new BooleanBuilder();
            builder.or(qNotice.title.contains(keyword));
            builder.or(qNotice.content.contains(keyword));
            query.where(builder);
        }
        int totalCount = Long.valueOf(query.fetchCount()).intValue();
        this.getQuerydsl().applyPagination(pageable,query);
        List<Notice> list = query.fetch();
        return PageResponseDTO.<NoticeDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list.stream().map(NoticeDTO::toDTO).toList())
                .total(totalCount)
                .build();
    }
}
