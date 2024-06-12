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
       2024-06-12
       메시지 타입 : 입장, 채팅, 나감, 강퇴
       ENTER     -> 초대 기능으로 변경 예정
       TALK      -> 일반 채팅 기능
       QUIT      -> 채팅방 나가기로 구현 예정
       KICK      -> 채팅방 강퇴 기능으로 구현 예정
       EMERGENCY -> 긴급 공지 시 메시지 타입
       TIMESTAMP -> 시간을 나타내는 메시지로 구현
    */
    public enum MessageType {
        ENTER, TALK, QUIT, KICK, EMERGENCY, TIMESTAMP
    }

    private MessageType type; // 메시지 타입
    private long messageNum; // 메시지 번호
    private String messageContent; // 메시지 내용
    private int readCount; // 안 읽은 사람 수
    private Date sendTime; // 메시지 보낸 시간
    private String timeStamp; // 채팅 타임스탬프
    private String dateStr; // 날짜별 조회
    private String senderId; // 보낸 회원의 아이디
    private long chatRoomNum; // 채팅방 번호
    private String fileUrl; // 파일이 저장된 업로드 S3 버킷 위치 (DB 저장)
    private String fileOriginName; // 파일의 원본 이름
    private String s3url; // 업데이트할 S3 URL (업데이트용)
}
