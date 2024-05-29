package com.example.jhta_3team_finalproject.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class User implements UserDetails {
    private String id;
    private String password;
    private String email;
    private String employee_number;
    private String name;
    private String phone_number;
    private String age;
    private String department;
    private String gender; // 기본값 설정
    private String position;
    private int is_approved;
    private String created_at;
    private String updated_at;
    private String profile_picture;
    private String chat_status_msg;
    private String auth="ROLE_MEMBER";


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();

        roles.add(new SimpleGrantedAuthority(this.getAuth()));

        return roles;
    }
    @Override
    public String toString() {
        return "User{" +
                "age='" + age + '\'' +
                ", id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", employee_number='" + employee_number + '\'' +
                ", name='" + name + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", is_approved=" + is_approved +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", profile_picture='" + profile_picture + '\'' +
                ", auth='" + auth + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> roles= new ArrayList<SimpleGrantedAuthority>();}

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone_number() {
        return phone_number;
    }
    public String getChat_status_msg() {
        return chat_status_msg;
    }

    public void setChat_status_msg(String chat_status_msg) {
        this.chat_status_msg = chat_status_msg;
    }

    public String getProfile_picture() {return profile_picture;}

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getUpdated_at() {

        return updated_at;
    }

    public void setUpdated_at(String updated_at) {

        this.updated_at = updated_at;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }



    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return id;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIs_approved() {
        return is_approved;
    }

    public void setIs_approved(int is_approved) {
        this.is_approved = is_approved;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployee_number() {
        return employee_number;
    }

    public void setEmployee_number(String employee_number) {
        this.employee_number = employee_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }



}