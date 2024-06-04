package com.example.jhta_3team_finalproject.service.chat;



import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.domain.chat.ChatMessage;
import com.example.jhta_3team_finalproject.domain.chat.ChatRoom;
import com.example.jhta_3team_finalproject.mybatis.mapper.chat.ChatMapper;
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

    public int updateMsgImageUrl(ChatMessage chatMessage) throws Exception {
        return dao.updateMsgImageUrl(chatMessage);
    }

    public List<ChatRoom> searchRoom(ChatRoom chatRoom) throws Exception {
        return dao.searchRoom(chatRoom);
    }

    public List<ChatRoom> searchRoomUser(ChatRoom chatRoom) throws Exception {
        return dao.searchRoomUser(chatRoom);
    }

    public List<User> chatUserList(String chatUserId) {
        return dao.chatUserList(chatUserId);
    }

    public User chatUserProfile(String chatUserId) {
        return dao.chatUserProfile(chatUserId);
    }

    public int chatUserProfileMsgUpdate(String profileStatusMsg, String chatUserId) {
        User user = new User();
        user.setChat_status_msg(profileStatusMsg);
        user.setId(chatUserId);
        return dao.chatUserProfileMsgUpdate(user);
    }
}
