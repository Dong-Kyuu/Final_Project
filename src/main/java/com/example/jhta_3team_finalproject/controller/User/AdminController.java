package com.example.jhta_3team_finalproject.controller.User;

import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.service.User.AdminService;
import com.example.jhta_3team_finalproject.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private AdminService adminService;
    private UserService userservice;


    @Autowired
    public AdminController(AdminService adminService, UserService userservice) {

        this.adminService = adminService;
        this.userservice = userservice;
    }

    // 모든 직원 목록 조회
    @GetMapping("/users")
    public ModelAndView users(ModelAndView mv) {
        mv.setViewName("member/admin/user_list");
        return mv;
    }

    @PostMapping("/updateUserInfo/{userNum}")
    @ResponseBody
    public String updateUserInfo(@PathVariable int userNum,
                                 @RequestParam int departmentId,
                                 @RequestParam int positionId,
                                 @AuthenticationPrincipal User userDetails) {
        adminService.updateUserInfo(userNum,departmentId,positionId);
        return "success";
    }

    @GetMapping("/filterUsers")
    @ResponseBody
    public List<User> getUsersFilter(@RequestParam int departmentId) {
        return adminService.getUsersFilter(departmentId);
    }

    // 기안하기
    @GetMapping("/sign")
    public String sign() {
        return "member/sign/sign2";
    }

    //양식
    @GetMapping("/approvalupdateLeave")
    public String approvalupdateLeave() {
        return "member/sign/leaveApplication";
    }


}
