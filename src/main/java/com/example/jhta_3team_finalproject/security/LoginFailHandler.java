package com.example.jhta_3team_finalproject.security;

import java.io.IOException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
//AuthenticationFailureHandler : 로그인 실패 후 처리할 작업를 해야 할 때 사용하는  인터페이스입니다.
public class LoginFailHandler implements AuthenticationFailureHandler {
    private static final Logger log = LoggerFactory.getLogger(LoginFailHandler.class);

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        HttpSession session = request.getSession();


        log.info(exception.getMessage());
        log.info("로그인 실패");
        session.setAttribute("loginfail", "loginFailMsg");
        String url = request.getContextPath() + "/member/login";
        response.sendRedirect(url);
    }
}
