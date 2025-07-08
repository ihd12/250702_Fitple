package com.fitple.fitple.infra.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class InfraService {
    // 예시: 주소에 따른 행정구역 코드(sigCd)를 반환하는 메서드
    public String getSigCdFromAddress(String address) {
        // 여기에 실제 로직을 넣어야 합니다.
        // 예시로, 주소를 기준으로 행정구역 코드(sigCd)를 반환한다고 가정
        // 실제로는 외부 API 호출이나 데이터베이스 조회를 할 수 있습니다.

        if (address.contains("서울")) {
            return "11"; // 서울의 행정구역 코드
        } else if (address.contains("부산")) {
            return "26"; // 부산의 행정구역 코드
        } else {
            return null; // 알 수 없는 주소
        }
    }


}
