package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.User.UserAuth;
import com.example.jhta_3team_finalproject.service.User.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final UserAuthService userAuthService;

    @Autowired
    public AuthenticationController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @GetMapping("/scan/auth")
    public ResponseEntity<UserAuth> getUserInfo(Authentication authentication) {
        UserAuth userInfo = userAuthService.getUserInfo(authentication);
        return ResponseEntity.ok(userInfo);
    }
}

