package com.example.jhta_3team_finalproject.domain.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Time;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Attendence {
    private int attendenceId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Time workTime;
    private int totalHours;
    private int userNum;
    private String userId;
    private String userName;
    private String departmentName;
    private String positionName;
}
