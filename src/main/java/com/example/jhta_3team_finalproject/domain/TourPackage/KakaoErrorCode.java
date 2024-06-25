package com.example.jhta_3team_finalproject.domain.TourPackage;

public enum KakaoErrorCode {
    INTERNAL_ERROR(-1, "Internal processing error."),
    INVALID_PARAMETER(-2, "Invalid parameter."),
    UNSUPPORTED_API(-3, "Unsupported API call."),
    ACCOUNT_SUSPENDED(-4, "Account is suspended."),
    PERMISSION_DENIED(-5, "Permission denied."),
    SERVICE_UNAVAILABLE(-7, "Service unavailable."),
    REQUEST_LIMIT_EXCEEDED(-10, "Request limit exceeded."),
    USER_NOT_CONNECTED(-101, "User not connected."),
    USER_ALREADY_CONNECTED(-102, "User already connected."),
    NON_EXISTENT_ACCOUNT(-103, "Non-existent Kakao account."),
    INVALID_USER_MANAGEMENT_PARAMETERS(-201, "Invalid user management parameters."),
    UNREGISTERED_APP_KEY(-301, "Unregistered app key."),
    INVALID_USER_TOKEN(-401, "Invalid user token."),
    USER_PERMISSION_DENIED(-402, "User permission denied."),
    UNREGISTERED_SITE(-403, "Unregistered site."),
    KAKAOTALK_NOT_JOINED(-501, "User not joined KakaoTalk."),
    KAKAOSTORY_NOT_JOINED(-601, "User not joined KakaoStory."),
    IMAGE_UPLOAD_EXCEEDS_MAX_SIZE(-602, "Image upload exceeds max size."),
    IMAGE_UPLOAD_TIMEOUT(-603, "Image upload timeout."),
    SCRAP_FAILED(-604, "Scrap failed."),
    NON_EXISTENT_STORY(-605, "Non-existent KakaoStory."),
    IMAGE_UPLOAD_EXCEEDS_MAX_COUNT(-606, "Image upload exceeds max count."),
    PAYMENT_AUTHENTICATION_INCOMPLETE(-701, "Payment authentication incomplete."),
    DUPLICATE_PAYMENT_APPROVAL(-702, "Duplicate payment approval."),
    INVALID_POINT_AMOUNT(-703, "Invalid point amount."),
    INVALID_ORIGINAL_PAYMENT_AMOUNT(-704, "Invalid original payment amount."),
    UNSUPPORTED_PAYMENT_METHOD(-705, "Unsupported payment method."),
    MISMATCHED_PARTNER_ORDER_ID(-706, "Mismatched partner order ID."),
    MISMATCHED_PARTNER_USER_ID(-707, "Mismatched partner user ID."),
    INVALID_PG_TOKEN(-708, "Invalid pg_token."),
    EXCESSIVE_CANCEL_AMOUNT(-710, "Excessive cancel amount."),
    NON_EXISTENT_TID(-721, "Non-existent TID."),
    INVALID_AMOUNT_INFO(-722, "Invalid amount info."),
    PAYMENT_EXPIRED(-723, "Payment expired."),
    INVALID_SINGLE_PAYMENT_AMOUNT(-724, "Invalid single payment amount."),
    INVALID_TOTAL_PAYMENT_AMOUNT(-725, "Invalid total payment amount."),
    INVALID_ORDER_INFO(-726, "Invalid order info."),
    INVALID_APP_INFO(-730, "Invalid app info."),
    INVALID_CID(-731, "Invalid CID."),
    INVALID_GID(-732, "Invalid GID."),
    INVALID_CID_SECRET(-733, "Invalid CID_SECRET."),
    NON_EXISTENT_SID(-750, "Non-existent SID."),
    DEACTIVATED_SID(-751, "Deactivated SID."),
    SID_USAGE_EXCEEDED(-752, "SID usage exceeded."),
    MISMATCHED_PARTNER_USER_ID_WITH_SID(-753, "Mismatched partner user ID with SID."),
    NON_REGISTERED_PHONE_NUMBER(-761, "Non-registered phone number."),
    PAYMENT_APPROVAL_FAILED(-780, "Payment approval failed."),
    PAYMENT_CANCEL_FAILED(-781, "Payment cancel failed."),
    REGULAR_PAYMENT_FAILED(-782, "Regular payment failed."),
    INVALID_PAYMENT_APPROVAL_STATUS(-783, "Invalid payment approval status."),
    INVALID_PAYMENT_CANCEL_STATUS(-784, "Invalid payment cancel status."),
    DUPLICATE_PAYMENT_CANCEL_REQUEST(-785, "Duplicate payment cancel request."),
    UNAUTHORIZED_IP(-798, "Unauthorized IP."),
    DOMAIN_MISMATCH(-799, "Domain mismatch."),
    UNREGISTERED_SITE_DOMAIN(-803, "Unregistered site domain."),
    INTERNAL_STORE_SMB_ERROR(-811, "Internal store SMB error."),
    INTERNAL_TRANSLATION_API_ERROR(-812, "Internal translation API error."),
    NO_PUSH_TOKEN(-901, "No push token."),
    UNSUPPORTED_IMAGE_FORMAT(-911, "Unsupported image format."),
    SERVICE_MAINTENANCE(-9798, "Service under maintenance.");

    private final int code;
    private final String message;

    KakaoErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static KakaoErrorCode fromCode(int code) {
        for (KakaoErrorCode errorCode : values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return null;
    }
}