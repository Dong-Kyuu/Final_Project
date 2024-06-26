package com.example.jhta_3team_finalproject.controller.User;

import com.example.jhta_3team_finalproject.domain.User.Attendence;
import com.example.jhta_3team_finalproject.domain.User.Response;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.mybatis.mapper.User.AttendenceMapper;
import com.example.jhta_3team_finalproject.service.User.AttendanceService;
import com.example.jhta_3team_finalproject.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping ("/admin/attendance")
public class AttendanceManagementController {
    private UserService userservice;
    private AttendanceService attendanceService;

    @Autowired
    public AttendanceManagementController(UserService userservice, AttendanceService attendanceService) {
        this.userservice = userservice;
        this.attendanceService = attendanceService;
    }

    //전 직원의 출퇴근 정보를 리스트로 보여주는 페이지로 이동
    @GetMapping("/list")
    public ModelAndView listAttendance(ModelAndView mv) {
        mv.setViewName("member/admin/attendance_list");
        return mv;
    }

    // 전 직원의 출퇴근 정보를 반환
    @GetMapping("/all")
    @ResponseBody
    public List<Response> getAllAttendances(ModelAndView mv,
                                            @RequestParam(value = "startDate", required = false) String startDateStr,
                                            @RequestParam(value = "endDate", required = false) String endDateStr,
                                            @RequestParam(value = "filter", defaultValue = "-1") int filter) {
        List<Map<String, Object>> attendanceList = attendanceService.getMonthlyAttendancesForAll(startDateStr, endDateStr, filter);
        List<Response> list = new ArrayList<>();
        for (int i = 0; i < attendanceList.size(); i++) {
            Map<String, Object> b = attendanceList.get(i);
            Response a = new Response();
            a.from(b);
            list.add(a);
        }
        return list;

    }

   
}
//  Map<String,Object> input =new HashMap<>()
//        input.put("startDate",startDateStr);
//        input.put("endDate",endDateStr);
//        input.put("filter",filter);
//    // 특정 직원의 한 달 일한 시간을 반환
//    @GetMapping("/monthly/{userNum}")
//    @ResponseBody
//    public Attendence getMonthlyAttendance(@PathVariable int userNum) {
//        LocalDate now = LocalDate.now();
//        LocalDateTime startDate = now.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
//        LocalDateTime endDate = now.with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);
//        return userService.getMonthlyAttendance(userNum, startDate, endDate);
//    }

