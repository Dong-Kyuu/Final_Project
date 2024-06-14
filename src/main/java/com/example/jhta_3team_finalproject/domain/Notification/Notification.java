package com.example.jhta_3team_finalproject.domain.Notification;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class Notification {

    private int notificationNum;
    private int notificationToUserNum;
    private String notificationToUserName;
    private int notificationFromUserNum;
    private String notificationFromUserName;
    private String notificationMessage;
    private String notificationUrl;
    private Date notificationRegdate;
    private int notificationIsread;


    public Notification(){
    }

    public Notification(int notificationToUserNum,
                        int notificationFromUserNum,
                        String notificationFromUserName,
                        String notificationUrl,
                        String notificationMessage){
        this.notificationToUserNum = notificationToUserNum;
        this.notificationFromUserNum = notificationFromUserNum;
        this.notificationFromUserName = notificationFromUserName;
        this.notificationMessage = notificationMessage;
        this.notificationUrl = notificationUrl;
    }
}
