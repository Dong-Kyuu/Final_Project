package com.example.jhta_3team_finalproject.domain.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


public class User implements UserDetails {
    private int user_num;
    private String user_id;
    private String user_password;
    private String user_email;
    private String employee_number;
    private String user_name;
    private String user_phone_number;
    private String user_age;
    private String user_department;
    private String user_gender; // 기본값 설정
    private String user_position;
    private int user_is_approved;
    private String user_created_at;
    private String user_updated_at;
    private String user_profile_picture;
    private String user_original_profile; // 파일 저장
    private String user_chat_status_msg;
    private String user_auth = "ROLE_MEMBER";


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();

        roles.add(new SimpleGrantedAuthority(this.getUser_auth()));

        return roles;
    }



    @Override
    public String toString() {
        return "User{" +
                "employee_number='" + employee_number + '\'' +
                ", user_num=" + user_num +
                ", user_id='" + user_id + '\'' +
                ", user_password='" + user_password + '\'' +
                ", user_email='" + user_email + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_phone_number='" + user_phone_number + '\'' +
                ", user_age='" + user_age + '\'' +
                ", user_department='" + user_department + '\'' +
                ", user_gender='" + user_gender + '\'' +
                ", user_position='" + user_position + '\'' +
                ", user_is_approved=" + user_is_approved +
                ", user_created_at='" + user_created_at + '\'' +
                ", user_updated_at='" + user_updated_at + '\'' +
                ", user_profile_picture='" + user_profile_picture + '\'' +
                ", user_original_profile='" + user_original_profile + '\'' +
                ", user_chat_status_msg='" + user_chat_status_msg + '\'' +
                ", user_auth='" + user_auth + '\'' +
                '}';
    }

    public String getEmployee_number() {
        return employee_number;
    }

    public void setEmployee_number(String employee_number) {
        this.employee_number = employee_number;
    }

    public String getUser_age() {
        return user_age;
    }

    public void setUser_age(String user_age) {
        this.user_age = user_age;
    }

    public String getUser_auth() {
        return user_auth;
    }

    public void setUser_auth(String user_auth) {
        this.user_auth = user_auth;
    }

    public String getUser_chat_status_msg() {
        return user_chat_status_msg;
    }

    public void setUser_chat_status_msg(String user_chat_status_msg) {
        this.user_chat_status_msg = user_chat_status_msg;
    }

    public String getUser_created_at() {
        return user_created_at;
    }

    public void setUser_created_at(String user_created_at) {
        this.user_created_at = user_created_at;
    }

    public String getUser_department() {
        return user_department;
    }

    public void setUser_department(String user_department) {
        this.user_department = user_department;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getUser_is_approved() {
        return user_is_approved;
    }

    public void setUser_is_approved(int user_is_approved) {
        this.user_is_approved = user_is_approved;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_num() {
        return user_num;
    }

    public void setUser_num(int user_num) {
        this.user_num = user_num;
    }

    public String getUser_original_profile() {
        return user_original_profile;
    }

    public void setUser_original_profile(String user_original_profile) {
        this.user_original_profile = user_original_profile;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_phone_number() {
        return user_phone_number;
    }

    public void setUser_phone_number(String user_phone_number) {
        this.user_phone_number = user_phone_number;
    }

    public String getUser_position() {
        return user_position;
    }

    public void setUser_position(String user_position) {
        this.user_position = user_position;
    }

    public String getUser_profile_picture() {
        return user_profile_picture;
    }

    public void setUser_profile_picture(String user_profile_picture) {
        this.user_profile_picture = user_profile_picture;
    }

    public String getUser_updated_at() {
        return user_updated_at;
    }

    public void setUser_updated_at(String user_updated_at) {
        this.user_updated_at = user_updated_at;
    }

    @Override
    public String getUsername() {
        return  this.getUser_id();
    }
    @Override
    public String getPassword() {
        return  this.getUser_password();
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