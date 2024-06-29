package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.config.trip.IamportConfig;
import com.example.jhta_3team_finalproject.domain.TourPackage.Purchase;
import com.example.jhta_3team_finalproject.service.TourPackage.CartService;
import com.example.jhta_3team_finalproject.service.TourPackage.PurchaseService;
import com.example.jhta_3team_finalproject.util.CookieService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.siot.IamportRestClient.response.PaymentCancelDetail;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.jhta_3team_finalproject.service.payment.KakaoErrorService;

import java.io.IOException;
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

    @Autowired
    private KakaoErrorService kakaoErrorService;

    @PostMapping("/complete")
    public ResponseEntity<Map<String, Object>> completePayment(HttpServletResponse cookieresponse,@RequestBody Map<String, String> paymentData) throws IamportResponseException, IOException {
        System.out.println("결제로직 시작");
        System.out.println("buyer_no: " + paymentData.get("buyer_no"));
        System.out.println("trip_no: " + paymentData.get("trip_no"));

        boolean value=false;
        String msg="";

        String paid_amount = paymentData.get("paid_amount");
        String imp_uid = paymentData.get("imp_uid");
        //String imp_uid ="error";

        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("imp_uid = "+imp_uid);
            IamportResponse<Payment> iamportResponse = iamportConfig.getIamportClient().paymentByImpUid(imp_uid);

            if (iamportResponse != null && iamportResponse.getResponse() != null) {
                Payment payment = iamportResponse.getResponse();
                // 실제 결제 금액과 주문한 금액을 비교하여 결제 완료 처리
                System.out.println("실제 결제 금액 : "+payment.getAmount());
                System.out.println("주문 금액 : "+paid_amount);

                String Actualpayment =  payment.getAmount().toString();

                if (Actualpayment.equals(paid_amount)) {

                    // Purchase 객체 생성 및 저장
                    Purchase purchase = new Purchase();
                    purchase.setImpUid(imp_uid);
                    purchase.setMerchantUid(paymentData.get("merchant_uid"));
                    purchase.setBuyerNo(Integer.valueOf(paymentData.get("buyer_no")));
                    purchase.setAmount(new BigDecimal(paid_amount));
                    purchase.setTripNo(Integer.valueOf(paymentData.get("trip_no")));
                    purchase.setOptionIds(paymentData.get("option_ids"));

                    purchaseService.savePurchase(purchase);

                    //해당 고객의 cart db/ cart cookie비우기
                    cartService.deleteCart(paymentData.get("buyer_no"));
                    CookieService.deleteCookie(cookieresponse, "cart");

                    // 서비스 로직 수행, 예: 주문 상태 업데이트
                    value=true;
                    msg="결제가 완료되었습니다.";
                    System.out.println("결제 완료되었습니다");

                } else {
                    msg="결제 금액이 일치하지 않습니다.";
                    System.out.println("결제 금액이 일치하지 않습니다.");
                }
            } else {
                msg="결제 정보를 확인할 수 없습니다.";
                System.out.println("결제 정보를 확인할 수 없습니다.");
            }

            response.put("success",value);
            response.put("message",msg);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error Handler Start");

            e.printStackTrace();
            int errorCode = extractErrorCode(e);
            String errorMessage = kakaoErrorService.handleError(errorCode);
            response.put("message",errorMessage);

            int retry;

            System.out.println("Error Code "+errorCode);

            switch (errorCode) {

                case -1:

                    System.out.println("Case 1");
                    retry = retryPayment(paid_amount);
                    if (retry == 1) {
                        response.put("success", true);
                        response.put("message", "결제가 성공적으로 완료되었습니다.");
                        return ResponseEntity.ok(response);
                    }else{
                        value=false;
                        msg="결제 인증 실패, 최대 재시도 횟수 초과.";
                    }
                    break;

                case -701: // 결제인증이 완료되지 않았는데 결제승인 API를 호출한 경우

                    System.out.println("Case 701");

                    retry = retryPayment(paid_amount);
                    if (retry == 1) {
                        response.put("success", true);
                        response.put("message", "결제가 성공적으로 완료되었습니다.");
                        return ResponseEntity.ok(response);
                    }else{
                        value=false;
                        msg="결제 인증 실패, 최대 재시도 횟수 초과.";
                    }
                    break;

                case -708: // 잘못된 PG토큰 처리

                    System.out.println("Case 708");

                    value=false;
                    response.put("message", "잘못된 PG 토큰입니다. 다시 시도해 주세요.");

                    // 새로운 토큰 발급 로직을 추가하거나, 사용자에게 토큰 재발급을 요청합니다.
                    try {
                        String newPgToken = String.valueOf(iamportConfig.getIamportClient().getAuth());

                        value=true;
                        msg="새로운 PG 토큰이 발급되었습니다. 결제를 다시 시도해 주세요.";
                        response.put("newPgToken", newPgToken);
                    } catch (Exception tokenException) {
                        tokenException.printStackTrace();
                        msg="새로운 PG 토큰 발급에 실패했습니다. 고객 지원팀에 문의해 주세요.";
                    }
                    break;

                case -780: // 결제승인 실패

                    System.out.println("Case 780");

                    retry = retryPayment(paid_amount);
                    if (retry == 1) {
                        response.put("success", true);
                        response.put("message", "결제가 성공적으로 완료되었습니다.");
                        return ResponseEntity.ok(response);
                    }else{
                        value=false;
                        msg="결제 승인 실패, 최대 재시도 횟수 초과.";
                    }
                    break;

                case -702: // 이미 결제 완료된 TID로 다시 결제승인 API를 호출한 경우
                    // 이미 처리된 결제인지 확인
                    System.out.println("Case 702");

                    boolean isAlreadyProcessed = checkIfPaymentAlreadyProcessed(imp_uid);

                    if (isAlreadyProcessed) {
                        // 이미 처리된 결제라면 자동으로 취소 처리
                        cancelPayment(imp_uid);

                        // 사용자에게 알림
                        value=false;
                        msg="이미 처리된 결제입니다. 자동으로 취소 처리되었습니다.";
                    } else {
                        // 신규 결제 처리 로직
                        IamportResponse<Payment> iamportResponse = iamportConfig.getIamportClient().paymentByImpUid(imp_uid);

                        if (iamportResponse != null && iamportResponse.getResponse() != null) {
                            response.put("success", true);
                            response.put("message", "결제가 성공적으로 처리되었습니다.");
                            return ResponseEntity.ok(response);
                        } else {
                            value=false;
                            msg="결제 처리 중 오류가 발생했습니다.";
                        }
                    }
                    break;

                case -781: // 결제 취소 실패

                    System.out.println("Case 781");

                    value=false;
                    msg="결제 취소에 실패했습니다.";

                    //결제 취소 반복 메서드
                    retryCancelPayment(imp_uid);

                    break;

                default:
                    value=false;
                    msg="결제 처리 중 오류가 발생했습니다. (controller)";
                    break;
            }
            response.put("success", value);
            response.put("message", msg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    @PostMapping("/refund")
    public ResponseEntity<String> refundPayment(@RequestBody Map<String, String> refundRequest) {

        BigDecimal amount = new BigDecimal(refundRequest.get("amount"));

        CancelData cancelData = new CancelData(refundRequest.get("impUid"),true,amount);

        cancelData.setChecksum(amount); // 환불 금액 설정
        cancelData.setReason(refundRequest.get("reason")); // 환불 사유 설정

        System.out.println("start refund");
        System.out.println(amount);

        try {
            IamportResponse<Payment> iamportResponse = iamportConfig.getIamportClient().cancelPaymentByImpUid(cancelData);

            if (iamportResponse == null || iamportResponse.getResponse() == null) {
                // 응답이 null인 경우
                System.out.println("Failed to cancel payment: Response is null");

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to cancel payment: Response is null");
            }

            if (iamportResponse.getResponse() != null) {
                Payment payment = iamportResponse.getResponse();
                String status = payment.getStatus();
                System.out.println(status);
                if ("cancelled".equals(status)) {
                    // 결제가 성공적으로 취소됨
                    System.out.println("Payment cancelled successfully");
                    return ResponseEntity.ok("Payment cancelled successfully");
                } else {
                    // 결제 취소 실패
                    System.out.println("Failed to cancel payment: Payment status is not 'cancelled'");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to cancel payment: Payment status is not 'cancelled'");
                }
            }else{
                //getResponse의 값이 null
                System.out.println("Failed to cancel payment: iamportResponse.getResponse is null");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to cancel payment: iamportResponse.getResponse is null");
            }

        } catch (Exception e) {
            int errorCode = extractErrorCode(e);
            String errorMessage = kakaoErrorService.handleError(errorCode);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exception occurred: " + e.getMessage());
        }
    }

    //에러코드 추출
    private int extractErrorCode(Exception e) {
        String message = e.getMessage();
        if (message != null && !message.isEmpty()) {
            try {
                // 숫자 이외의 문자는 모두 제거하고 숫자만 남깁니다.
                System.out.println("message :"+message);
                String numericPart = message.replaceAll("[^\\d]", "");

                return Integer.parseInt(numericPart);
            } catch (NumberFormatException ex) {
                System.err.println("NumberFormatException occurred: " + e.getMessage());
            }
        }
        return -1; // 오류 코드를 추출할 수 없는 경우 -1을 반환합니다.
    }

    // 재시도 메서드
    private int retryPayment(String paid_amount) {
        int retryIntervalSeconds = 30; // 재시도 간격 30초

        for (int i = 0; i < 3; i++) {
            try {
                System.out.println(i+"차 재시도");
                // 결제 재시도
                IamportResponse<Payment> retryResponse = iamportConfig.getIamportClient().paymentByImpUid(paid_amount);
                if (retryResponse != null && retryResponse.getResponse() != null) {
                    return 1; // 성공
                }
            } catch (Exception retryException) {
                retryException.printStackTrace();
            }

            // 재시도 간격 설정
            if (i < 2) { // 마지막 재시도 이후 sleep을 호출x.
                try {
                    Thread.sleep(retryIntervalSeconds * 1000); // 초 단위로 설정
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return -1; // 실패
    }


    //이미 처리된 결제인지 확인
    private boolean checkIfPaymentAlreadyProcessed(String imp_uid) {
        try {
            // imp_uid로 결제 정보를 조회
            IamportResponse<Payment> iamportResponse = iamportConfig.getIamportClient().paymentByImpUid(imp_uid);

            // 조회된 결제 정보가 있다면 이미 처리된 결제로 간주하여 true를 반환합니다.
            return iamportResponse != null && iamportResponse.getResponse() != null;

        } catch (IamportResponseException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void cancelPayment(String imp_uid) throws IamportResponseException, IOException {
        CancelData cancelData = new CancelData(imp_uid, true);

        // 취소 요청을 결제 시스템에 전송합니다.
        IamportResponse<Payment> cancelResponse = iamportConfig.getIamportClient().cancelPaymentByImpUid(cancelData);

        if ("cancelled".equals(cancelResponse.getResponse().getStatus())) {
            System.out.println("결제 취소 성공");
        } else {
            System.out.println("결제 취소 실패");
        }
    }

    private void retryCancelPayment(String imp_uid) {
        int currentRetry = 0;
        while (currentRetry < 3) {
            try {
                cancelPayment(imp_uid);
                break;
            } catch (Exception e) {
                currentRetry++;
                if (currentRetry < 3) {
                    int retryIntervalSeconds = 5;
                    try {
                        Thread.sleep(retryIntervalSeconds * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

}