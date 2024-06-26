package com.example.jhta_3team_finalproject.domain.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterAllow {
    private int userNum;
    private String userName;
    private String userEmail;
    private String userPhoneNumber;
    private String departmentName;
    private String positionName;
    private int userIsApproved;
    private String userCreatedAt;
}
