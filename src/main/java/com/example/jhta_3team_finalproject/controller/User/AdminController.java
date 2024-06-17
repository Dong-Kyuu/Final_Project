package com.example.jhta_3team_finalproject.controller.User;

import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.service.User.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 모든 직원 목록 조회
    @GetMapping("/users")
    public ModelAndView users(ModelAndView mv) {
        mv.setViewName("member/admin/user_list");
        return mv;
    }

    @PostMapping("/updateUserInfo")
    @ResponseBody
    public String updateUserInfo(@RequestBody User user) {
        adminService.updateUserInfo(user);
        return "success";
    }

    @GetMapping("/filterUsers")
    @ResponseBody
    public List<User> getUsersFilter(@RequestParam Map<String, Object> params) {
        return adminService.getUsersFilter(params);
    }

}
