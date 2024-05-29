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

//	private int chatNum;
//	private String roomNumber;
//	private String userName;
//	private String content;

    private MessageType type; // 메시지 타입
    private long message_num; // 메시지 번호
    private String message_content; // 메시지 내용
    private int read_count; // 안 읽은 사람 수
    private Date send_time; // 메시지 보낸 시간
    private String sender_id; // 보낸 회원의 아이디
    private String chat_room_num; // 채팅방 번호
}
