package com.example.jhta_3team_finalproject.domain.User;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class AttendanceReqeust {
    private String action;

    public void setAction(String action) {
        this.action = action;
    }
}
