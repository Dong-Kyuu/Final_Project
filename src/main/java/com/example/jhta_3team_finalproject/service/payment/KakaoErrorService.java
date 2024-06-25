package com.example.jhta_3team_finalproject.service.payment;


import com.example.jhta_3team_finalproject.domain.TourPackage.KakaoErrorCode;
import org.springframework.stereotype.Service;

@Service
public class KakaoErrorService {

    public String handleError(int errorCode) {
        KakaoErrorCode kakaoErrorCode = KakaoErrorCode.fromCode(errorCode);
        if (kakaoErrorCode != null) {
            // 로그 남기기
            logError(kakaoErrorCode);
            // 적절한 메시지 반환
            return kakaoErrorCode.getMessage();
        } else {
            // 기본 에러 메시지
            return "Unknown error occurred.";
        }
    }

    private void logError(KakaoErrorCode kakaoErrorCode) {
        // 에러를 로그로 남깁니다. (예: 로깅 프레임워크 사용)
        System.err.println("Error Code: " + kakaoErrorCode.getCode() + ", Message: " + kakaoErrorCode.getMessage());
    }
}