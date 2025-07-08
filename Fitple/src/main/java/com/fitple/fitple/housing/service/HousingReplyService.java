package com.fitple.fitple.housing.service;

import com.fitple.fitple.housing.domain.HousingReply;
import com.fitple.fitple.housing.dto.HousingReplyDTO.ReplyCreateRequest;
import com.fitple.fitple.housing.dto.HousingReplyDTO.ReplyUpdateRequest;
import com.fitple.fitple.housing.dto.HousingReplyDTO.ReplyResponse;
import com.fitple.fitple.housing.repository.HousingReplyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 만들어줍니다. (DI)
@Transactional // 클래스 내의 모든 public 메소드에 트랜잭션을 적용합니다.
public class HousingReplyService {

    private final HousingReplyRepository housingReplyRepository;

    @Transactional(readOnly = true)
    public List<ReplyResponse> findRepliesByPropertyId(String propertyId) {
        return housingReplyRepository.findByPropertyIdAndIsDeletedFalseOrderByCreatedAtDesc(propertyId)
                .stream()
                .map(ReplyResponse::from)
                .collect(Collectors.toList());
    }

    public ReplyResponse createReply(String userId, ReplyCreateRequest request) {
        HousingReply newReply = HousingReply.builder()
                .userId(userId)
                .replyContent(request.replyContent())
                .propertyId(request.propertyId()) // ▼▼▼ pnu -> propertyId 변경
                .build();

        HousingReply savedReply = housingReplyRepository.save(newReply);

        return ReplyResponse.from(savedReply);
    }

    public ReplyResponse updateReply(Long replyId, String currentUserId, ReplyUpdateRequest request) {
        HousingReply reply = housingReplyRepository.findById(replyId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 댓글을 찾을 수 없습니다: " + replyId));

        if (!reply.getUserId().equals(currentUserId)) {
            throw new IllegalStateException("댓글을 수정할 권한이 없습니다.");
        }

        reply.updateContent(request.replyContent());

        return ReplyResponse.from(reply);
    }

    public void deleteReply(Long replyId, String currentUserId) {
        HousingReply reply = housingReplyRepository.findById(replyId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 댓글을 찾을 수 없습니다: " + replyId));

        if (!reply.getUserId().equals(currentUserId)) {
            throw new IllegalStateException("댓글을 삭제할 권한이 없습니다.");
        }

        reply.markAsDeleted();
    }
}