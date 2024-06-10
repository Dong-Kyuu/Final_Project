package com.example.jhta_3team_finalproject.domain.chat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Setter
@Getter
public class ChatRoom {

    private long chatRoomNum;
    private String chatSessionId;
    private String roomName;
    private Date roomCreateDate;
}