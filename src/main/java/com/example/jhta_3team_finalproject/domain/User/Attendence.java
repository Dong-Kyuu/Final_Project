package com.example.jhta_3team_finalproject.domain.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Getter
@Setter
@ToString

public class Attendence  {
    private int attendenceId;
    private int userNum;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private LocalDate  workDate;

    private int totalHours;

    private String userId;
    private String userName;
}
