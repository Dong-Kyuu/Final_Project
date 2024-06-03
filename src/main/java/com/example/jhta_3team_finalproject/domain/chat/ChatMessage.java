package com.example.jhta_3team_finalproject.domain.chat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Setter
@Getter
public class ChatMessage {
    /*
       2024-05-23
       메시지 타입 : 입장, 채팅, 나감, 강퇴
       ENTER -> 초대 기능으로 변경 예정
       TALK  -> 일반 채팅 기능
       QUIT  -> 채팅방 나가기로 구현 예정
       KICK  -> 채팅방 강퇴 기능으로 구현 예정
    */
    public enum MessageType {
        ENTER, TALK, QUIT, KICK
    }

    private MessageType type; // 메시지 타입
    private long messageNum; // 메시지 번호
    private String messageContent; // 메시지 내용
    private int readCount; // 안 읽은 사람 수
    private Date sendTime; // 메시지 보낸 시간
    private String senderId; // 보낸 회원의 아이디
    private long chatRoomNum; // 채팅방 번호
}
