package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.config.trip.IamportConfig;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private IamportConfig iamportConfig;

    @PostMapping("/complete")
    public ResponseEntity<Map<String, Object>> completePayment(@RequestBody Map<String, String> paymentData) {
        System.out.println("결제로직 시작");

        Map<String, Object> response = new HashMap<>();
        try {
            String impUid = paymentData.get("imp_uid");
            System.out.println("imp_uid = "+impUid);
            IamportResponse<Payment> iamportResponse = iamportConfig.getIamportClient().paymentByImpUid(impUid);

            if (iamportResponse != null && iamportResponse.getResponse() != null) {
                Payment payment = iamportResponse.getResponse();
                // 실제 결제 금액과 주문한 금액을 비교하여 결제 완료 처리
                System.out.println("실제 결제 금액 : "+payment.getAmount());
                System.out.println("주문 금액 : "+paymentData.get("paid_amount"));

                String Actualpayment =  payment.getAmount().toString();
                String Orderpayment = paymentData.get("paid_amount");

                if (Actualpayment.equals(Orderpayment)) {
                    // 서비스 로직 수행, 예: 주문 상태 업데이트
                    response.put("success", true);
                    response.put("message", "결제가 완료되었습니다.");
                } else {
                    response.put("success", false);
                    response.put("message", "결제 금액이 일치하지 않습니다.");
                }
            } else {
                response.put("success", false);
                response.put("message", "결제 정보를 확인할 수 없습니다.");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "결제 처리 중 오류가 발생했습니다.(controller)");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}