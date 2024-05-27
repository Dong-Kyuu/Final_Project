package com.example.jhta_3team_finalproject.domain.chat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Setter
@Getter
public class ChatRoom {
//	int roomNumber;
//	String roomName;
//	String sessionId;
//	String description;

	private long chat_room_num;
	private String chat_session_id;
	private String room_name;
	private Date room_create_date;

}