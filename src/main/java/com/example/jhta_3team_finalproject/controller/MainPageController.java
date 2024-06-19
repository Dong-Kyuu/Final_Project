package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.Board.AnnounceBoard;
import com.example.jhta_3team_finalproject.domain.TourPackage.Purchase;
import com.example.jhta_3team_finalproject.domain.TourPackage.Trip;
import com.example.jhta_3team_finalproject.domain.TourPackage.TripOption;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.domain.calendar.Calendar;
import com.example.jhta_3team_finalproject.domain.dashboard.DashAnnounceBoard;
import com.example.jhta_3team_finalproject.service.TourPackage.PurchaseService;
import com.example.jhta_3team_finalproject.service.dashboard.*;
import com.example.jhta_3team_finalproject.service.User.UserService;
import com.example.jhta_3team_finalproject.service.dashboard.DashTripService;
import com.example.jhta_3team_finalproject.util.PagingUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class MainPageController {
    private final UserService userservice;
    private DashAnnounceBoardService dashAnnounceBoardService;
    private DashBoardCalendarService dashBoardCalendarService;
    private DashTripService dashTripService;
    private DashPurchaseService dashPurchaseService;

    private static final Logger logger = LoggerFactory.getLogger(AnnounceBoardController.class);

    @Autowired
    public MainPageController(UserService userService, DashBoardCalendarService dashBoardCalendarService,
                              DashAnnounceBoardService dashAnnounceBoardService, DashTripService dashTripService,
                              DashPurchaseService dashPurchaseService) {
        this.userservice = userService;
        this.dashAnnounceBoardService = dashAnnounceBoardService;
        this.dashBoardCalendarService = dashBoardCalendarService;
        this.dashTripService=dashTripService;
        this.dashPurchaseService=dashPurchaseService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/user/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal User userDetails, Model model) {
        User dbuser = userservice.departmentPositionInfo(userDetails.getDepartmentId(), userDetails.getPositionId());

        userDetails.setDepartmentName(dbuser.getDepartmentName());
        userDetails.setPositionName(dbuser.getPositionName());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //캘린더 당일 일정 가져오기
        List<Calendar> list = dashBoardCalendarService.select();
        //캘린더-list
        model.addAttribute("list", list);



        return "dashboard/dashboard-page";
    }

    // 리스트 가져오기
    @RequestMapping(value = "/dashAnnounceList", method = RequestMethod.GET)
    public ModelAndView freeTable(@RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "searchField", defaultValue = "-1") int index,
                                  @RequestParam(value = "search", defaultValue = "") String searchWord,
                                  @RequestParam(value = "targetDepartment", defaultValue = "") String targetDepartment,
                                  ModelAndView mv) {



        int limit = 10; // 한 화면에 출력할 로우 갯수
        int listcount = dashAnnounceBoardService.getListCount(index, searchWord, targetDepartment); // 총 리스트 수를 받아온다.
        logger.info("listcount:" + listcount);
        // 총 페이지 수
        PagingUtil.Paging pagingUtil = new PagingUtil.Paging(page, limit, listcount);

        List<AnnounceBoard> boardlist = dashAnnounceBoardService.getBoardList(index, searchWord, targetDepartment, page, limit); // 리스트를 받아옴

        mv.setViewName("dashboard/dashAnnounceBoard");
        mv.addObject("page", page);
        mv.addObject("maxpage", pagingUtil.getMaxpage());
        mv.addObject("startpage", pagingUtil.getStartpage());
        mv.addObject("endpage", pagingUtil.getEndpage());
        mv.addObject("rownum", pagingUtil.getRowNum());
        mv.addObject("listcount", listcount);
        mv.addObject("boardlist", boardlist);
        mv.addObject("limit", limit);
        mv.addObject("search_field", index);
        mv.addObject("search_word", searchWord);
        mv.addObject("targetDepartment", targetDepartment);

        logger.info("StartPage="+ String.valueOf(pagingUtil.getStartpage()));
        return mv;

    }


    //여행 결재리스트
    @GetMapping("/dashDepartment")
    public String tripDepartment(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "9") int limit,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "keyword", required = false) String keyword,
            HttpServletRequest request, HttpServletResponse response,
            Model model) {

        int startRow = (page - 1) * limit + 1;
        int endRow = startRow + limit - 1;

        int listcount = this.getListcount(category, keyword);
        List<Trip> triplist = this.getTriplist(category, keyword, startRow, endRow, sort);

        PagingUtil.Paging pageService = new PagingUtil.Paging(page, limit, listcount);

        model.addAttribute("page", page);
        model.addAttribute("maxpage", pageService.getMaxpage());
        model.addAttribute("startpage", pageService.getStartpage());
        model.addAttribute("endpage", pageService.getEndpage());
        model.addAttribute("listcount", listcount);
        model.addAttribute("triplist", triplist);
        System.out.println("trip="+triplist.size());
        model.addAttribute("limit", limit);
        model.addAttribute("pagefirst", pageService.getPagefirst());
        model.addAttribute("pagelast", pageService.getPagelast());
        model.addAttribute("sort", sort);  // 현재 정렬 기준 추가
        model.addAttribute("keyword", keyword);  // 현재 검색어 추가
        //--------------------------------------------------------------------------------

//        List<Trip> triplistAll = tripService.getAllTrip();
//        List<TripOption> optionlistAll = optionService.getAllOptions();
//
//        model.addAttribute("triplistAll", triplistAll);
//        model.addAttribute("optionlistAll", optionlistAll);
//
//        //trip status가 APPROVED인 tripList
//        List<Trip> approvedtripList = tripService.getApprovedTrip();
//        model.addAttribute("approvedTrip", approvedtripList);
//
//        //trip status가 PENDING인 tripList
//        List<Trip> pendingtripList = tripService.getPendingTrip();
//        model.addAttribute("pendingTrip",pendingtripList);
//
//        // 이전에 RedirectAttributes로 추가한 Flash attribute를 가져옴
//        String message = (String) model.asMap().get("message");
//        if (message != null) {
//            // 가져온 메시지를 다시 모델에 추가하여 Thymeleaf에서 사용할 수 있도록 함
//            model.addAttribute("message", message);
//        }
//
        //purchase 정보 불러오기
        List<Purchase> purchaseList= getPurchaseListWithoutStatus("REJECTED","REFUND");
        model.addAttribute("purchaseList",purchaseList);
        System.out.println("purchaseList="+ purchaseList);

        return "dashboard/DashTravel_Department";
    }

    private int getListcount(String category,String keyword){
        if (isValidString(category)) {
            return dashTripService.getCategoryListCount(category);
        } else if (isValidString(keyword)) {
            return dashTripService.getKeywordListCount(keyword);
        } else {
            return dashTripService.getListCount();
        }
    }


    private List<Trip> getTriplist(String category,String keyword,int startRow,int endRow,String sort){
        if (isValidString(category)) {
            return dashTripService.getCategoryTripList(startRow, endRow,category, sort);
        } else if (isValidString(keyword)) {
            return dashTripService.getTripListByKeyword(startRow, endRow,keyword,  sort);
        } else {
            return dashTripService.getTripList(startRow, endRow, sort);
        }
    }

    private boolean isValidString(String str) {
        return str != null && !str.isEmpty() && !Objects.equals(str, "null");
    }

    public List<Purchase> getPurchaseListWithoutStatus(String status1,String status2){
        //purchase 정보 불러오기
        List<Purchase> purchaseList= dashPurchaseService.getAllPurchaseInfo();
        List<Purchase> newpurchaseList= new ArrayList<>();
        for(Purchase p : purchaseList){
            if(!(p.getStatus().equals(status1)||p.getStatus().equals(status2))){
                newpurchaseList.add(p);
            }
        }
        return newpurchaseList;
    }


    @GetMapping("/dashSales")
    public String tripSales(
            HttpServletRequest request,HttpServletResponse response,
            Model model) {

        return "dashboard/DashTour_Sales";
    }

}
