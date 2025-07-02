package com.fitple.fitple.local_price.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "lc_price")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class LCPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long priceId; // 🔑 데이터베이스가 관리하는 고유 ID (PK)

    @Column(name= "local_no", nullable = false)
    private Integer localNo; // 식별용 지역 번호

    @Column(name = "local_name", length = 20, nullable = false) // 식별용 지역 이름
    private String localName;

    @Column // 물가 항목(원 단위)
    private Integer subwayCa;
    private Integer subwayMo;
    private Integer busCa;
    private Integer busMo;
    private Integer taxy;
    private Integer trashBag;
    private Integer laundry;
    private Integer stay;
    private Integer manCut;
    private Integer womanCut;
    private Integer bath;
    private Integer rangmyeon;
    private Integer bibimbab;
    private Integer kimchistew;
    private Integer samgyeopsal;
    private Integer zazhang;
    private Integer samgye;
    private Integer kimbab;
    private Integer kalnoodle;
}