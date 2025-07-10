package com.fitple.fitple.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PageRequestDTO {

    @Min(1)
    private Integer page;

    @Min(5)
    @Max(100)
    private Integer size;

    private String[] types;
    private String keyword;
    private Integer salary;
    private String location;
    private String ncs;

    // 지역 필터링용 법정시군구코드
    private String[] zipCds;

    public String getLink() {
        StringBuilder sb = new StringBuilder();

        if (keyword != null && !keyword.isEmpty()) {
            sb.append("&keyword=").append(keyword);
        }

        if (zipCds != null) {
            for (String zip : zipCds) {
                sb.append("&zipCds=").append(zip);
            }
        }

        return sb.toString();
    }


    public int getPage() {
        return (page == null) ? 1 : page;
    }

    public int getSize() {
        return (size == null) ? 10 : size;
    }

    public int getSkip() {
        return (getPage() - 1) * getSize();
    }
}
