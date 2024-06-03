package com.example.jhta_3team_finalproject.domain.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class UserCommute {

    private Long id;
    private String num;
    private String name;
    private String department;
    private String position;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private LocalDate  workDate;
    private LocalTime totalWorkTime;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
