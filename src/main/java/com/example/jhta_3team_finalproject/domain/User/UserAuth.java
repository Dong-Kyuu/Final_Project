package com.example.jhta_3team_finalproject.domain.User;

import java.util.List;

public class UserAuth {
    private List<String> authorities;
    private String department;

    // Getters and setters
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
}
