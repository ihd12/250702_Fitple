package com.fitple.fitple.housing.dto;

import com.fitple.fitple.housing.domain.HousingReply;

import java.time.LocalDateTime;

public class HousingReplyDTO {

    // 요청 DTO
    public record ReplyCreateRequest(String replyContent, String propertyId) {}
    public record ReplyUpdateRequest(String replyContent) {}

    // 응답 DTO
    public record ReplyResponse(Long id, String userId, String replyContent, LocalDateTime createdAt) {
        public static ReplyResponse from(HousingReply reply) {
            return new ReplyResponse(reply.getId(), reply.getUserId(), reply.getReplyContent(), reply.getCreatedAt());
        }
    }
}
