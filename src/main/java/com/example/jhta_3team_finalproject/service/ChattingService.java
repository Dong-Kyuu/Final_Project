package com.example.jhta_3team_finalproject.service;



import com.example.jhta_3team_finalproject.domain.User;
import com.example.jhta_3team_finalproject.domain.chat.ChatMessage;
import com.example.jhta_3team_finalproject.domain.chat.ChatRoom;
import com.example.jhta_3team_finalproject.mybatis.mapper.ChatMapper;
import com.example.jhta_3team_finalproject.mybatis.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChattingService {

    private final ChatMapper dao;

    List<ChatRoom> chatChatRoomList;
    List<ChatMessage> chatMessageList;



    public int createMessage(ChatMessage chatMessage) throws Exception {
        return dao.createMessage(chatMessage);
    }

    public int createChatRoom(ChatRoom chatRoom) throws Exception {
        return dao.createChatRoom(chatRoom);
    }

    public List<ChatMessage> searchMessages(ChatMessage chatMessage) throws Exception {
        return dao.searchMessages(chatMessage);
    }

    public List<ChatRoom> searchRoom(ChatRoom chatRoom) throws Exception {
        return dao.searchRoom(chatRoom);
    }

    public List<ChatRoom> searchRoomUser(ChatRoom chatRoom) throws Exception {
        return dao.searchRoomUser(chatRoom);
    }

    public List<User> chatUserList(String chat_user_id) {
        return dao.chatUserList(chat_user_id);
    }
}
