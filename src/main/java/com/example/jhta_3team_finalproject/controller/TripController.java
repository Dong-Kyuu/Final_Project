package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.TourPackage.tripMailVO;
import com.example.jhta_3team_finalproject.domain.TourPackage.tripSendMail;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.service.Notification.SseService;
import com.example.jhta_3team_finalproject.service.TourPackage.*;
import com.example.jhta_3team_finalproject.domain.TourPackage.*;
import com.example.jhta_3team_finalproject.service.User.UserService;
import com.example.jhta_3team_finalproject.util.CookieService;
import com.example.jhta_3team_finalproject.util.PagingUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping(value = "/trip")
public class TripController {
    private static final Logger logger = LoggerFactory.getLogger(TripController.class);

    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private static final int UPDATE_SUCCESS = 1;
    private static final int JOIN_SUCCESS = 1;
    private final tripSendMail tripSendMail;
    private final TripService tripService;
    private final OptionService optionService;
    private final CartService cartService;
    private final PurchaseService purchaseService;
    private final SseService sseService;
    private final UserService userService;

    private static final int ANONYMOUS_CUSTOMER_NO = 0;


    @Autowired
    public TripController(CustomerService customerService, TripService tripService, CartService cartService, OptionService optionService, PasswordEncoder passwordEncoder, tripSendMail tripSendMail, PurchaseService purchaseService, SseService sseService, UserService userService) {
        this.customerService = customerService;
        this.tripService = tripService;
        this.cartService = cartService;
        this.optionService = optionService;
        this.passwordEncoder = passwordEncoder;
        this.tripSendMail = tripSendMail;
        this.purchaseService = purchaseService;
        this.sseService = sseService;
        this.userService = userService;
    }

