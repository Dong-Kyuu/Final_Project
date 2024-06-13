package com.example.jhta_3team_finalproject.service.User;

import com.example.jhta_3team_finalproject.domain.User.UserAuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserAuthService {

    private final RoleHierarchy roleHierarchy;

    @Autowired
    public UserAuthService(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

    public UserAuth getUserInfo(Authentication authentication) {
        UserAuth userAuth = new UserAuth();

        // Hierarchical roles
        Collection<? extends GrantedAuthority> authorities = roleHierarchy.getReachableGrantedAuthorities(authentication.getAuthorities());

        userAuth.setAuthorities(authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));



        // 로그로 계층적 권한 정보 출력
        authorities.forEach(authority -> {
            System.out.println("Authority: " + authority.getAuthority());
        });

        return userAuth;
    }
}