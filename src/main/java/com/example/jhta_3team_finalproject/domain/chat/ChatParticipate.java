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
    private Date lastSendTime;
    private String messageContent;
    private String roomName;
    private Date roomCreateDate;

    /**
     * 2024-06-13, 확장 - 유저 이름, 유저 프로필 사진, 유저 이메일
     */
    private String userId;
    private String username;
    private String userProfilePicture;
    private String userEmail;
}
