package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.Board.AnnounceBoard;
import com.example.jhta_3team_finalproject.domain.Board.Board;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.service.User.UserService;
import com.example.jhta_3team_finalproject.util.PagingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MainPageController {
    private final  UserService userservice;

    private static final Logger logger = LoggerFactory.getLogger(AnnounceBoardController.class);

    @Autowired
    public MainPageController(UserService userService) {
        this.userservice = userService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/user/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal User userDetails) {
        User dbuser = userservice.departmentPositionInfo(userDetails.getDepartmentId(), userDetails.getPositionId());

        userDetails.setDepartmentName(dbuser.getDepartmentName());
        userDetails.setPositionName(dbuser.getPositionName());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "dashboard/dashboard-page";
    }




}
