package com.example.jhta_3team_finalproject.domain.User;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class Response {
    private int user_num;
    private String user_name;
    private String department_name;
    private String position_name;

    private LocalDateTime check_in_time;


    private LocalDateTime check_out_time;

    private Time work_time;

    public void from(Map<String, Object> data) {
        this.setUser_num((Integer) data.get("user_num"));
        this.setUser_name((String) data.get("user_name"));
        this.setDepartment_name((String) data.get("department_name"));
        this.setPosition_name((String) data.get("position_name"));
        LocalDateTime checkInTime = (LocalDateTime) data.get("check_in_time");
        LocalDateTime checkOutTime = (LocalDateTime) data.get("check_out_time");
        this.setCheck_in_time(checkInTime != null ? LocalDateTime.parse(checkInTime.toString()) : null);
        this.setCheck_out_time(checkOutTime != null ? LocalDateTime.parse(checkOutTime.toString()) : null);
        this.setWork_time((Time) data.get("work_time"));
    }
}

