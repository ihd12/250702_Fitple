package com.fitple.fitple.home.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "home_sigungu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
// 광역시도 및 시군구 데이터를 바탕으로 임대주택 API와 연결 및 필요시 지역 구분 가능할 수 있도록
public class Home {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 법정동코드를 지역 구분용 ID로 할 예정

    @Column (name = "brtc", nullable = false)
    private String brtc;

    @Column (name = "sigungu", nullable = false)
    private String sigungu;

    @Column (name = "local_code", nullable = false, updatable = false, unique = true)
    private Long localCode;
}
