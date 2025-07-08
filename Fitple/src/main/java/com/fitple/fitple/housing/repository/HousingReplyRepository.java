package com.fitple.fitple.housing.repository;

import com.fitple.fitple.housing.domain.HousingReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HousingReplyRepository extends JpaRepository<HousingReply, Long> {
    List<HousingReply> findByPropertyIdAndIsDeletedFalseOrderByCreatedAtDesc(String propertyId);
}