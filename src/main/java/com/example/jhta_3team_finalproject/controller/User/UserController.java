package com.example.jhta_3team_finalproject.controller.User;

import com.example.jhta_3team_finalproject.domain.User.Attendence;
import com.example.jhta_3team_finalproject.domain.User.MailVO;
import com.example.jhta_3team_finalproject.domain.User.SendMail;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.service.Notification.SseService;
import com.example.jhta_3team_finalproject.service.User.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userservice;
    private PasswordEncoder passwordEncoder;
    private SendMail sendMail;
    private SseService sseService;
    private static final int UPDATE_SUCCESS = 1;
    private static final int JOIN_SUCCESS = 1;


    @Autowired
    public UserController(UserService userservice,
                          PasswordEncoder passwordEncoder, SendMail sendMail, SseService sseService) {

        this.userservice = userservice;
        this.passwordEncoder = passwordEncoder;
        this.sendMail = sendMail;
        this.sseService = sseService;
    }

    @GetMapping(value = "/login")
    public ModelAndView login(
            ModelAndView mv,
            @CookieValue(value = "remember-me", required = false) Cookie readCookie,
            HttpSession session,
            Principal userPrincipal
    ) {
        if (readCookie != null) {
            mv.setViewName("redirect:/user/login");
        } else {
            mv.setViewName("member/login");
            mv.addObject("fail", session.getAttribute("fail"));
            session.removeAttribute("fail");
        }
        return mv;
    }

    //신입사원이 로그인 한 경우 해당 페이지 이동
    @RequestMapping(value = "/newbie", method = RequestMethod.GET)
    public String newbie() {
        return "member/newbie";
    }

    @GetMapping(value = "/join")
    public String join(Model model) {
        model.addAttribute("user", new User());
        return "member/register";
    }

    //회원가입

    @PostMapping(value = "/joinProcess")
    public String joinProcess(@Valid User user, BindingResult result, Model model, RedirectAttributes rattr, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            System.out.println("에러");
            return "member/register";
        }
        // 비밀번호와 비밀번호 확인이 일치하는지 확인
        if (!user.getUserPassword().equals(user.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            model.addAttribute("user", user);
            return "member/register";
        }
        int joinResult = userservice.join(user);

        if (joinResult == JOIN_SUCCESS) {
            MailVO vo = new MailVO();
            vo.setTo(user.getUserEmail());
            sendMail.sendMail(vo);

            // 승인 대기 상태로 설정
            user.setUserIsApproved(0);

            rattr.addFlashAttribute("result", "joinSuccess");
            return "redirect:/user/login";
        } else {
            model.addAttribute("url", request.getRequestURI());
            model.addAttribute("message", "회원가입 실패");
            return "error";
        }
    }

    //신규 사원 승인
    @GetMapping(value = "/register_allow")
    public String registerAllow() {
        return "member/register_allow";
    }

    // 승인 사용자 요청 조회
    @GetMapping("/register_requests")
    @ResponseBody
    public List<Map<String, Object>> getRegisterRequests() {
        return userservice.getUsersFilter();
    }

    // 사용자 승인
    @PostMapping("/approve/{userNum}")
    @ResponseBody
    public Map<String, String> approveUser(@PathVariable int userNum,
                                           @RequestParam int departmentId,
                                           @RequestParam int positionId,
                                           @AuthenticationPrincipal User userDetails) {
        userservice.approveUser(userNum,departmentId,positionId);
        log.info(userNum + "userNum");

        // 알림 보낼 데이터 가져오기 (예: 승인한 사람의 이름, 해당 링크 등)
        User user = userservice.getUserInfo(userNum); // 승인된 사용자 정보
        String approverName = userDetails.getUserName();
        String notificationMessage = approverName + "님이 승인 완료 했습니다.";
        String notificationLink = "http://43.203.196.38:9000/dashboard/user/detail/" + userNum;

        // 알림 전송
        sseService.sendNotification(user.getUserNum(), userDetails.getUserNum(), approverName, notificationLink, notificationMessage);

        return Map.of("status", "success", "message", "승인 완료, 사원의 정보가 저장되었습니다.");

    }

    // 사용자 거절
    @PostMapping("/reject/{userNum}")
    @ResponseBody
    public Map<String, String> rejectUser(@PathVariable int userNum) {
        userservice.rejectUser(userNum);
        return Map.of("status", "success", "message", "거절 완료되었습니다.");
    }


    @GetMapping(value = "/idcheck")
    public @ResponseBody int idcheck(@RequestParam("userId") String id) {
        User user = userservice.getUserId(id);
        return (user == null) ? -1 : 1; // -1은 아이디가 존재하지 않는 경우, 1은 아이디가 존재하는 경우;
    }

    @GetMapping(value = "/logout")
    public String logout() {
        return "member/login";
    }

    @PostMapping(value = "/findPassword")
    public String findPassword() {
        return "member/findPassword";
    }

    @GetMapping(value = "/info")
    public ModelAndView user_info(
            UsernamePasswordAuthenticationToken principal,
            ModelAndView mv,
            HttpServletRequest request) {

        User userDetails = (User) principal.getPrincipal();
        User employee = userservice.getEmployee(userDetails.getUserNum());

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

    @PostMapping(value = "/updateProcess") //회원 정보 수정
    public String updateProcess(User user, Model model,
                                RedirectAttributes rattr,
                                HttpServletRequest request,
                                @RequestParam("profilePictureFile") MultipartFile uploadfile,
                                Principal principal) throws IOException {

        boolean isUpdated = userservice.updateUser(user, uploadfile);

        if (isUpdated) { // 사용자 정보 업데이트
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof UsernamePasswordAuthenticationToken) {

                User updatedUser = userservice.getUserInfo(user.getUserNum()); // 업데이트된 사용자 정보 가져오기

                UsernamePasswordAuthenticationToken newAuthentication =
                        new UsernamePasswordAuthenticationToken(updatedUser, authentication.getCredentials(), authentication.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(newAuthentication);
            }

            rattr.addFlashAttribute("result", "updateSuccess");
            return "redirect:/user/info";
        } else {
            model.addAttribute("url", request.getRequestURI());
            model.addAttribute("message", "정보 수정실패");
            return "error";
        }
    }


    // 출퇴근 관리 페이지로 이동
    @GetMapping(value = "/commute")
    public ModelAndView commutePage(Principal principal, ModelAndView mv) {
        log.info("출퇴근 페이지");
        User userDetails = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        int userNum = userDetails.getUserNum();

        mv.setViewName("member/attendance");
        mv.addObject("userNum", userNum);
        mv.addObject("username", userDetails.getUsername());
        return mv;

    }

    //사용자의 한달 출퇴근 기록을 반환
    @GetMapping(value = "/monthlyAttendance")
    @ResponseBody
    public List<Map<String, Object>> getMonthlyAttendances(Principal principal) {
        User userDetails = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        int userNum = userDetails.getUserNum();

        String startDateStr = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).toString();
        String endDateStr = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).toString();

        log.info(startDateStr + "시작" + endDateStr);
        return userservice.getMonthlyAttendances(userNum, startDateStr, endDateStr);
    }


    // 사원 출퇴근 시간 기록
    @PostMapping("/attendance")
    @ResponseBody
    public Map<String, Object> recordAttendance(String action, @AuthenticationPrincipal User userDetails) {
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
    public Map<String, Object> getTodayAttendance(@AuthenticationPrincipal User userDetails) {
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





