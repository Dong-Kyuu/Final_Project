package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.service.Notification.SseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;

@RestController
public class SseController {

    private static final Logger logger = LoggerFactory.getLogger(AnnounceBoardController.class);

    @Autowired
    private SseService sseService;

    @GetMapping("/sse")
    public SseEmitter streamSse(Principal principal) {
        // sse
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginuser = (User)authentication.getPrincipal();
        logger.info("sse");
        return sseService.createEmitter(loginuser.getUserNum());

    }

    @PostMapping("/notification/readAction")
    public int notificationreadAction(int notifiNum) {

        return sseService.notificationRead(notifiNum);
    }
}
