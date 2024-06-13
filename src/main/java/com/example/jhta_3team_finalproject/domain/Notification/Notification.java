package com.example.jhta_3team_finalproject.domain.Notification;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class Notification {

    private int notificationToUserNum;
    private int notificationFromUserNum;
    private String notificationMessage;
    private String notificationUrl;
    private Date notificationRegdate;

    public Notification(){
    }

    public Notification(int notificationToUserNum, String notificationMessage){
        this.notificationToUserNum = notificationToUserNum;
        this.notificationMessage = notificationMessage;
    }

}
