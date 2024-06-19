package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.config.trip.IamportConfig;
import com.example.jhta_3team_finalproject.domain.TourPackage.Purchase;
import com.example.jhta_3team_finalproject.service.TourPackage.CartService;
import com.example.jhta_3team_finalproject.service.TourPackage.CustomerService;
import com.example.jhta_3team_finalproject.service.TourPackage.PurchaseService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private CartService cartService;

    @Autowired
    private IamportConfig iamportConfig;

    @PostMapping("/complete")
    public ResponseEntity<Map<String, Object>> completePayment(HttpServletResponse cookieresponse,@RequestBody Map<String, String> paymentData) {
        System.out.println("결제로직 시작");
        System.out.println("buyer_no: " + paymentData.get("buyer_no"));
        System.out.println("trip_no: " + paymentData.get("trip_no"));

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

                    // Purchase 객체 생성 및 저장
                    Purchase purchase = new Purchase();
                    purchase.setImpUid(impUid);
                    purchase.setMerchantUid(paymentData.get("merchant_uid"));
                    purchase.setBuyerNo(Integer.valueOf(paymentData.get("buyer_no")));
                    purchase.setAmount(new BigDecimal(Orderpayment));
                    purchase.setTripNo(Integer.valueOf(paymentData.get("trip_no")));
                    purchase.setOptionIds(paymentData.get("option_ids"));

                    purchaseService.savePurchase(purchase);

                    //해당 고객의 cart db/ cart cookie비우기
                    cartService.deleteCart(paymentData.get("buyer_no"));
                    deleteCookie(cookieresponse, "cart");

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
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "결제 처리 중 오류가 발생했습니다.(controller)");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/refund")
    public ResponseEntity<String> refundPayment(@RequestBody Purchase refundRequest) {
        CancelData cancelData = new CancelData(refundRequest.getImpUid(),true);
        cancelData.setChecksum(refundRequest.getAmount()); // 환불 금액 설정
        cancelData.setReason(refundRequest.getRejectReason()); // 환불 사유 설정

        try {
            IamportResponse<Payment> response = iamportConfig.getIamportClient().cancelPaymentByImpUid(cancelData);
            if ("cancelled".equals(response.getResponse().getStatus())) {
                return ResponseEntity.ok("Payment cancelled successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to cancel payment");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exception occurred: " + e.getMessage());
        }
    }

    // 쿠키 삭제 메서드
    public void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}