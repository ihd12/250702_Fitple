package com.fitple.fitple.local_price.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 지역별 물가 기본 정보
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class LCPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 전략 추가
    private Long localNo;

    @Column (name="subwayCa")
    private Integer subwayCa;

    @Column (name="subwayMo")
    private Integer subwayMo;

    @Column (name="BusCa")
    private Integer busCa;

    @Column (name="BusMo")
    private Integer busMo;

    @Column (name="taxy")
    private Integer taxy;

    @Column (name="trashBag")
    private Integer trashBag;

    @Column (name="laundry")
    private Integer laundry;

    @Column (name="stay")
    private Integer stay;

    @Column (name="manCut")
    private Integer manCut;

    @Column (name="womanCut")
    private Integer womanCut;

    @Column (name="bath")
    private Integer bath;

    @Column (name="rangmyeon")
    private Integer rangmyeon;

    @Column (name="bibimbab")
    private Integer bibimbab;

    @Column (name="kimchistew")
    private Integer kimchistew;

    @Column (name="samgyeopsal")
    private Integer samgyeopsal;

    @Column (name="zazhang")
    private Integer zazhang;

    @Column (name="samgye")
    private Integer samgye;

    @Column (name="kimbab")
    private Integer kimbab;

    @Column (name="kalnoodle")
    private Integer kalnoodle;

}
