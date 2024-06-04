package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
public class AuthenticationController {

    private final RoleHierarchy roleHierarchy;

    @Autowired
    public AuthenticationController(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

    @GetMapping("/scan/auth")
    public ResponseEntity<UserInfo> getUserInfo(Authentication authentication) {
        UserInfo userInfo = new UserInfo();

        // Hierarchical roles
        Collection<? extends GrantedAuthority> authorities = roleHierarchy.getReachableGrantedAuthorities(authentication.getAuthorities());

        userInfo.setAuthorities(authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        userInfo.setDepartment(((User) authentication.getPrincipal()).getUserDepartment());

        // 로그로 계층적 권한 정보 출력
        authorities.forEach(authority -> {
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
