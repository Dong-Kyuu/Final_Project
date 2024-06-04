package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.util.PagingUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.jhta_3team_finalproject.service.User.UserService;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.Random;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.example.jhta_3team_finalproject.domain.User.User;

import java.io.*;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${my.savefolder}")
    private String saveFolder;
    private UserService userService;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    //회원가입 폼에서 아이디 검사
    @ResponseBody
    @RequestMapping(value ="/idcheck",method=RequestMethod.GET)
    public int idcheck(@RequestParam("user_id") String id) {
        return userService.isId(id);
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
        user.setUser_password(passwordEncoder.encode(user.getUser_password()));

        Random randomCreate = new Random();
        int random = randomCreate.nextInt(100000);
        user.setUser_num(random);

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
            mv.setViewName("error");
        }
        return mv;
    }


    //회원 정보 수정 폼
    @RequestMapping(value = "/update")
    public ModelAndView user_update(ModelAndView mv, Principal principal) {
        String id = principal.getName();

        if (id == null) {
            mv.setViewName("redirect:/user/login");
            logger.info("id is null");
        } else {
            User user = userService.user_info(id);
            mv.setViewName("member/user_updateForm");
            mv.addObject("memberinfo", user);
        }
        return mv;
    }


    //회원 수정 저장
    @RequestMapping(value = "/updateProcess", method = RequestMethod.POST)
    public String updateProcess(User user, Model model,
                                RedirectAttributes rattr,
                                HttpServletRequest request,
                                @RequestParam("profilePictureFile") MultipartFile uploadfile) throws IOException {

        if (!uploadfile.isEmpty()) {
            String fileName = uploadfile.getOriginalFilename();
            String fileDBName = fileDBName(fileName, saveFolder);
            logger.info("fileDBName = " + fileDBName);

            File destinationFile = new File(saveFolder + fileDBName);
            uploadfile.transferTo(destinationFile);
            logger.info("File saved to: " + destinationFile.getAbsolutePath());

            user.setUser_profile_picture(fileDBName);
        }
        int result = userService.update(user);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        logger.info("Updating user: " + user);
        logger.info("Update result: " + result);

        if (result == 1) {
            rattr.addFlashAttribute("result", "updateSuccess");
            return "redirect:/user/info";
        } else {
            model.addAttribute("url", request.getRequestURI());
            model.addAttribute("message", "정보 수정실패");
            return "error";
        }
    }

    private String fileDBName(String fileName, String saveFolder) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);

        String homedir = saveFolder + "/" + year + "-" + month + "-" + date;
        logger.info(homedir);
        File path1 = new File(homedir);
        if (!path1.exists()) {
            path1.mkdirs();
        }

        Random r = new Random();
        int random = r.nextInt(100000000);

        int index = fileName.lastIndexOf(".");
        String fileExtension = fileName.substring(index + 1);
        logger.info("fileExtension = " + fileExtension);

        String refileName = "bbs" + year + month + date + random + "." + fileExtension;
        logger.info("refileName = " + refileName);

        String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
        logger.info("fileDBName = " + fileDBName);

        return fileDBName;
    }

//    //사원 출퇴근 관리
//    @RequestMapping(value = "/commute")
//    public String user_copmmute(@RequestParam(value = "page", defaultValue = "1") int page, ModelAndView mv) {
//
//        int limit = 10;  // 한 화면에 출력할 로우 갯수
//        int listcount = userService.getListCount();  // 총 리스트 수를 받아옴
//
//        PagingUtil.Paging paging= PagingUtil.getPaging(page, limit, listcount);
//
//        mv.addObject("page", paging.getPage());
//        mv.addObject("maxpage", paging.getMaxpage());
//        mv.addObject("startpage", paging.getStartpage());
//        mv.addObject("endpage", paging.getEndpage());
//
//
//        mv.setViewName("member/commute");
//        return "member/commute";
//    }

    //직원 근태 관리
    //직원 정보 관리
    //신규 등록 사원

}





