package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.service.TourPackage.*;
import com.example.jhta_3team_finalproject.domain.TourPackage.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.*;

@Controller
public class TripController {

    private TripService tripService;
    private OptionService optionService;
    private CartService cartService;

    @Autowired
    public TripController(TripService tripService, CartService cartService, OptionService optionService) {
        this.tripService = tripService;
        this.cartService = cartService;
        this.optionService = optionService;
    }

    @GetMapping("/tripPage")
    public String tripPage(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "9") int limit,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "keyword", required = false) String keyword,
            HttpServletRequest request,HttpServletResponse response,
            Model model) {

        // -----------------------------------------
        // 산 - 카트 쿠키가 자기 자신의 쿠키가 아니면 삭제
        // cartNo가 mem_id와 동일하면 유지
        // 로그인하지않은 상태라면 항상 카트 쿠키 / 카트 db 초기화
        //아이디 세션에서 불러오기
        HttpSession session = request.getSession(); // 세션이 없으면 새로 생성하지 않음
        String memberId = (String) session.getAttribute("member_id");// 세션에서 "member_id" 값을 가져옴
        // memberId를 이용하여 필요한 작업 수행
        String cookieValue = getCookieValue(request);// 쿠키 내용을 갖고오는 메서드
        if(cookieValue!=null) {
            String cartNoValue = getValueBetweenEquals(cookieValue, "cartNo");
            if(!cartNoValue.equals(memberId)) {//쿠키의 cartNo와 mem_id비교
                deleteCookie(response, "cart");
                System.out.println("<카트 쿠키 삭제>");
            }
        }

        if(cartService.isId("0")==1) {
            System.out.println("삭제");
            cartService.deleteCart("0");//비로그인회원에게 제공되는 cartNo="0"
        }
        // -----------------------------------------

        int listcount = 0;
        List<Trip> triplist;

        int startRow = (page - 1) * limit + 1;
        int endRow = startRow + limit - 1;

        if (category != null && !category.isEmpty() && !Objects.equals(category, "null")) {
            listcount = tripService.getCategoryListCount(category);
            triplist = tripService.getCategoryTripList(startRow, endRow,category, sort);
        } else if (keyword != null && !keyword.isEmpty() && !Objects.equals(keyword, "null")) {
            listcount = tripService.getKeywordListCount(keyword);
            triplist = tripService.getTripListByKeyword(startRow, endRow,keyword,  sort);
        } else {
            listcount = tripService.getListCount();
            triplist = tripService.getTripList(startRow, endRow, sort);
        }

        int maxpage = (listcount + limit - 1) / limit;
        int startpage = ((page - 1) / 10) * 10 + 1;
        int endpage = startpage + 10 - 1;
        if (endpage > maxpage) {
            endpage = maxpage;
        }

        int pagefirst = (page - 1) * limit + 1;//startrow
        int pagelast = Math.min(pagefirst + limit - 1, listcount);//endrow

        model.addAttribute("page", page);
        model.addAttribute("maxpage", maxpage);
        model.addAttribute("startpage", startpage);
        model.addAttribute("endpage", endpage);
        model.addAttribute("listcount", listcount);
        model.addAttribute("triplist", triplist);
        model.addAttribute("limit", limit);
        model.addAttribute("pagefirst", pagefirst);
        model.addAttribute("pagelast", pagelast);
        model.addAttribute("sort", sort);  // 현재 정렬 기준 추가
        model.addAttribute("keyword", keyword);  // 현재 검색어 추가
        return "TourPackage/Trip_Page";
    }

    @GetMapping("/tripDetail")
    public String tripDetail(@RequestParam(name = "num") int num, HttpServletRequest request,Model model) {
        System.out.println("===========================");
        System.out.println("tripDetail?num="+num);
        System.out.println("===========================");

        Trip trip = tripService.getDetail(num);
            if (trip == null) {
                model.addAttribute("message", "데이터를 읽지 못했습니다.");
                return "error/error";
            }

            String optionIds = tripService.getOptionIds(num);
            System.out.println("==> optionIds = "+optionIds);
            String[] optionId = optionIds.split(",");
            List<TripOption> options = new ArrayList<TripOption>();

        for (String s : optionId) {
            System.out.println("==> optionId="+s);
            // 해당 상품의 옵션 정보 가져오기
            TripOption option = optionService.getOptionsByOptionId(s);
            if (option != null) {
                options.add(option);
            }
        }

        TripFile tripFile = tripService.getTripFileByNo(trip.getFileNo());

        //아이디 세션에서 불러오기
        HttpSession session = request.getSession();
        String memberId = (String) session.getAttribute("member_id");

        if (memberId == null) {
                memberId = "0";
        }
        System.out.println("memberId="+memberId);

        Cart cart = cartService.getDetail(memberId);
        int check = cartService.isId(memberId);

        //cartService.updateCart(memberId, String.valueOf(num),null,0 );
        String cartTripNo =String.valueOf(num);
        System.out.println("cartTripNo="+cartTripNo);
        /*
        if (check == 1) {
                cartTripNo = cart.getTripNo();
            System.out.println("cartTripNo="+cartTripNo);
        }
         */

        model.addAttribute("trip", trip);
        model.addAttribute("options", options);
        model.addAttribute("tripFile", tripFile);
        model.addAttribute("tripNo", trip.getTripNo());
        model.addAttribute("check", check);
        model.addAttribute("cartTripNo", cartTripNo);

        return "TourPackage/Trip_Detail";
    }

    @GetMapping("/tripCart")
    public String tripCart(@RequestParam(name = "num", required = false) Integer num,
                           @RequestParam(name="selectedOptions", required = false) String selectedOptions,
                           HttpServletRequest request, HttpServletResponse response,Model model) throws ServletException, IOException {

        // 트립넘버==null ->근데 트립넘버가 null이어도 쿠키가 있을수도 있음 -> 해결법 -> 트립넘버가 아니라 카트넘버로 찾아봐야하나?
        // 경우의 수
        // 1. 젤 맨처음 추가할때 http://localhost:8081/MBTI/TripCart.net?selectedOptions=&num=1
        // => num가 존재하는경우 / 쿠키는 없다
        // 2. 옵션과 메인상품을 다 지우면 => 쿠키는 존재 => 쿠키는 있는데 cart가 없는경우 --> 이럴때는 삭제에서 cartNo까지
        // 지워버리게 해야
        // 3. 쿠키는 있는데 num은 없는경우 =>이건 그냥 조회하는거임 => 원래 쿠키만 보여주면 됨
        // 4. 쿠키도 있고 (쿼리매개변수)num도 있는 경우 => 추가 하려는 거임 =>num도 받아와서 cookie랑 비교해서 집어넣어줘야함
        System.out.println("======TripCart Mapping Processing======");

        System.out.println("num="+num);
        System.out.println("selectedOptions="+selectedOptions);

        Cart cart = null;
        Trip trip = null;
        List<TripOption> options = null;

        int total_price = 0;
        int product_Num = 0;
        // ---------------------
        // 우선 카트를 생성 만약 카트가 이미 있다면 생성 x 카트는 아이디와 동일

        // 1.아이디 세션에서 불러오기
        HttpSession session = request.getSession(); // 세션이 없으면 새로 생성하지 않음
        // 세션에서 "member_id" 값을 가져옴
        String memberId = (String) session.getAttribute("member_id");
        if (memberId == null) {
            memberId = "0";
        }
        System.out.println("memberId = " + memberId);

        // 2.카트의 존재여부 확인
        int cartcheck = cartService.isId(memberId);// cart비었음:0 cart있음:1
        System.out.println("cartcheck = " + cartcheck);

        // 3. 쿠키 존재 여부 확인
        String cookieValue = getCookieValue(request);// 쿠키 내용을 갖고오는 메서드
        System.out.println("cookieValue = " + cookieValue);
        // 4. 주소로 불려오는 num 를 확인
        String check = request.getParameter("num");
        System.out.println("check = " + check);

        if(num!=null){
            trip = tripService.getDetail(num);
        }

        // ---------------------
        if (cookieValue == null && check != null) {
            // CASE 1: First time adding to cart, no cookie
            handleFirstTimeAddToCart(model, response, memberId, num, selectedOptions);
        } else if (cartcheck == 0 && cookieValue == null) {
            // CASE 2: Cart is empty, no cookie
            System.out.println("======CASE 2======");
        } else if (cookieValue != null && check != null) {
            // CASE 3: Cookie exists, adding new items
            handleAddToCartWithExistingCookie(model, response, memberId, cookieValue, selectedOptions);
        } else if (cookieValue != null && check == null) {
            // CASE 4: Cookie exists, no num parameter
            handleViewCartWithCookie(model, cookieValue);
        } else if (cartcheck == 1 && cookieValue == null) {
            // CASE 5: Cart exists, no cookie
            handleViewCartWithoutCookie(model, memberId);
        }

        return "TourPackage/Trip_cart";
    }

    //---------------------------------------------
    //Cart메서드
    private void handleFirstTimeAddToCart(Model model, HttpServletResponse response,
                                            String memberId, int num, String selectedOptions) {
        System.out.println("======CASE 1======");
        Trip trip = tripService.getDetail(num);

        int total_price = trip.getTripPrice();
        int product_Num = 1;

        List<TripOption> options = new ArrayList<>();
        if (selectedOptions != null && !selectedOptions.isEmpty()) {
            String[] optionId = selectedOptions.split("-");
            for (String str : optionId) {
                TripOption option = optionService.getOptionsByOptionId(str);
                if (option != null) {
                    options.add(option);
                    total_price += option.getOptionPrice();
                    product_Num++;
                }
            }
        }

        Cart cart;
        cartService.insertCart(memberId, String.valueOf(num), total_price, selectedOptions);
        cart = cartService.getDetail(memberId);
        String cartString = serializeCart(cart);

        Cookie cartCookie = new Cookie("cart", cartString);
        cartCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cartCookie);

        modelAttribute(model,trip,cart,total_price,product_Num,options);
    }

    private void handleAddToCartWithExistingCookie(Model model, HttpServletResponse response,
                                                   String memberId, String cookieValue, String selectedOptions) {
        System.out.println("======CASE 3======");
        System.out.println("selectedOptions = " + selectedOptions);
        String cartNoValue = getValueBetweenEquals(cookieValue, "cartNo");
        String tripNoValue = getValueBetweenEquals(cookieValue, "tripNo");
     //   String optionIdsValue = getValueBetweenEquals(cookieValue, "optionIds");
      //  String newOptionIds = "";
        System.out.println("Values =>"+cartNoValue+","+tripNoValue+","+selectedOptions);

        if (selectedOptions != null && !selectedOptions.isEmpty()) {
            System.out.println("======selectedOption Processing======");
     //       newOptionIds = mergeOptionIds(optionIdsValue, selectedOptions);
            String updatedCookieValue = cookieValue.replaceAll("optionIds=[^&]*", "optionIds=" + selectedOptions);
            updateCookie(response, updatedCookieValue);
        }

        processCartDetails(model, memberId, tripNoValue, selectedOptions, cartNoValue, response);
    }

    private void handleViewCartWithCookie(Model model, String cookieValue) {
        System.out.println("======CASE 4======");
        String cartNoValue = getValueBetweenEquals(cookieValue, "cartNo");
        String tripNoValue = getValueBetweenEquals(cookieValue, "tripNo");
        String optionIdsValue = getValueBetweenEquals(cookieValue, "optionIds");
        processCartDetails(model, null, tripNoValue, optionIdsValue, cartNoValue, null);
    }

    private void handleViewCartWithoutCookie(Model model, String memberId) {
        System.out.println("======CASE 5======");
        Cart cart = cartService.getDetail(memberId);
        String tripNoValue = cart.getTripNo();
        String optionIdsValue = cart.getOptionIds();
        processCartDetails(model, memberId, tripNoValue, optionIdsValue, null, null);
    }
    //---------------------------------------------

    @PostMapping("/cartDelete")
    public ResponseEntity<Map<String, String>> cartDelete(@RequestParam(name = "identifier") String identifier,
    @RequestParam(name = "itemType") String itemType,
    @RequestParam(name = "cartNo") String cartNo,
    @RequestParam(name = "cartTotal", required = false) String cartTotal,
    HttpServletRequest request, HttpServletResponse response,
    Model model) throws IOException {
        System.out.println("======Cart Delete Processing======");
        System.out.println("identifier="+identifier);
        System.out.println("itemType="+itemType);
        System.out.println("cartNo="+cartNo);
        System.out.println("cartTotal="+cartTotal);

        String updatedOptionIds = "";
                
        TripOption tripOption = new TripOption();

        Cart cart =cartService.getDetail(cartNo);
        optionService.getOptionsByOptionId(identifier);
        int optionPrice = 0;
        int priceTotal = Integer.parseInt(cartTotal);
        if (identifier != null) {
            optionPrice = tripOption.getOptionPrice();
        }

        if ("trip".equals(itemType)) {
            // tripNo를 삭제하는 DAO 호출
            cartService.deleteTripNo(cartNo);
            System.out.println("trip 삭제: " + identifier);
        } else if ("option".equals(itemType)) {
            // optionId을 삭제하는 DAO 호출
            String optionIds = cartService.getOptionIds(cartNo);
            updatedOptionIds = removeOptionId(optionIds,identifier);

            String updatedCartTotal = String.valueOf(priceTotal - optionPrice);
            System.out.println("updatedCartTotal="+updatedCartTotal);

            cartService.deleteOption(updatedOptionIds, cartNo, updatedCartTotal);
            System.out.println("option 삭제: " + identifier);
        } else {
            // 유효하지 않은 itemType에 대한 오류 처리
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid itemType");
        }

        cart = cartService.getDetail(cartNo);

        String cookieValue = getCookieValue(request); // 쿠키 내용을 갖고오는 메서드
        String newCookie = serializeCart(cart);
        setCookie(response, newCookie); // 쿠키 내용을 변경하는 메서드

        if (cart.getTripNo() == null && cart.getOptionIds() == null) {
            cartService.deleteCart(cartNo);
            System.out.println("카트 완전삭제");
            // 쿠키를 완전히 삭제
            deleteCookie(response, "cart");
        }

        String newURL = cookieToURL(newCookie);

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("newURL", newURL);



        return  ResponseEntity.ok(responseMap);
    }

    private String cookieToURL(String cookieValue) {
        System.out.println("cookieToURL cookieValue=" + cookieValue);

        String num = null;
        String selectedOptions = null;

        String[] parts = cookieValue.split("&");
        for (String part : parts) {
            String[] keyValue = part.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];

                if ("tripNo".equals(key)) {
                    num = value.isEmpty() ? null : value;
                } else if ("optionIds".equals(key)) {
                    selectedOptions = value.isEmpty() ? null : value;
                }
            }
        }

        StringBuilder result = new StringBuilder("tripCart?");
        if (selectedOptions != null && !selectedOptions.equals("null")) {
            result.append("selectedOptions=").append(selectedOptions);
        }
        if (num != null && !num.equals("null")) {
            if (selectedOptions != null) {
                result.append("&");
            }
            result.append("num=").append(num);
        }

        String newURL = result.toString();
        System.out.println("result===> " + newURL);

        return newURL;
    }

    private String removeOptionId(String optionIds, String optionId) {    // optionIds에서 optionId를 제거하는 메서드
        String[] optionIdArray = optionIds.split("-");
        StringBuilder sb = new StringBuilder();

        for (String id : optionIdArray) {
            if (!id.equals(optionId)) {
                sb.append(id).append("-");
            }
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // 마지막의 "-"를 삭제합니다.
        }

        return sb.toString();
    }

    @GetMapping("/tripPurchase")
    public String purchase(Model model) {
        // 필요한 모델 속성 추가
        model.addAttribute("member_id", "sampleMemberId"); // 예제 데이터
        model.addAttribute("memberInfo", new Member()); // MemberInfo 객체 생성 및 초기화
        model.addAttribute("trip", new Trip()); // Trip 객체 생성 및 초기화
        model.addAttribute("subTotal", 0); // 예제 데이터
        model.addAttribute("cartTotal", 0); // 예제 데이터

        return "TourPackage/Trip_Purchase";
    }

    @GetMapping("/tripRegister")
    public String tripRegister(Model model) {
        // 필요한 모델 속성 추가
        // 여기에 모델 속성을 추가하십시오.
        return "TourPackage/Trip_Register";
    }

    //------------------------------
    //model.addAttribute
    public void modelAttribute(Model model,Trip trip,Cart cart,int total_price,int product_Num,List<TripOption> options) {
        System.out.println("======Model addAttribute Processing======");
        model.addAttribute("cart", cart);
        model.addAttribute("total_price", total_price);
        model.addAttribute("trip", trip);
        model.addAttribute("product_Num", product_Num);
        model.addAttribute("options", options);
        System.out.println("cart.cartNo="+cart.getCartNo());
    };
    //쿠키 메서드

    private String serializeCart(Cart cart) {
        String cartString = "cartNo=" + cart.getCartNo() + "&tripNo=" + cart.getTripNo() + "&optionIds=" + cart.getOptionIds();
        System.out.println("쿠키 = " + cartString);
        return cartString;
    }

    // 쿠키 내용을 변경하는 메서드
    public void setCookie(HttpServletResponse response, String newCookie) {
        Cookie cookie = new Cookie("cart", newCookie);
        cookie.setMaxAge(24 * 60 * 60); // Set cookie expiry to 1 day
        response.addCookie(cookie);
        System.out.println("쿠키 변경 완료"+newCookie);
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

    // 쿠키 존재 여부를 확인하는 메서드
    public String getCookieValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cart")) {
                    System.out.println("쿠키내용 ==> " + cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        // 쿠키가 없는 경우
        return null;
    }

    // 쿠키 삭제 메서드
    public void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    //쿠키 업데이트 메서드
    public void updateCookie(HttpServletResponse response,String CookieValue){
        Cookie updatedCookie = new Cookie("cart", CookieValue);
        updatedCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(updatedCookie);
    }
    //------------------------------
    //------------------------------
    private List<TripOption> getOptions(String optionIds) {
        List<TripOption> options = new ArrayList<>();
        String[] optionIdArray = optionIds.split("-");
        for (String optionId : optionIdArray) {
            TripOption option = optionService.getOptionsByOptionId(optionId);
            if (option != null) {
                options.add(option);
            }
        }
        return options;
    }

    private String mergeOptionIds(String existingOptionIds, String newOptionIds) {
        if (existingOptionIds == null || existingOptionIds.equals("null")) {
            return newOptionIds;
        }
        String[] existingOptions = existingOptionIds.split("-");
        String[] newOptions = newOptionIds.split("-");

        StringBuilder mergedOptions = new StringBuilder(existingOptionIds);
        for (String newOption : newOptions) {
            boolean exists = false;
            for (String existingOption : existingOptions) {
                if (newOption.equals(existingOption)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                mergedOptions.append("-").append(newOption);
            }
        }
        return mergedOptions.toString();
    }

    private void processCartDetails(Model model, String memberId, String tripNoValue, String optionIdsValue, String cartNoValue, HttpServletResponse response) {
       System.out.println("======processCartDetails Processing======");

        int total_price = 0;
        int product_Num = 0;

        Trip trip = null;
        Cart cart= null;
        List<TripOption> options=null;
        if (!tripNoValue.equals("null")) {
            trip = tripService.getDetail(Integer.parseInt(tripNoValue));
            System.out.println("tripNoValue is not null");
        }

//        Trip trip = tripNoValue != null ? tripService.getDetail(Integer.parseInt(tripNoValue)) : null;
        if (trip != null) {
            total_price += trip.getTripPrice();
            product_Num++;

            if (optionIdsValue != null) {
                options = getOptions(optionIdsValue);
                for (TripOption option : options) {
                    total_price += option.getOptionPrice();
                    product_Num++;
                }
            }
        }

        if (optionIdsValue != null && !optionIdsValue.isEmpty()) {
            cartService.updateCart(memberId, tripNoValue, optionIdsValue, total_price);
            System.out.println("======Cart Update Complete======");
        }
        cart = cartService.getDetail(cartNoValue);
        System.out.println("Cart.TripNo="+cart.getTripNo());
        modelAttribute(model, trip, cart, total_price, product_Num, options);
    }
    //------------------------------
}
