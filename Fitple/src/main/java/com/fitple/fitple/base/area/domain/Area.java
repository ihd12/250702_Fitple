package com.fitple.fitple.base.area.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "area")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Area {

    @Id
    @Column(name = "area_code", length = 10)
    private String areaCode;

    @Column(name = "area_name", length = 100, nullable = false)
    private String areaName;

    @Column(name = "region_level", length = 10)
    private String regionLevel;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
