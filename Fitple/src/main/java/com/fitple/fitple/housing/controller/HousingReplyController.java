package com.fitple.fitple.housing.controller;

import com.fitple.fitple.housing.dto.HousingReplyDTO;
import com.fitple.fitple.housing.service.HousingReplyService;
import com.fitple.fitple.housing.dto.HousingReplyDTO.ReplyCreateRequest;
import com.fitple.fitple.housing.dto.HousingReplyDTO.ReplyUpdateRequest;
import com.fitple.fitple.housing.dto.HousingReplyDTO.ReplyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/replies")
@RequiredArgsConstructor
public class HousingReplyController {

    private final HousingReplyService housingReplyService;

    // 댓글 목록 조회
    @GetMapping
    public ResponseEntity<List<ReplyResponse>> getReplies(@RequestParam String propertyId) {
        List<ReplyResponse> replies = housingReplyService.findRepliesByPropertyId(propertyId);
        return ResponseEntity.ok(replies);
    }

    // 댓글 작성
    @PostMapping
    public ResponseEntity<ReplyResponse> createReply(@RequestBody ReplyCreateRequest request) {
        String currentUserId = "testUser"; // 임시 사용자 ID
        ReplyResponse createdReply = housingReplyService.createReply(currentUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReply);
    }

    // 댓글 수정
    @PutMapping("/{replyId}")
    public ResponseEntity<ReplyResponse> updateReply(@PathVariable Long replyId, @RequestBody ReplyUpdateRequest request) {
        String currentUserId = "testUser"; // 임시 사용자 ID
        ReplyResponse updatedReply = housingReplyService.updateReply(replyId, currentUserId, request);
        return ResponseEntity.ok(updatedReply);
    }

    // 댓글 삭제 (DELETE /api/replies/1)
    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId) {
        String currentUserId = "testUser"; // 임시 사용자 ID
        housingReplyService.deleteReply(replyId, currentUserId);
        return ResponseEntity.noContent().build();
    }
}