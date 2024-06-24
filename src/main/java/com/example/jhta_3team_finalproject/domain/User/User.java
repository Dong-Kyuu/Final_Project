package com.example.jhta_3team_finalproject.domain.User;

import jakarta.validation.constraints.*;
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

    @NotBlank(message = "아이디는 필수 정보입니다.")
    @Pattern(regexp = "^[A-Za-z]{1}[A-Za-z0-9]{3,19}$", message = "아이디는 영문자로 시작하고 4~20자 사이여야 합니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 정보입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{6,20}$", message = "비밀번호는 영문 대소문자와 숫자 4~12자리로 입력해야 합니다.")
    private String userPassword;

    @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
    private String confirmPassword; // 비밀번호 확인

    @NotBlank(message = "이름은 필수 정보입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "이름은 한글 또는 영문으로 입력해 주세요.")
    private String userName;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해 주세요.")
    private String userEmail;

    @NotBlank(message = "전화번호는 필수 정보입니다.")
    @Pattern(regexp = "^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$", message = "전화번호를 정확히 입력해 주세요.")
    private String userPhoneNumber;

    @NotBlank(message = "성별은 필수 정보입니다.")
    private String userGender;


    private String employeeNumber;
    private String userAge;
    private int departmentId;
    private String departmentName;
    private int positionId;
    private String positionName;
    private int userIsApproved;
    private String userCreatedAt;
    private String userUpdatedAt;
    private String userProfilePicture;
    private String userChatStatusMsg;
    private String userAuth = "ROLE_NEWBIE";
    private String userStatus;

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


    public int getDepartmentId() {
        return departmentId;
    }


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