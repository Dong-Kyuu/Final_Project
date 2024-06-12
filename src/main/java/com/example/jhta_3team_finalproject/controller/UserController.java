package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.User.*;
import com.example.jhta_3team_finalproject.service.User.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${my.savefolder}")
    private String saveFolder;
    private UserService userservice;
    private PasswordEncoder passwordEncoder;
    private SendMail sendMail;

    private static final int UPDATE_SUCCESS = 1;
    private static final int JOIN_SUCCESS = 1;


    // private S3CommomService s3CommomService;
    //, S3CommomService s3CommomService
    //this.s3CommomService = s3CommomService;

    //   TODO : s3 사용시 변경


    @Autowired
    public UserController(UserService userservicece,
                          PasswordEncoder passwordEncoder, SendMail sendMail) {
        this.userservice = userservicece;
        this.passwordEncoder = passwordEncoder;
        this.sendMail = sendMail;

    }


//    @ModelAttribute
//    public void addAttribute( @AuthenticationPrincipal  User user , Model model){
////        User userDetails = (User) principal.getPrincipal();
//       // var employee = this.userservice.getEmployee(userDetails.getUserNum());
//        if (user != null) {
//            model.addAttribute("departmentName", user.getDepartmentId());
//
//            model.addAttribute("positionName", user.getPositionId());
//        }
//    }

    @GetMapping(value = "/login")
    public ModelAndView login(
            ModelAndView mv,
            @CookieValue(value = "remember-me", required = false) Cookie readCookie,
            HttpSession session,
            Principal userPrincipal
    ) {
        if (readCookie != null) {
            log.info("저장된 아이디:" + userPrincipal.getName());
            mv.setViewName("redirect:/board/list");
        } else {
            mv.setViewName("member/login");
            mv.addObject("loginfail", session.getAttribute("loginfail"));
            session.removeAttribute("loginfail");
        }
        log.info("login 페이지");
        return mv;
    }

    @RequestMapping(value = "/join", method = RequestMethod.GET)
    public String join() {
        return "member/register";
    }

    @PostMapping(value = "/joinProcess")
    public String joinProcess(User user, Model model, RedirectAttributes rattr, HttpServletRequest request) {
        log.info("User: " + user.toString());
        user.setUserPassword(passwordEncoder.encode(user.getPassword()));
        log.info(user.getPassword());
        int result = userservice.join(user);
        if (result == JOIN_SUCCESS) {
            MailVO vo = new MailVO();
            vo.setTo(user.getUserEmail());
            sendMail.sendMail(vo);
            log.info(sendMail + "확인");

            rattr.addFlashAttribute("result", "joinSuccess");
            return "redirect:/user/login";
        } else {
            model.addAttribute("url", request.getRequestURI());
            model.addAttribute("message", "회원가입 실패");
            return "error";
        }
    }

    @RequestMapping(value = "/idcheck", method = RequestMethod.GET)
    public @ResponseBody int idcheck(@RequestParam("userId") String id) {
        int result = userservice.getUserId(id);
        return result;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        return "member/login";
    }

    @RequestMapping(value = "/findPassword", method = RequestMethod.POST)
    public String findPassword() {
        return "member/findPassword";
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ModelAndView user_info(
            UsernamePasswordAuthenticationToken principal,
            ModelAndView mv,
            HttpServletRequest request) {

        User userDetails = (User) principal.getPrincipal();
        var employee = this.userservice.getEmployee(userDetails.getUserNum());
        log.info(employee.getDepartmentName());
        if (employee != null) {
            mv.setViewName("member/user_updateForm");
            mv.addObject("memberinfo", employee);
            mv.addObject("departmentName", employee.getDepartmentName());
            mv.addObject("positionName", employee.getPositionName());
        } else {
            mv.addObject("url", request.getRequestURI());
            mv.addObject("message", "정보 수정실패");
            mv.setViewName("error");
        }
        return mv;
    }


//   TODO : s3 사용시 변경

//    @PostMapping(value = "/updateProcess")
//    public String updateProcess(User user, Model model,
//                                RedirectAttributes rattr,
//                                HttpServletRequest request,
//                                @RequestParam("profilePictureFile") MultipartFile uploadfile) throws IOException {
//
//
//        log.info("수정 전 User 정보: " + user);
//        if (!uploadfile.isEmpty()) {
//            String fileUrl = s3CommomService.uploadFile(uploadfile);
//            log.info("Uploaded file URL: " + fileUrl);
//            user.setUserProfilePicture(fileUrl);
//        }
//
//        log.info("업데이트 전에 User 정보: " + user);
//        int result = userservice.update(user);
//
//        UsernamePasswordAuthenticationToken authentication =
//                new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        log.info("Updating user: " + user);
//        log.info("Update result: " + result);
//
//        if (result == UPDATE_SUCCESS) {
//            rattr.addFlashAttribute("result", "updateSuccess");
//            return "redirect:/user/info";
//        } else {
//            model.addAttribute("url", request.getRequestURI());
//            model.addAttribute("message", "정보 수정실패");
//            return "error";
//        }
//    }


    // 출퇴근 관리 페이지로 이동
    @GetMapping(value = "/commute")
    public ModelAndView commutePage(Principal principal, ModelAndView mv) {
        log.info("출퇴근 페이지");
        User userDetails = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        int userNum = userDetails.getUserNum();


//        LocalDate now =LocalDate.now();
//        LocalDateTime startDate = now.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
//        LocalDateTime endDate = now.with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);
//
//        List<Attendence> attendanceList = userservice.getMonthlyAttendances(userNum,startDate,endDate);
        mv.setViewName("member/attendance");

        mv.addObject("userNum", userNum);
        mv.addObject("username", userDetails.getUsername());
//        mv.addObject("attendanceList", attendanceList);
        return mv;

    }

    //사용자의 한달 출퇴근 기록을 반환
    @GetMapping(value = "/monthlyAttendance")
    @ResponseBody
    public List<Attendence> getMonthlyAttendances(Principal principal) {
        User userDetails = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        int userNum = userDetails.getUserNum();

        LocalDate now = LocalDate.now();
        LocalDateTime startDate = now.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime endDate = now.with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);

        return userservice.getMonthlyAttendances(userNum, startDate, endDate);
    }

    // 사원 출퇴근 시간 기록
    @PostMapping("/attendance")
    @ResponseBody
    public Map<String, Object> recordAttendance(String action, Principal principal, Model model, @AuthenticationPrincipal User userDetails) {
        log.info("출퇴근 입력");
        int userNum = userDetails.getUserNum();

        Attendence attendence = userservice.recordAttendance(userNum, action);

        Map<String, Object> response = new HashMap<>();
        response.put("message", action.equalsIgnoreCase("checkIn") ? "출근 완료" : "퇴근 완료");
        response.put("attendance", attendence);
        return response;
    }

    // 오늘의 출퇴근 기록 조회
    @GetMapping("/todayAttendance")
    @ResponseBody
    public Map<String, Object>getTodayAttendance(@AuthenticationPrincipal User userDetails) {
        int userNum = userDetails.getUserNum();

        Attendence attendance = userservice.getTodayAttendance(userNum);

        Map<String, Object> response = new HashMap<>();

        if (attendance == null) {
            response.put("status", "0");  // 출,퇴근이 데이터 베이스에 들어가지 않은 경우 -0
        } else {
            if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {
                response.put("status", "2"); //출,퇴근이 데이터베이스에 들어간 경우 2
                response.put("checkInTime", attendance.getCheckInTime());
                response.put("checkOutTime", attendance.getCheckOutTime());

            } else if (attendance.getCheckOutTime() == null) {
                response.put("status", "1"); //출근만 디비에 들어간 경우 1번
                response.put("checkInTime", attendance.getCheckInTime());
            }

        }
        return response;


    }
}


//    @PostMapping("/check-in")
//    public String checkIn( Principal principal) {
//        User userDetails = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
//        int userNum = userDetails.getUserNum();
//        userservice.checkIn(userNum);
//        return "출근 완료";
//    }
//
//    @PostMapping("/check-out")
//    public String checkOut(Principal principal) {
//        User userDetails = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
//        int userNum = userDetails.getUserNum();
//        userservice.checkOut(userNum);
//        return "퇴근 완료";
//    }






