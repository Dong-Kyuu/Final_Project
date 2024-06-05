package com.example.jhta_3team_finalproject.domain.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


public class User implements UserDetails {
    private int userNum;
    private String userId;
    private String userPassword;
    private String userEmail;
    private String employeeNumber;
    private String userName;
    private String userPhoneNumber;
    private String userAge;
    private String userDepartment;
    private String userGender; // 기본값 설정
    private String userPosition;
    private int userIsApproved;
    private String userCreatedAt;
    private String userUpdatedAt;
    private String userProfilePicture;
    private String userOriginalProfile; // 파일 저장
    private String userChatStatusMsg;
    private String userAuth = "ROLE_MEMBER";


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();

        roles.add(new SimpleGrantedAuthority(this.getUserAuth()));

        return roles;
    }


    @Override
    public String toString() {
        return "User{" +
                "employeeNumber='" + employeeNumber + '\'' +
                ", userNum=" + userNum +
                ", userId='" + userId + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhone_number='" + userPhoneNumber + '\'' +
                ", userAge='" + userAge + '\'' +
                ", userDepartment='" + userDepartment + '\'' +
                ", userGender='" + userGender + '\'' +
                ", userPosition='" + userPosition + '\'' +
                ", userIsApproved=" + userIsApproved +
                ", userCreatedAt='" + userCreatedAt + '\'' +
                ", userUpdatedAt='" + userUpdatedAt + '\'' +
                ", userProfilePicture='" + userProfilePicture + '\'' +
                ", userOriginalProfile='" + userOriginalProfile + '\'' +
                ", userChatStatusMsg='" + userChatStatusMsg + '\'' +
                ", userAuth='" + userAuth + '\'' +
                '}';
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(String userAuth) {
        this.userAuth = userAuth;
    }

    public String getUserChatStatusMsg() {
        return userChatStatusMsg;
    }

    public void setUserChatStatusMsg(String userChatStatusMsg) {
        this.userChatStatusMsg = userChatStatusMsg;
    }

    public String getUserCreatedAt() {
        return userCreatedAt;
    }

    public void setUserCreatedAt(String userCreatedAt) {
        this.userCreatedAt = userCreatedAt;
    }

    public String getUserDepartment() {
        return userDepartment;
    }

    public void setUserDepartment(String userDepartment) {
        this.userDepartment = userDepartment;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getUserIsApproved() {
        return userIsApproved;
    }

    public void setUserIsApproved(int userIsApproved) {
        this.userIsApproved = userIsApproved;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public String getUserOriginalProfile() {
        return userOriginalProfile;
    }

    public void setUserOriginalProfile(String userOriginalProfile) {
        this.userOriginalProfile = userOriginalProfile;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }

    public String getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserProfilePicture(String userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }

    public String getUserUpdatedAt() {
        return userUpdatedAt;
    }

    public void setUserUpdatedAt(String userUpdatedAt) {
        this.userUpdatedAt = userUpdatedAt;
    }


    @Override
    public String getUsername() {
        return  this.getUserId();
    }
    @Override
    public String getPassword() {
        return  this.getUserPassword();
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

}