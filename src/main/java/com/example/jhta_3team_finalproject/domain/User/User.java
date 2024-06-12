package com.example.jhta_3team_finalproject.domain.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@ToString
@Slf4j
public class User implements UserDetails {
    private int userNum;
    private String userId;


    private String userPassword;
    private String userEmail;
    private String employeeNumber;
    private String userName;
    private String userPhoneNumber;
    private String userAge;
    private int departmentId= 0; // 기본값 설정
    private String departmentName;
    private String userGender; // 기본값 설정
    private int positionId;
    private String positionName;
    private int userIsApproved;
    private String userCreatedAt;
    private String userUpdatedAt;
    private String userProfilePicture;
    private String userOriginalProfile; // 파일 저장
    private String userChatStatusMsg;
    private String userAuth = "ROLE_MEMBER";

    // 동규
    private int viewCheck;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();

        roles.add(new SimpleGrantedAuthority(this.getUserAuth()));

        return roles;
    }


    @Override
    public String getUsername() {
        return userName;
    }

//    public String getUsername() {
//        return this.getUserId();
//    }



    public int getDepartmentId() {return departmentId;}



    @Override
    public String getPassword() {
        return this.getUserPassword();
    }



    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setDepartment(String department) {
        this.departmentName = department;
    }


}