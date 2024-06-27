package com.example.jhta_3team_finalproject.domain.chat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class ChatRoomView {

    private String type;
    private String name;
    private String userButton;
    private String roomButton;
    private String chatRoomNum;
    private String chatUserId;
}
