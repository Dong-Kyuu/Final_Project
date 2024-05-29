package com.example.jhta_3team_finalproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.jhta_3team_finalproject.service.UserService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.example.jhta_3team_finalproject.domain.User;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    //로그인
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            ModelAndView mv,
            @CookieValue(value = "remember-me", required = false) Cookie readCookie,
            HttpSession session,
            Principal userPrincipal
    ) {
        if (readCookie != null) {
            //principal.getName():로그인 한 아이디 값을 알 수 있어요
            logger.info("저장됭 아이디:" + userPrincipal.getName());
            mv.setViewName("redirect:/board/list");
        } else {
            mv.setViewName("member/login");

            //세션에 저장된 값을 한 번만 실행 될 수 있도록 model에 저장
            mv.addObject("loginfail", session.getAttribute("loginfail"));
            session.removeAttribute("loginfail");//세션의 값은 제거합니다
        }
        logger.info("login 페이지");
        return mv;
    }

    // 로그아웃
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        return "member/login";
    }

    //회원가입
    @RequestMapping(value = "/join", method = RequestMethod.GET)
    public String join() {
        return "member/register";
    }

    @RequestMapping(value = "/joinProcess", method = RequestMethod.POST)
    public String joinProcess(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        logger.info(("User: " + user.toString()));
        userService.join(user);
        return "redirect:/user/login";
    }

    //비밀번호 찾기
    @RequestMapping(value = "/findPassword", method = RequestMethod.POST)
    public String findPassword() {
        return "member/findPassword";
    }


    //회원 정보  폼
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ModelAndView user_info(
            Principal principal,
            ModelAndView mv,
            HttpServletRequest request) {

        String id = principal.getName();
        User m = userService.user_info(id);

        //m==null;//오류 확인하는 값
        if (m != null) {
            mv.setViewName("member/user_info");
            mv.addObject("memberinfo", m);
        } else {
            mv.addObject("url", request.getRequestURI());
            mv.addObject("message", "정보 수정실패");
            //mv.setViewName("error/error");
        }
        return mv;
    }


    //회원 정보 수정
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ModelAndView user_update(
            ModelAndView mv,
            Principal principal) {

        String id = principal.getName();

        if (id == null) {
            mv.setViewName("redirect:login");
            logger.info("id is null");
        } else {
            User user = userService.user_info(id);
            mv.setViewName("member/user_updateForm");
            mv.addObject("memberinfo", user);
        }
        return mv;
    }




    //수정하기 저장
    @RequestMapping(value = "/updateProcess", method = RequestMethod.POST)
    public String UpdateProcess(User user, Model model,
                                RedirectAttributes rattr,
                                HttpServletRequest request) {
        int result = userService.update(user);

        //삽입이 된 경우
        if (result == 1) {
            rattr.addFlashAttribute("result", "updateSuccess");
            return "redirect:/";
        } else {
            model.addAttribute("url", request.getRequestURI());
            model.addAttribute("message", "정보 수정실패");
            return "";
        }
    }


}





