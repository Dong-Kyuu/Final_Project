package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.TourPackage.tripMailVO;
import com.example.jhta_3team_finalproject.domain.TourPackage.tripSendMail;
import com.example.jhta_3team_finalproject.service.TourPackage.*;
import com.example.jhta_3team_finalproject.domain.TourPackage.*;
import com.example.jhta_3team_finalproject.util.PagingUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class TripController {
    private static final Logger logger = LoggerFactory.getLogger(TripController.class);

    private CustomerService customerService;
    private PasswordEncoder passwordEncoder;

    private static final int UPDATE_SUCCESS = 1;
    private static final int JOIN_SUCCESS = 1;
    private tripSendMail tripSendMail;


    private TripService tripService;
    private OptionService optionService;
    private CartService cartService;

    private static final int ANONYMOUS_CUSTOMER_NO = 0;


    @Autowired
    public TripController(CustomerService customerService, TripService tripService, CartService cartService, OptionService optionService, PasswordEncoder passwordEncoder, tripSendMail tripSendMail) {
        this.customerService = customerService;
        this.tripService = tripService;
        this.cartService = cartService;
        this.optionService = optionService;
        this.passwordEncoder = passwordEncoder;
        this.tripSendMail = tripSendMail;
    }

    //로그인
    @RequestMapping(value = "/triplogin", method = RequestMethod.GET)
    public ModelAndView login(
            ModelAndView mv,
            @CookieValue(value = "remember-me", required = false) Cookie readCookie,
            HttpSession session,
            Principal userPrincipal) {
        if (readCookie != null) {
        //principal.getName():로그인 한 아이디 값을 알 수 있어요
        logger.info("저장됭 아이디:" + userPrincipal.getName());
            mv.setViewName("redirect:/tripPage");
       // mv.setViewName("redirect:/board/list");??어디로 가는거지
    } else {
        mv.setViewName("tourpackage/trip_login");

        //세션에 저장된 값을 한 번만 실행 될 수 있도록 model에 저장
        mv.addObject("loginfail", session.getAttribute("loginfail"));
        session.removeAttribute("loginfail");//세션의 값은 제거합니다
    }
        logger.info("login 페이지");
        return mv;
    }

    //회원가입
    @RequestMapping(value = "/tripjoin", method = RequestMethod.GET)
    public String join() {
        return "tourpackage/trip_join";
    }


    @RequestMapping(value = "/tripjoinProcess", method = RequestMethod.POST)
    public String joinProcess(Customer customer , Model model, RedirectAttributes rattr, HttpServletRequest request) {
        System.out.println(customer.toString());
        customer.setCustomerPassword(passwordEncoder.encode(customer.getCustomerPassword()));

        logger.info(("User: " + customer.toString()));
        int result= customerService.join(customer);

        if(result == JOIN_SUCCESS) {
            //회원가입 성공 시 메일 전송
            tripMailVO vo = new tripMailVO();
            vo.setTo(customer.getCustomerEmail());
            tripSendMail.sendMail(vo);
            logger.info(tripSendMail +"확인");

            rattr.addFlashAttribute("result", "joinSuccess");
            return "redirect:/triplogin";
        } else {
            model.addAttribute("url", request.getRequestURI());
            model.addAttribute("message", "회원가입 실패");
            return "error";
        }
    }

    //회원가입 폼에서 아이디 검사
    @ResponseBody
    @RequestMapping(value ="/tripidcheck",method=RequestMethod.GET)
    public int idcheck(@RequestParam("customerId") String id) {
        return customerService.getcustomerId(id);
    }

    @PostMapping("/triploginProcess")
    public String loginProcess(@RequestParam("customerId") String customerId,
                               @RequestParam("customerPassword") String customerPassword,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        // DB에서 userId에 해당하는 사용자 정보를 가져옵니다.
        Customer customer = customerService.findByCustomerId(customerId);

        // 사용자 정보가 존재하고, 입력된 비밀번호와 DB에 저장된 비밀번호가 일치하는 경우
        if (customer != null && passwordEncoder.matches(customerPassword, customer.getCustomerPassword())) {
            // 세션에 사용자 정보를 저장합니다.
            session.setAttribute("customer", customer);
            // 로그인 성공 메시지를 Flash 속성으로 전달합니다.
            redirectAttributes.addFlashAttribute("loginSuccess", true);
            // 로그인 성공 후 이동할 페이지를 지정합니다. 여기서는 예시로 메인 페이지로 이동합니다.
            return "redirect:/tripPage";
        } else {
            // 로그인 실패 메시지를 Flash 속성으로 전달합니다.
            redirectAttributes.addFlashAttribute("loginFail", true);
            // 로그인 실패 시 로그인 페이지로 다시 이동합니다.
            return "redirect:/triplogin";
        }
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

        // 세션에서 "customer" 값을 가져옴
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer != null) {
            // customer 정보가 있는 경우 추가 작업 수행
            System.out.println("로그인된 사용자: " + customer.getCustomerId());
        } else {
            // customer 정보가 없는 경우
            System.out.println("로그인된 사용자가 없습니다.");
        }

        model.addAttribute("customer",customer);

        // -----------------------------------------
        // 산 - 카트 쿠키가 자기 자신의 쿠키가 아니면 삭제
        // cartNo가 mem_id와 동일하면 유지
        // 로그인하지않은 상태라면 항상 카트 쿠키 / 카트 db 초기화
        //아이디 세션에서 불러오기
       // HttpSession session = request.getSession(); // 세션이 없으면 새로 생성하지 않음
        int customerNo = customer != null ? customer.getCustomerNo() : 0;

        // customerNo 이용하여 필요한 작업 수행
        String cookieValue = getCookieValue(request);// 쿠키 내용을 갖고오는 메서드

        DeleteCartCookie(cookieValue,customerNo,response);
/*
        if(cookieValue!=null) {
            String cartNoValue = getValueBetweenEquals(cookieValue, "cartNo");
            if(!cartNoValue.equals(String.valueOf(customerNo))) {//쿠키의 cartNo와 mem_id비교
                deleteCookie(response, "cart");
                System.out.println("<카트 쿠키 삭제>");
            }
        }
*/
        if(customerNo==ANONYMOUS_CUSTOMER_NO) {
            deleteCookie(response, "cart");
            System.out.println("삭제");
            cartService.deleteCart("0");//비로그인회원에게 제공되는 cartNo="0"
        }
        // -----------------------------------------


        int startRow = (page - 1) * limit + 1;
        int endRow = startRow + limit - 1;

        int listcount = this.getListcount(category,keyword);
        List<Trip> triplist = this.getTriplist(category,keyword,startRow,endRow,sort);
/*
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
*/

        PagingUtil.Paging pageService = new PagingUtil.Paging(page,limit,listcount);
        /*
        int maxpage = (listcount + limit - 1) / limit;
        int startpage = ((page - 1) / 10) * 10 + 1;
        int endpage = startpage + 10 - 1;
        if (endpage > maxpage) {
            endpage = maxpage;
        }

        int pagefirst = (page - 1) * limit + 1;//startrow
        int pagelast = Math.min(pagefirst + limit - 1, listcount);//endrow
*/
        model.addAttribute("page", page);
        model.addAttribute("maxpage", pageService.getMaxpage());
        model.addAttribute("startpage", pageService.getStartpage());
        model.addAttribute("endpage", pageService.getEndpage());
        model.addAttribute("listcount", listcount);
        model.addAttribute("triplist", triplist);
        model.addAttribute("limit", limit);
        model.addAttribute("pagefirst", pageService.getPagefirst());
        model.addAttribute("pagelast", pageService.getPagelast());
        model.addAttribute("sort", sort);  // 현재 정렬 기준 추가
        model.addAttribute("keyword", keyword);  // 현재 검색어 추가
        return "tourpackage/Trip_Page";
    }

    @GetMapping("/tripDetail")
    public String tripDetail(@RequestParam(name = "num") int num, HttpServletRequest request, HttpServletResponse response,Model model) {

        // 세션에서 "customer" 값을 가져옴
        HttpSession session = request.getSession(false);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer != null) {
            // customer 정보가 있는 경우 추가 작업 수행
            System.out.println("로그인된 사용자: " + customer.getCustomerId());
        } else {
            // customer 정보가 없는 경우
            System.out.println("로그인된 사용자가 없습니다.");
        }
        model.addAttribute("customer",customer);

        int customerNo = customer != null ? customer.getCustomerNo() : 0;

        System.out.println("customerNo ="+customerNo);

        // customerNo 이용하여 필요한 작업 수행
        String cookieValue = getCookieValue(request);// 쿠키 내용을 갖고오는 메서드

        DeleteCartCookie(cookieValue,customerNo,response);
        /*
        if(cookieValue!=null) {
            String cartNoValue = getValueBetweenEquals(cookieValue, "cartNo");
            if(!cartNoValue.equals(String.valueOf(customerNo))) {//쿠키의 cartNo와 mem_id비교
                deleteCookie(response, "cart");
                System.out.println("<카트 쿠키 삭제>");
            }
        }
*/
        if(customerNo==ANONYMOUS_CUSTOMER_NO) {
            System.out.println("삭제");
            deleteCookie(response, "cart");
            cartService.deleteCart("0");//비로그인회원에게 제공되는 cartNo="0"
        }
        // -----------------------------------------

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

        TripFile tripFile = tripService.getTripFileByNo(trip.getFileId());

        Cart cart = cartService.getDetail(String.valueOf(customerNo));
        int check = cartService.isId(String.valueOf(customerNo));

        String cartTripNo=String.valueOf(num);
        if(cart!=null){
            cartTripNo =cart.getTripNo();
        }

        System.out.println("cartTripNo="+cartTripNo);

        model.addAttribute("trip", trip);
        model.addAttribute("options", options);
        model.addAttribute("tripFile", tripFile);
        model.addAttribute("tripNo", num);
        model.addAttribute("check", check);
        model.addAttribute("cartTripNo", cartTripNo);

        return "tourpackage/Trip_Detail";
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

        // ---------------------
        // 우선 카트를 생성 만약 카트가 이미 있다면 생성 x 카트는 아이디와 동일

        // 1.아이디 세션에서 불러오기
        // 세션에서 "customer" 값을 가져옴
        HttpSession session = request.getSession(false);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer != null) {
            // customer 정보가 있는 경우 추가 작업 수행
            System.out.println("로그인된 사용자: " + customer.getCustomerId());
        } else {
            // customer 정보가 없는 경우
            System.out.println("로그인된 사용자가 없습니다.");
        }
        model.addAttribute("customer",customer);

        int customerNo = customer != null ? customer.getCustomerNo() : 0;

        System.out.println("customerId = "+customerNo);

        // 2.카트의 존재여부 확인
        int cartcheck = cartService.isId(String.valueOf(customerNo));// cart비었음:0 cart있음:1
        System.out.println("cartcheck = " + cartcheck);

        /*
        if(customerNo==ANONYMOUS_CUSTOMER_NO ||cartcheck==0){
            cartService.insertCart(String.valueOf(customerNo),null,0,null);
            System.out.println("insert CartNo 0");
        }
*/
        // 3. 쿠키 존재 여부 확인
        String cookieValue = getCookieValue(request);// 쿠키 내용을 갖고오는 메서드
        System.out.println("cookieValue = " + cookieValue);

        // 4. 주소로 불려오는 num 를 확인
        String check = request.getParameter("num");
        System.out.println("check = " + check);

        // ---------------------
        if (cartcheck==0 && cookieValue == null && check != null) {
            // CASE 1: First time adding to cart, no cookie
            System.out.println("======CASE 1======");
            handleFirstTimeAddToCart(model, response, String.valueOf(customerNo), num, selectedOptions);
        } else if (cartcheck == 0 && cookieValue == null) {
            // CASE 2: Cart is empty, no cookie
            System.out.println("======CASE 2======");
        } else if (cartcheck == 1 && cookieValue != null && check != null) {
            // CASE 3: Cookie exists, adding new items
            System.out.println("======CASE 3======");
            handleAddToCartWithExistingCookie(model, response, String.valueOf(customerNo), cookieValue, selectedOptions);
        } else if (cookieValue != null && check == null) {
            System.out.println("======CASE 4======");
            // CASE 4: Cookie exists, no num parameter
            handleViewCartWithCookie(model, cookieValue);
        } else if (cartcheck == 1 && cookieValue == null &&check==null) {
            System.out.println("======CASE 5======");
            // CASE 5: Cart exists, no cookie
            handleViewCartWithoutCookie(model, String.valueOf(customerNo));
        } else if(cartcheck==1&&cookieValue==null&&check!=null){
            //CASE 6 : Add new items
            System.out.println("======CASE 6======");
            handleAddNewItems(model,selectedOptions,customerNo);
        }

        return "tourpackage/Trip_cart";
    }

    //Cart 메서드-----------------------------------
    private void handleFirstTimeAddToCart(Model model, HttpServletResponse response,
                                            String memberId, int num, String selectedOptions) {
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
        System.out.println("selectedOptions = " + selectedOptions);
        String cartNoValue = getValueBetweenEquals(cookieValue, "cartNo");
        String tripNoValue = getValueBetweenEquals(cookieValue, "tripNo");
        String optionIdsValue = getValueBetweenEquals(cookieValue, "optionIds");
        String newOptionIds = "";
        System.out.println("Values =>"+cartNoValue+","+tripNoValue+","+selectedOptions);

        if (selectedOptions != null && !selectedOptions.isEmpty()) {
            System.out.println("======selectedOption Processing======");
            newOptionIds = mergeOptionIds(optionIdsValue, selectedOptions);
            String updatedCookieValue = cookieValue.replaceAll("optionIds=[^&]*", "optionIds=" + newOptionIds);
            updateCookie(response, updatedCookieValue);
        }

        processCartDetails(model, memberId, tripNoValue, selectedOptions, cartNoValue, response);
    }

    private void handleViewCartWithCookie(Model model, String cookieValue) {
        String cartNoValue = getValueBetweenEquals(cookieValue, "cartNo");
        String tripNoValue = getValueBetweenEquals(cookieValue, "tripNo");
        String optionIdsValue = getValueBetweenEquals(cookieValue, "optionIds");
        processCartDetails(model, null, tripNoValue, optionIdsValue, cartNoValue, null);
    }

    private void handleViewCartWithoutCookie(Model model, String memberId) {
        Cart cart = cartService.getDetail(memberId);
        String tripNoValue = cart.getTripNo();
        String optionIdsValue = cart.getOptionIds();
        processCartDetails(model, memberId, tripNoValue, optionIdsValue, memberId, null);
    }

    private void handleAddNewItems(Model model, String selectedOptions, int customerNo){
        Cart cart = cartService.getDetail(String.valueOf(customerNo));

        String OriginalOptions = cart.getOptionIds();

        String NewOptionIds= mergeOptionIds(OriginalOptions,selectedOptions);
        System.out.println(NewOptionIds);

        processCartDetails(model, String.valueOf(customerNo), cart.getTripNo(), NewOptionIds, String.valueOf(customerNo), null);

    }
    //---------------------------------------------

    @GetMapping("/tripPurchase")
    public String tripPurchase(HttpServletRequest request, HttpServletResponse response,Model model) {

        HttpSession session = request.getSession(false);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer != null) {
            // customer 정보가 있는 경우 추가 작업 수행
            System.out.println("로그인된 사용자: " + customer.getCustomerId());
        } else {
            // customer 정보가 없는 경우
            System.out.println("로그인된 사용자가 없습니다.");
            customer = customerService.findByCustomerId("rlatks15");//임시
        }

        model.addAttribute("customer",customer);

        int customerNo = customer != null ? customer.getCustomerNo() : ANONYMOUS_CUSTOMER_NO;
        System.out.println("customerNo = " + customerNo);

        System.out.println("customerNo = " + customerNo);

        Cart cart = cartService.getDetail(String.valueOf(customerNo));
        model.addAttribute("cart",cart);

        Trip trip = tripService.getDetail(Integer.parseInt(cart.getTripNo()));
        model.addAttribute("trip",trip);

        String optionIds = cart.getOptionIds();

        List<TripOption> options = getOptions(optionIds);
        model.addAttribute("options",options);

        return "tourpackage/Trip_Purchase";
    }

    @GetMapping("/PurchaseSuccess")
    public String purchaseSuccess() {
        return"tourpackage/PurchaseSuccess";
    }

    @PostMapping("/UpdateCustomer")
    public ResponseEntity<String> updateCustomer(@RequestBody Customer customer) {

        System.out.println("udaptecustomer start");

        System.out.println("customer = " + customer.getCustomerId());

        customerService.update(customer);
        return ResponseEntity.ok("Customer information updated successfully");
    }




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

    @GetMapping("/tripRegister")
    public String tripRegister(Model model) {
        List<TripOption> optionlistAll = optionService.getAllOptions();
        model.addAttribute("optionlistAll", optionlistAll);
        Trip trip = new Trip();
        model.addAttribute("trip", trip);
        return "tourdepartment/Tour_Register";
    }


    @PostMapping("/addMainTrip")
    public String addMainTrip(@RequestParam(name ="TripName", required = false) String tripName,
                              @RequestParam(name ="TripPrice", required = false) Integer tripPrice,
                              @RequestParam(name ="TripMaxStock", required = false) Integer tripMaxStock,
                              @RequestParam(name ="tripDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tripDate,
                              @RequestParam(name ="ExprDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expireDate,
                              @RequestParam(name ="TripCategory", required = false) String category,
                              @RequestParam(name ="optionIds", required = false) String optionIds,
                              @RequestParam(name ="img[]", required = false) MultipartFile[] images,
                              Model model) throws IOException {
        System.out.println("====== Received Parameters ======");
        System.out.println("======addMainTrip tripName=" + tripName);
        System.out.println("======addMainTrip tripPrice=" + tripPrice);
        System.out.println("======addMainTrip tripMaxStock=" + tripMaxStock);
        System.out.println("======addMainTrip expireDate=" + expireDate);
        System.out.println("======addMainTrip category=" + category);
        System.out.println("======addMainTrip optionIds=" + optionIds);

        if (images != null && images.length > 0) {
            for (int i = 0; i < images.length; i++) {
                MultipartFile image = images[i];
                System.out.println("======addMainTrip image[" + i + "] name=" + image.getOriginalFilename());
            }
        } else {
            System.out.println("======addMainTrip No images uploaded");
        }

            // Trip 객체 생성 및 저장
            Trip trip = new Trip();
            trip.setTripName(tripName);
            trip.setTripPrice(tripPrice != null ? tripPrice : 0); // Use default value if null
            trip.setTripMaxStock(tripMaxStock != null ? tripMaxStock : 0); // Use default value if null
            trip.setTripDate(tripDate.toString());
            trip.setExpireDate(expireDate.toString());
            trip.setTripCategory(category);
            trip.setOptionIds(optionIds);

            tripService.saveTrip(trip, images);

            System.out.println("success save");

        return "redirect:/tripRegister";
    }

    @GetMapping("/tripUpdate")
    public String tripUpdate(Model model,
                             @RequestParam(name="num") int num) {
        List<TripOption> optionlistAll = optionService.getAllOptions();
        Trip trip = tripService.getDetail(num);
        TripFile tripFile = tripService.getTripFileByNo(String.valueOf(num));

        model.addAttribute("optionlistAll", optionlistAll);
        model.addAttribute("trip", trip);
        model.addAttribute("tripfile", tripFile);

        return "tourdepartment/Tour_Update";
    }

    @GetMapping("/tripDepartment")
    public String tripDepartment(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "9") int limit,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "keyword", required = false) String keyword,
            HttpServletRequest request,HttpServletResponse response,
            Model model) {

        // -----------------------------------------
        // tripPage내용과 동일
        HttpSession session = request.getSession(); // 세션이 없으면 새로 생성하지 않음
        String memberId = (String) session.getAttribute("member_id");// 세션에서 "member_id" 값을 가져옴
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

        int startRow = (page - 1) * limit + 1;
        int endRow = startRow + limit - 1;

        int listcount = this.getListcount(category,keyword);
        List<Trip> triplist = this.getTriplist(category,keyword,startRow,endRow,sort);


        //
        PagingUtil.Paging pageService = new PagingUtil.Paging(page,limit,listcount);

     /*
int maxpage = (listcount + limit - 1) / limit;
int startpage = ((page - 1) / 10) * 10 + 1;
int endpage = startpage + 10 - 1;
if (endpage > maxpage) {
    endpage = maxpage;
}

int pagefirst = (page - 1) * limit + 1;//startrow
int pagelast = Math.min(pagefirst + limit - 1, listcount);//endrow
*/

        model.addAttribute("page", page);
        model.addAttribute("maxpage",pageService.getMaxpage());
        model.addAttribute("startpage",pageService.getStartpage());
        model.addAttribute("endpage", pageService.getEndpage());
        model.addAttribute("listcount", listcount);
        model.addAttribute("triplist", triplist);
        model.addAttribute("limit", limit);
        model.addAttribute("pagefirst", pageService.getPagefirst());
        model.addAttribute("pagelast", pageService.getPagelast());
        model.addAttribute("sort", sort);  // 현재 정렬 기준 추가
        model.addAttribute("keyword", keyword);  // 현재 검색어 추가
        //--------------------------------------------------------------------------------

        List<Trip> triplistAll = tripService.getAllTrip();
        List<TripOption> optionlistAll = optionService.getAllOptions();

        model.addAttribute("triplistAll",triplistAll);
        model.addAttribute("optionlistAll",optionlistAll);

        return "tourdepartment/Travel_Department";
    }

    @GetMapping("/TLManagement")
    public String TLManagement(@RequestParam(name = "num", required = false) Integer num,
            HttpServletRequest request,HttpServletResponse response,
            Model model) {

        Trip trip = tripService.getDetail(num);

        model.addAttribute("trip", trip);
        return "tourdepartment/Tour_Management";
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
        Cart cart;
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

//---------
    private int getListcount(String category,String keyword){
        if (isValidString(category)) {
            return tripService.getCategoryListCount(category);
        } else if (isValidString(keyword)) {
            return tripService.getKeywordListCount(keyword);
        } else {
            return tripService.getListCount();
        }
    }

    private List<Trip> getTriplist(String category,String keyword,int startRow,int endRow,String sort){
        if (isValidString(category)) {
            return tripService.getCategoryTripList(startRow, endRow,category, sort);
        } else if (isValidString(keyword)) {
            return tripService.getTripListByKeyword(startRow, endRow,keyword,  sort);
        } else {
            return tripService.getTripList(startRow, endRow, sort);
        }
    }

    private boolean isValidString(String str) {
        return str != null && !str.isEmpty() && !Objects.equals(str, "null");
    }
//--------------

    private void DeleteCartCookie(String cookieValue, int customerNo, HttpServletResponse response) {
        if(cookieValue!=null) {
            String cartNoValue = getValueBetweenEquals(cookieValue, "cartNo");
            if(!cartNoValue.equals(String.valueOf(customerNo))) {//쿠키의 cartNo와 mem_id비교
                deleteCookie(response, "cart");
                System.out.println("<카트 쿠키 삭제>");
            }
        }
    }

    //------------------------------
}
