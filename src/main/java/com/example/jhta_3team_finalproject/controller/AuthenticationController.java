package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.User.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AuthenticationController {

    @GetMapping("/scan/auth")
    public ResponseEntity<UserInfo> getUserInfo(Authentication authentication) {
        UserInfo userInfo = new UserInfo();
        userInfo.setAuthorities(authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        userInfo.setDepartment(((User) authentication.getPrincipal()).getUserDepartment());

        // 로그로 권한 정보 출력
        authentication.getAuthorities().forEach(authority -> {
            System.out.println("Authority: " + authority.getAuthority());
        });
        return ResponseEntity.ok(userInfo);
    }
}

class UserInfo {
    private List<String> authorities;
    private String department;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
// Getters and setters
}
