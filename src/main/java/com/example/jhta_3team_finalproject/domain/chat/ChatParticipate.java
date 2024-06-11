package com.example.jhta_3team_finalproject.domain.chat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Setter
@Getter
public class ChatParticipate {
    private String chatUserId;
    private long chatRoomNum;
    private Date chatEntryTime;
    private Date chatVisitTime;

    /**
     * 2024-06-10, 확장 - 마지막 메시지, 마지막 메시지의 시간, 방이름, 방생성 시간
     */
    private Date sendTime;
    private String messageContent;
    private String roomName;
    private Date roomCreateDate;
}
