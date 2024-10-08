package com.example.jhta_3team_finalproject.mybatis.mapper.chat;


import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.domain.chat.ChatMessage;
import com.example.jhta_3team_finalproject.domain.chat.ChatParticipate;
import com.example.jhta_3team_finalproject.domain.chat.ChatRoom;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {

    public int createMessage(ChatMessage chatMessage);

    public ChatMessage lastMessage();

    public int createChatRoom(ChatRoom chatRoom);

    public ChatRoom lastChatRoom();

    public int addChatParticipate(ChatParticipate chatParticipate);

    public List<ChatParticipate> searchRoom(ChatRoom chatRoom);

    public List<ChatParticipate> searchRoomUser(ChatRoom chatRoom);

    public ChatParticipate searchLastRoomUser(ChatRoom chatRoom);

    public List<ChatMessage> searchChatMessages(ChatMessage chatMessage);

    public List<ChatMessage> redisSearchMessages(ChatMessage chatMessage);

    public int updateMsgImageUrl(ChatMessage chatMessage);

    public ChatMessage searchOldMessage(ChatMessage chatMessage);

    public ChatMessage searchNewMessage(ChatMessage chatMessage);

    public List<User> chatUserList(String chatUserId);

    public User chatUserProfile(String chatUserId);

    public int chatUserProfileMsgUpdate(User user);

    public void participateExitChatRoom(ChatParticipate chatParticipate);

    public int isChatRoomParticipate(ChatParticipate chatParticipate);

    public void deleteChatRoom(ChatParticipate chatParticipate);

    public ChatRoom getChatRoomInfo(ChatParticipate chatParticipate);

    public int getChatRoomUserCount(ChatParticipate chatParticipate);

    public List<ChatParticipate> getChatRoomUserList(ChatParticipate chatParticipate);

    public List<User> getChatRoomCanInviteUserList(ChatRoom chatRoom);

    public int isp2pChatRoom(String chatCounterpartId, String chatUserId, String type);

    public int createp2pChatRoom(ChatRoom chatRoom);

    public void addp2pChatParticipate(ChatParticipate chatParticipate);

    public int lastChatRoomNum();

    public List<User> chatRoomParticipateList(ChatRoom chatRoom);

    public int initChatRoomVisitTime(ChatParticipate chatParticipate);

    public ChatMessage getLastDay(ChatMessage chatMessage);

    public List<ChatRoom> getDeptChatRoomNum();
}
