package com.example.jhta_3team_finalproject.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
//AuthenticationSuccessHandler : 사용자 인증이 성공 후 처리할 작업을 직접 작성할 때 인터페이스입니다.
public class LoginSuccessHandler implements AuthenticationSuccessHandler{
	private static final Logger logger = LoggerFactory.getLogger(LoginSuccessHandler.class);

	public void onAuthenticationSuccess(HttpServletRequest request,HttpServletResponse response,
					Authentication authentication) throws IOException{
			logger.info("로그인 성공:LoginSucessHandler");
			String url = "../table/freelist";
			response.sendRedirect(url);
			logger.info(url);
	}
}
