package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.domain.calendar.Calendar;
import com.example.jhta_3team_finalproject.service.User.UserService;
import com.example.jhta_3team_finalproject.service.dashboard.DashBoardCalendarService;
import com.example.jhta_3team_finalproject.service.dashboard.DashBoardCalendarServiceImpl;
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

import java.util.List;

@Controller
public class MainPageController {
    private final  UserService userservice;

    private DashBoardCalendarService dashBoardCalendarService;

    private static final Logger logger = LoggerFactory.getLogger(AnnounceBoardController.class);

    @Autowired
    public MainPageController(UserService userService, DashBoardCalendarService dashBoardCalendarService, DashBoardCalendarServiceImpl dashBoardCalendarServiceImpl) {
        this.userservice = userService;
        this.dashBoardCalendarService = dashBoardCalendarService;
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

    @RequestMapping(value = "/announce", method = RequestMethod.GET)
    public String test() {
        return "table/announce/announceBoard";
    }

    @RequestMapping(value = "/travel", method = RequestMethod.GET)
    public String test2() {
        return "tourdepartment/Travel_Department";
    }

}
