package com.fitple.fitple.recommend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobRecommendDTO {

    private Long id;
    private String title;
    private String location;
    private String ncs;
    private Integer salary;
    private int score;

    public JobRecommendDTO(Long id, String title, String location, String ncs, Integer salary) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.ncs = ncs;
        this.salary = salary;
    }
}