    //로그인
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            ModelAndView mv,
            @CookieValue(value = "remember-me", required = false) Cookie readCookie,
            HttpSession session,
            Principal userPrincipal) {
        if (readCookie != null) {
        //principal.getName():로그인 한 아이디 값을 알 수 있어요
        logger.info("저장됭 아이디:" + userPrincipal.getName());
            mv.setViewName("redirect:trip/mainPage");
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
    @RequestMapping(value = "/join", method = RequestMethod.GET)
    public String join() {
        return "tourpackage/trip_join";
    }

    @RequestMapping(value = "/joinProcess", method = RequestMethod.POST)
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
            return "redirect:/trip/login";
        } else {
            model.addAttribute("url", request.getRequestURI());
            model.addAttribute("message", "회원가입 실패");
            return "error";
        }
    }

    //회원가입 폼에서 아이디 검사
    @ResponseBody
    @RequestMapping(value ="/idcheck",method=RequestMethod.GET)
    public int idcheck(@RequestParam("customerId") String id) {
        return customerService.isCustomerId(id);
    }

    @PostMapping("/loginProcess")
    public String loginProcess(@RequestParam("customerId") String customerId,
                               @RequestParam("customerPassword") String customerPassword,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        // DB에서 userId에 해당하는 사용자 정보를 가져옵니다.
        Customer customer = customerService.findByCustomerX("customerId",customerId);

        // 사용자 정보가 존재하고, 입력된 비밀번호와 DB에 저장된 비밀번호가 일치하는 경우
        if (customer != null && passwordEncoder.matches(customerPassword, customer.getCustomerPassword())) {
            // 세션에 사용자 정보를 저장합니다.
            session.setAttribute("customer", customer);
            // 로그인 성공 메시지를 Flash 속성으로 전달합니다.
            redirectAttributes.addFlashAttribute("loginSuccess", true);
            // 로그인 성공 후 이동할 페이지를 지정합니다. 여기서는 예시로 메인 페이지로 이동합니다.
            return "redirect:/trip/mainPage";
        } else {
            // 로그인 실패 메시지를 Flash 속성으로 전달합니다.
            redirectAttributes.addFlashAttribute("loginFail", true);
            // 로그인 실패 시 로그인 페이지로 다시 이동합니다.
            return "redirect:/trip/login";
        }
    }






    @GetMapping("/mainPage")
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

        int customerNo = customer != null ? customer.getCustomerNo() : 0;

        if(customerNo==ANONYMOUS_CUSTOMER_NO) {
            CookieService.deleteCookie(response, "cart"+customerNo);
            System.out.println("삭제");
            cartService.deleteCart("0");//비로그인회원에게 제공되는 cartNo="0"
        }
        // -----------------------------------------

        getTripPage(page,limit,category,sort,keyword,model);

        return "tourpackage/Trip_Page";
    }

    @GetMapping("/Detail")
    public String tripDetail(@RequestParam(name = "num") int num, HttpServletRequest request, HttpServletResponse response,Model model) {

        // 세션에서 "customer" 값을 가져옴
        HttpSession session = request.getSession(false);

        Customer customer = customerService.getcustomerBySession(session);

        model.addAttribute("customer",customer);

        int customerNo = customer != null ? customer.getCustomerNo() : 0;

        Trip trip = tripService.getDetail(num);
        if (trip == null) {
            model.addAttribute("message", "데이터를 읽지 못했습니다.");
            return "error/error";
        }

        String optionIds = tripService.getOptionIds(num);
        String[] optionId = optionIds.split("-");
        List<TripOption> options = new ArrayList<TripOption>();

        for (String s : optionId) {
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

        return "tourpackage/Trip_detail";
    }



    //카트 수정본
    @GetMapping("/Cart")
    public String tripCart(@RequestParam(name = "num", required = false) Integer num,
                           @RequestParam(name="selectedOptions", required = false) String selectedOptions,
                           HttpServletRequest request, HttpServletResponse response,Model model) {

        System.out.println("======TripCart Processing======");

        // 1.아이디 세션에서 불러오기
        // 세션에서 "customer" 값을 가져옴
        HttpSession session = request.getSession(false);

        Customer customer = customerService.getcustomerBySession(session);

        int customerNo = customer != null ? customer.getCustomerNo() : 0;

        model.addAttribute("customer",customer);

        int cartcheck = cartService.isId(String.valueOf(customerNo));// cart비었음:0 cart있음:1

        boolean isCookie = CookieService.isCookiePresent(request,"cart"+customerNo);
        String cookieValue;

        //쿠키가 없는데 카트는 존재하는 경우 카트 삭제
        if(!isCookie&&cartcheck==1){
            cartService.deleteCart(String.valueOf(customerNo));
           // cartService.insertCart(String.valueOf(customerNo),null,0,null);
        }

        int total_price = 0;
        int product_Num = 0;
        List<TripOption> options=null;


        if(!isCookie){
            //최초 추가 - 쿠키X URL로만 추가
            //isCookie=false ,num값 존재 ->cart 존재 여부
            //새로운 쿠키 생성
            if(num!=null){
                System.out.println("최초생성");

                if(selectedOptions!=null) {
                    options = optionService.getOptions(selectedOptions);
                    for (TripOption option : options) {
                        total_price += option.getOptionPrice();
                        product_Num++;
                    }
                }

                Trip trip = tripService.getDetail(num);
                total_price+=trip.getTripPrice();
                //카트 생성
                cartService.insertCart(String.valueOf(customerNo), String.valueOf(num),total_price,selectedOptions);

                //쿠키 생성
                Cart cart = cartService.getDetail(String.valueOf(customerNo));
                String newCookieValue = CookieService.serializeCartCookie(cart);
                CookieService.setCookie(response,"cart"+customerNo,newCookieValue);

                cartModelAttribute(cart,trip,options,total_price,product_Num,model);

            }
            //쿠키도 없고, 추가되는 내용도 없다면 빈화면
            else{
                System.out.println("빈화면");
                cartModelAttribute(null,null,null,0,0,model);

            }

        }else {//isCookie==true

            if(num!=null){
                System.out.println("업데이트");

                cookieValue = CookieService.getCookieValue(request,"cart"+customerNo);// 쿠키 내용을 가져옴

                String tripNoValue = CookieService.getValueBetweenEquals(cookieValue, "tripNo");
                String optionIdsValue = CookieService.getValueBetweenEquals(cookieValue, "optionIds");

                String newOptionIdsValue = optionService.mergeOptionIds(optionIdsValue,selectedOptions);

                if(newOptionIdsValue!=null) {
                    options = optionService.getOptions(newOptionIdsValue);
                    for (TripOption option : options) {
                        total_price += option.getOptionPrice();
                        product_Num++;
                    }
                }

                Trip trip= tripService.getDetail(Integer.parseInt(tripNoValue));
                Cart cart = cartService.getDetail(String.valueOf(customerNo));

                total_price+=trip.getTripPrice();

                if(cartcheck==1){
                    cartService.updateCart(String.valueOf(customerNo),tripNoValue,newOptionIdsValue,total_price);
                }else{
                    cartService.insertCart(String.valueOf(customerNo), tripNoValue,total_price,newOptionIdsValue);
                }

                cartModelAttribute(cart,trip,options,total_price,product_Num,model);

            }else{
                System.out.println("장바구니 직접 접근/쿠키 존재");

                cookieValue = CookieService.getCookieValue(request,"cart"+customerNo);// 쿠키 내용을 가져옴

                String tripNoValue = CookieService.getValueBetweenEquals(cookieValue, "tripNo");
                String optionIdsValue = CookieService.getValueBetweenEquals(cookieValue, "optionIds");

                if(optionIdsValue!=null) {
                    options = optionService.getOptions(optionIdsValue);
                    for (TripOption option : options) {
                        total_price += option.getOptionPrice();
                        product_Num++;
                    }
                }

                Trip trip= tripService.getDetail(Integer.parseInt(tripNoValue));
                Cart cart = cartService.getDetail(String.valueOf(customerNo));

                total_price+=trip.getTripPrice();

                if(cartcheck==1){
                    cartService.updateCart(String.valueOf(customerNo),tripNoValue,optionIdsValue,total_price);
                }else{
                    cartService.insertCart(String.valueOf(customerNo), tripNoValue,total_price,optionIdsValue);
                }

                cartModelAttribute(cart,trip,options,total_price,product_Num,model);

            }

        }

        return "tourpackage/Trip_cart";
    }

    private void cartModelAttribute(Cart cart, Trip trip, List<TripOption> options, int totalPrice, int productNum,Model model) {
        model.addAttribute("cart",cart);
        model.addAttribute("trip",trip);
        model.addAttribute("options",options);
        model.addAttribute("total_price",totalPrice);
        model.addAttribute("product_Num",productNum);
    }

    //---------------------------------------------

    @GetMapping("/Purchase")
    public String tripPurchase(HttpServletRequest request, HttpServletResponse response,Model model,@RequestParam(name = "loginNum", required = false) Integer loginNum) {

        System.out.println("loginNum="+loginNum);

        HttpSession session = request.getSession(false);
        Customer customer = customerService.getcustomerBySession(session);

        model.addAttribute("customer",customer);

        int customerNo = customer != null ? customer.getCustomerNo() : ANONYMOUS_CUSTOMER_NO;
        System.out.println("customerNo = " + customerNo);

        Cart cart = cartService.getDetail(String.valueOf(customerNo));
        model.addAttribute("cart",cart);

        Trip trip = tripService.getDetail(Integer.parseInt(cart.getTripNo()));
        model.addAttribute("trip",trip);

        String optionIds = cart.getOptionIds();

        List<TripOption> options = optionService.getOptions(optionIds);
        model.addAttribute("options",options);

        return "tourpackage/Trip_Purchase";
    }

    @GetMapping("/PurchaseSuccess")
    public String purchaseSuccess(HttpServletResponse cookieresponse) {
        CookieService.deleteCookie(cookieresponse, "cart");

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
                                                          HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("======Cart Delete Processing======");

        HttpSession session = request.getSession(false);
        Customer customer = customerService.getcustomerBySession(session);
        int customerNo = customer != null ? customer.getCustomerNo() : 0;

        TripOption tripOption;
        Cart cart;

        int optionPrice;
        int priceTotal = Integer.parseInt(cartTotal);

        String newCookie;
        String newURL = "";

        if ("trip".equals(itemType)) {//전부 삭제
            System.out.println("trip삭제");
            cartService.deleteCart(cartNo);
            // 쿠키를 완전히 삭제
            CookieService.deleteCookie(response, "cart"+ customerNo);
            if(!CookieService.isCookiePresent(request,"cart"+customerNo)){
                System.out.println("카트쿠키 삭제 완료");
            }else{
                System.out.println("카트쿠키 삭제 실패");
            }
            newURL = "Cart?";

        } else if ("option".equals(itemType)) {//특정 옵션 삭제
            System.out.println("option삭제");

            tripOption= optionService.getOptionsByOptionId(identifier);
            optionPrice = tripOption.getOptionPrice();

            // optionId을 삭제하는 DAO 호출
            String optionIds = cartService.getOptionIds(cartNo);
            String updatedOptionIds = optionService.removeOptionId(optionIds,identifier);

            String updatedCartTotal = String.valueOf(priceTotal - optionPrice);

            cartService.deleteOption(updatedOptionIds, cartNo, updatedCartTotal);

            cart = cartService.getDetail(cartNo);
            newCookie = CookieService.serializeCartCookie(cart);

            CookieService.setCookie(response, "cart"+customerNo,newCookie); // 쿠키 내용을 변경하는 메서드
            newURL = CookieService.CartcookieToURL(newCookie);

        } else {
            // 유효하지 않은 itemType에 대한 오류 처리
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid itemType");
        }

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("newURL", newURL);

        return  ResponseEntity.ok(responseMap);
    }

    @GetMapping("/tripRegister")
    public String tripRegister(Model model) {
        List<TripOption> optionlistAll = optionService.getAllOptions();
        model.addAttribute("optionlistAll", optionlistAll);
        Trip trip = new Trip();
        model.addAttribute("trip", trip);

        // 이전에 RedirectAttributes로 추가한 Flash attribute를 가져옴
        String message = (String) model.asMap().get("message");
        if (message != null) {
            // 가져온 메시지를 다시 모델에 추가하여 Thymeleaf에서 사용할 수 있도록 함
            model.addAttribute("message", message);
        }

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
                              @AuthenticationPrincipal User userDetails,
                              Model model, RedirectAttributes redirectAttributes) throws IOException {

            // Trip 객체 생성 및 저장
            Trip trip = tripService.setTripForRegAndUpdate(tripName,tripPrice,tripMaxStock,tripDate,expireDate,category,optionIds);

        try {
            tripService.saveTrip(trip, images);
            redirectAttributes.addFlashAttribute("message", "저장되었습니다");

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "저장에 실패했습니다");
            e.printStackTrace();
        }

        return "redirect:/trip/tripRegister";
    }

    @GetMapping("/optionRegister")
    public String optionRegister(Model model) {
        List<City> cities = optionService.getAllCities();
        model.addAttribute("cities", cities);

        // 이전에 RedirectAttributes로 추가한 Flash attribute를 가져옴
        String message = (String) model.asMap().get("message");
        if (message != null) {
            // 가져온 메시지를 다시 모델에 추가하여 Thymeleaf에서 사용할 수 있도록 함
            model.addAttribute("message", message);
        }

        return "tourdepartment/Tour_Option_Register";
    }

    @PostMapping("/addOptionTrip")
    public String addOptionTrip(@RequestParam(name ="OptionName", required = false) String optionName,
                                @RequestParam(name ="OptionPrice", required = false) Integer optionPrice,
                                @RequestParam(name ="OptionMaxStock", required = false) Integer optionMaxStock,
                                @RequestParam(name ="OptionDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate optionDate,
                                @RequestParam(name="cityNo",required =false) String cityNo,
                                @RequestParam(name ="img[]", required = false) MultipartFile[] images,
                                @AuthenticationPrincipal User userDetails,
                                Model model, RedirectAttributes redirectAttributes) throws IOException{

        //option 넘버 만들기
        String optionId = optionService.generateOptionId(cityNo);

        TripOption option = optionService.setTripForRegAndUpdate(optionId,optionName,optionPrice,optionMaxStock,optionDate,cityNo);

        try {
            optionService.saveOption(option,images);
            redirectAttributes.addFlashAttribute("message", "저장되었습니다");

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "저장에 실패했습니다");
            e.printStackTrace();
        }

        return "redirect:/trip/optionRegister";
    }

    @GetMapping("/tripUpdate")
    public String tripUpdate(Model model,
                             @RequestParam(name="num") int num) {
        List<TripOption> optionlistAll = optionService.getAllOptions();
        Trip trip = tripService.getDetail(num);
        TripFile tripFile = tripService.getTripFileByNo(String.valueOf(trip.getFileId()));
        String currentoption = trip.getOptionIds();

        List<TripOption> currentoptions = optionService.getOptions(currentoption);
        StringBuilder currentoptionsName= new StringBuilder();
        for(TripOption option : currentoptions){
            String optionName=option.getOptionName();
            System.out.println("optionName =" +optionName);
            if(!optionName.isEmpty()){
                currentoptionsName.append(optionName).append("-");
            }
        }
        if (!currentoptionsName.isEmpty()) {
            currentoptionsName.setLength(currentoptionsName.length() - 1);
        }

        model.addAttribute("optionlistAll", optionlistAll);
        model.addAttribute("trip", trip);
        model.addAttribute("tripfile", tripFile);
        model.addAttribute("categories", Arrays.asList("WEU", "CEU", "EEU", "SEU"));
        model.addAttribute("currentoptionsName", currentoptionsName.toString());
        return "tourdepartment/Tour_Update";
    }

    @PostMapping("/updateMainTrip")
    public String updateMainTrip(@RequestParam(name="tripNo") int num,
                                 @RequestParam(name ="TripName", required = false) String tripName,
                              @RequestParam(name ="TripPrice", required = false) Integer tripPrice,
                              @RequestParam(name ="TripMaxStock", required = false) Integer tripMaxStock,
                              @RequestParam(name ="tripDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tripDate,
                              @RequestParam(name ="expireDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expireDate,
                              @RequestParam(name ="TripCategory", required = false) String category,
                              @RequestParam(name ="optionIds", required = false) String optionIds,
                              @RequestParam(name ="img[]", required = false) MultipartFile[] images,
                              @AuthenticationPrincipal User userDetails,
                              Model model, RedirectAttributes redirectAttributes) throws IOException {

        // Trip 객체 생성 및 저장
        Trip trip = tripService.setTripForRegAndUpdate(tripName,tripPrice,tripMaxStock,tripDate,expireDate,category,optionIds);

        try {
            tripService.updateTrip(trip, images);
            redirectAttributes.addFlashAttribute("message", "저장되었습니다");

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "저장에 실패했습니다");
            e.printStackTrace();
        }

        return "redirect:/trip/tripRegister";
    }

    @GetMapping("/Department")
    public String tripDepartment(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "9") int limit,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model) {

        getTripPage(page,limit,category,sort,keyword,model);

        //purchase 정보 불러오기
        List<Purchase> purchaseList= getPurchaseListWithoutStatus("REJECTED","APPROVED");
        model.addAttribute("purchaseList",purchaseList);


        return "tourdepartment/Travel_Department";
    }

    @PostMapping("/approveTrip")
    public String approveTrip(@RequestParam("tripNo") int tripNo, RedirectAttributes redirectAttributes) {
        tripService.updateTripStatus(tripNo, "APPROVED");

        redirectAttributes.addFlashAttribute("message", "여행 일정을 승인하였습니다.");

        return "redirect:/trip/Department";
    }

    @PostMapping("/rejectTrip")
    public String rejectTrip(@RequestParam("tripNo") int tripNo,
                             RedirectAttributes redirectAttributes) {

        tripService.updateTripStatus(tripNo, "REJECTED");

        redirectAttributes.addFlashAttribute("message", "여행 일정을 거부하였습니다.");

        return "redirect:/trip/Department";
    }


    @PostMapping("/approvePurchase")
    public String approvePurchase(@RequestParam("tripNo") int tripNo,@RequestParam("purchaseId") int id,@RequestParam("buyerNo") String buyerNo, RedirectAttributes redirectAttributes) {

        Customer customer;
        customer = customerService.findByCustomerX("customerNo",buyerNo);
        String name = customer.getCustomerNameKor();

        Trip trip;
        trip = tripService.getDetail(tripNo);

        //예약확정 시 trip의 stock증가
        int maxstock = trip.getTripMaxStock();
        int stock = trip.getTripStock();

        String message="";
        if(maxstock>stock){

            boolean isUpdated =tripService.updateTripStock(tripNo,stock+1);
            if(isUpdated){
                purchaseService.updatePurchaseStatus(id,"APPROVED");
                message = name+"님의 예약을 승인하였습니다.";
            }else{
                message= "재고 업데이트에 실패했습니다.";
            }

        }else{
            message="예약이 이미 가득 차있습니다.";
        }

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/trip/Department";

    }

    @PostMapping("/rejectPurchase")
    public String rejectPurchase(@RequestParam("tripNo") int tripNo,@RequestParam("purchaseId") int id,
                                 @RequestParam("buyerNo") String buyerNo,@RequestParam("rejectReason") String rejectReason
                                 ,RedirectAttributes redirectAttributes) {

        System.out.println(rejectReason);

        Customer customer =null;
        customer = customerService.findByCustomerX("customerNo",buyerNo);
        String name = customer.getCustomerNameKor();

        Trip trip;
        trip = tripService.getDetail(tripNo);

        //예약거절 시 trip의 stock감소
        int stock = trip.getTripStock();

        String message="";

        purchaseService.updatePurchaseStatus(id,"REJECTED");
        purchaseService.updateRejectReason(id,rejectReason);

        boolean isUpdated=true;
        if(stock!=0){
            isUpdated =tripService.updateTripStock(tripNo,stock-1);
        }

        if(isUpdated){
            purchaseService.updatePurchaseStatus(id,"REJECTED");
            message = name+"님의 예약을 거절하였습니다.";
        }else{
            message= "재고 업데이트에 실패했습니다.";
        }

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/trip/Department";
    }


    @GetMapping("/TLManagement")
    public String TLManagement(@RequestParam(name = "num", required = false) Integer num,
            HttpServletRequest request,HttpServletResponse response,
            Model model) {

        Trip trip = tripService.getDetail(num);
        model.addAttribute("trip", trip);

        //purchase 정보 불러오기 조건: tripNo
        List<Purchase> purchaseList= getPurchaseListWithoutStatus("REJECTED","PENDING");
        List<Purchase> newpurchaseList =new ArrayList<>();
        for(Purchase p : purchaseList){
            if(p.getTripNo()==num){
                newpurchaseList.add(p);
            }
        }
        model.addAttribute("purchaseList",newpurchaseList);

        //가이드 선출
        List<User> users = userService.getEmployeeListByDepartment(5);
        // positionId가 3 이하인 user만 필터링하여 저장
        List<User> travelleader = new ArrayList<>();
        for (User user : users) {
            if (user.getPositionId() <= 3) {
                travelleader.add(user);
            }
        }
        model.addAttribute("travelleader",travelleader);

        //가이드 존재시 가이드 이름 가져오기
        User user;
        int TLNo = trip.getTravelleaderNo();
        String userName = "트래블리더";
        if(TLNo!=0){
            user = userService.getEmployee(TLNo);
            userName = user.getUsername();

        }
        model.addAttribute("userName",userName);

        return "tourdepartment/Tour_Management";
    }

    @PostMapping("/updateTravelLeader")
    public ResponseEntity<?> updateTravelLeader(@RequestBody Map<String, String> request) {
        int tripId = Integer.parseInt(request.get("tripId"));
        int userNo = Integer.parseInt(request.get("userNo"));
        System.out.println(tripId);

        boolean isUpdated = tripService.updateTravelLeader(tripId, userNo);

        if (isUpdated) {
            return ResponseEntity.ok().body(Map.of("success", true));
        } else {
            return ResponseEntity.ok().body(Map.of("success", false));
        }
    }

    @GetMapping("/tripBoss")
    public String tripBoss(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "9") int limit,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model) {

        getTripPage(page,limit,category,sort,keyword,model);

        List<Purchase> purchaseList = getPurchaseListWithoutStatus("APPROVED","PENDING");
        model.addAttribute("purchaseList",purchaseList);

        List<Trip> newtripList = new ArrayList<>();
        String status ="";
        User user;
        String TLName="";

        List<Trip> triplistAll = tripService.getAllTrip();
        for(Trip trip : triplistAll){
            status = trip.getStatus();
            if(status.equals("APPROVED")){

                if(trip.getTravelleaderNo()!=0){
                    user = userService.getEmployee(trip.getTravelleaderNo());
                    TLName=user.getUsername();
                    trip.setTravelleaderName(TLName);
                }
                newtripList.add(trip);
            }
        }
        model.addAttribute("newtripList",newtripList);

        return "tourdepartment/Travel_BossPage";
    }

    @GetMapping("/tripSales")
    public String tripSales(
            HttpServletRequest request,HttpServletResponse response,
            Model model) {

        return "tourdepartment/Tour_Sales";
    }

    public List<Purchase> getPurchaseListWithoutStatus(String status1,String status2){
        //purchase 정보 불러오기
        List<Purchase> purchaseList= purchaseService.getAllPurchaseInfo();
        List<Purchase> newpurchaseList= new ArrayList<>();
        for(Purchase p : purchaseList){
            if(!(p.getStatus().equals(status1)||p.getStatus().equals(status2))){
                newpurchaseList.add(p);
            }
        }
        return newpurchaseList;
    }

    private void getTripPage(int page, int limit, String category, String sort, String keyword, Model model) {
        int startRow = (page - 1) * limit + 1;
        int endRow = startRow + limit - 1;

        int listcount = tripService.getListcount(category,keyword);
        List<Trip> triplist = tripService.getTriplist(category,keyword,startRow,endRow,sort);

        PagingUtil.Paging pageService = new PagingUtil.Paging(page,limit,listcount);

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

        //trip status가 APPROVED인 tripList
        List<Trip> approvedtripList = tripService.getApprovedTrip();
        model.addAttribute("approvedTrip",approvedtripList);

        //trip status가 PENDING인 tripList
        List<Trip> pendingtripList = tripService.getPendingTrip();
        model.addAttribute("pendingTrip",pendingtripList);

        // 이전에 RedirectAttributes로 추가한 Flash attribute를 가져옴
        String message = (String) model.asMap().get("message");
        if (message != null) {
            // 가져온 메시지를 다시 모델에 추가하여 Thymeleaf에서 사용할 수 있도록 함
            model.addAttribute("message", message);
        }
    }
}

