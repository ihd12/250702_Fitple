package com.fitple.fitple.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

public class KakaoCategoryResponse {
    private List<Document> documents;
    private Meta meta;

    @Data
    public static class Document {
        @JsonProperty("place_name") private String placeName;
        @JsonProperty("address_name") private String addressName;
        private String x; // longitude
        private String y; // latitude
        @JsonProperty("place_url") private String placeUrl;
    }

    @Data
    public static class Meta {
        @JsonProperty("total_count") private int totalCount;
        @JsonProperty("pageable_count") private int pageableCount;
        @JsonProperty("is_end") private boolean isEnd;
    }
}
