package com.example.jhta_3team_finalproject.util;

import com.example.jhta_3team_finalproject.domain.TourPackage.Cart;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieService {

    // 쿠키 존재 여부 확인 메서드
    public static boolean isCookiePresent(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String serializeCartCookie(Cart cart) {
        //String cartString = "cartNo=" + cart.getCartNo() + "&tripNo=" + cart.getTripNo() + "&optionIds=" + cart.getOptionIds();
        String cartString ="tripNo=" + cart.getTripNo() + "&optionIds=" + cart.getOptionIds();
        System.out.println("쿠키 = " + cartString);
        return cartString;
    }

    // 쿠키 내용을 변경하는 메서드
    public static void setCookie(HttpServletResponse response, String cookieName,String newCookie) {
        Cookie cookie = new Cookie(cookieName, newCookie);
        cookie.setMaxAge(24 * 60 * 60); // Set cookie expiry to 1 day
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    // 쿠키 내용을 가져오는 메서드
    public static String getCookieValue(HttpServletRequest request,String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    System.out.println("쿠키내용 ==> " + cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        // 쿠키가 없는 경우
        return null;
    }

    // 쿠키 삭제 메서드
    public static void deleteCookie(HttpServletResponse response, String cookieName) {

        System.out.println(cookieName);
        Cookie cookie = new Cookie(cookieName, "");

        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }



    public static String getValueBetweenEquals(String cookieValue, String key) {
        // 입력 문자열을 "&"을 기준으로 나누기
        String[] pairs = cookieValue.split("&");

        // 각각의 키-값 쌍에서 주어진 키를 찾기
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2 && keyValue[0].equals(key)) {
                return keyValue[1];
            }
        }
        // 해당 키가 없으면 null 리턴
        return null;
    }

    public static String CartcookieToURL(String cookieValue) {

        StringBuilder result = new StringBuilder("Cart?");

        if(cookieValue!=null){
            String num = getValueBetweenEquals(cookieValue, "tripNo");
            String selectedOptions = getValueBetweenEquals(cookieValue, "optionIds");

            if (selectedOptions != null && !selectedOptions.equals("null")) {
                result.append("selectedOptions=").append(selectedOptions);
            }
            if (num != null && !num.equals("null")) {
                if (selectedOptions != null) {
                    result.append("&");
                }
                result.append("num=").append(num);
            }
        }

        String newURL = result.toString();
        System.out.println("result===> " + newURL);

        return newURL;
    }


}
