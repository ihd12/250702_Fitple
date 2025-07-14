package com.fitple.fitple.notice.repository;

import com.fitple.fitple.notice.domain.Notice;
import com.fitple.fitple.notice.repository.dsl.NoticeDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice,Long>, NoticeDslRepository {

    List<Notice> findAllByOrderByCreatedAtDesc();


}
