package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainPageController {
    @Autowired
    private UserService userservice;

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
        return "dashboard/blank-page";
    }

}
