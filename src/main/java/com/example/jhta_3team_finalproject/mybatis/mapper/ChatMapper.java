package com.example.jhta_3team_finalproject.mybatis.mapper;


import com.example.jhta_3team_finalproject.domain.User;
import com.example.jhta_3team_finalproject.domain.chat.ChatMessage;
import com.example.jhta_3team_finalproject.domain.chat.ChatRoom;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {

    public int createMessage(ChatMessage chatMessage);

    public int createChatRoom(ChatRoom chatRoom);

    public List<ChatRoom> searchRoom(ChatRoom chatRoom);

    public List<ChatRoom> searchRoomUser(ChatRoom chatRoom);

    public List<ChatMessage> searchMessages(ChatMessage chatMessage);

    public List<User> chatUserList(String chat_user_id);
}
