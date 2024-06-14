package com.example.jhta_3team_finalproject.service.chat;



import com.example.jhta_3team_finalproject.cache.RedisChatUtils;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.domain.chat.ChatMessage;
import com.example.jhta_3team_finalproject.domain.chat.ChatParticipate;
import com.example.jhta_3team_finalproject.domain.chat.ChatRoom;
import com.example.jhta_3team_finalproject.mybatis.mapper.chat.ChatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatMapper dao;
    private final RedisChatUtils redisChatUtils;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    LocalTime localTime = LocalTime.of(0, 0, 0);
    LocalDate localDate = LocalDate.now(); // 오늘 0시 0분 0초
    LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

    public ChatMessage createMessage(ChatMessage chatMessage) throws Exception {
        int result = dao.createMessage(chatMessage);
        if (result > 0) {
            /**
             * 2024-06-05, Insert 가 성공적으로 된 경우 Redis 에도 저장하기 위해 마지막 메시지를 가져옴
             */
            chatMessage = dao.lastMessage();

            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            String roomKey = String.valueOf(chatMessage.getChatRoomNum());
            String dateKey = simpleDateFormat.format(date.getTime());
            String redisKey = roomKey + ":" + dateKey; // 방번호:날짜
            Long expiredTime = 1L; // 만료 시간 1주일 부여

            redisChatUtils.setAddSets(redisKey, chatMessage); // 키, 값
            redisChatUtils.setExpired(redisKey, expiredTime);
        }
        return chatMessage; // 마지막 메시지를 반환
    }

    public ChatMessage updateMsgImageUrl(ChatMessage chatMessage) throws Exception {
        /**
         * 2024-06-07, Redis 에서 Update 가 필요한 oldChatMessage 를 저장해놓습니다.
         */
        ChatMessage oldChatMessage = dao.searchOldMessage(chatMessage);

        int result = dao.updateMsgImageUrl(chatMessage);

        if (result > 0) {
            /**
             * 2024-06-07, S3의 URL 이 정상적으로 Update 된 경우 Redis도 oldChatMessage를 newMessage로 Update
             */
            ChatMessage newChatMessage = dao.searchNewMessage(oldChatMessage);
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            String roomKey = String.valueOf(oldChatMessage.getChatRoomNum());
            String dateKey = simpleDateFormat.format(date.getTime());
            String redisKey = roomKey + ":" + dateKey; // 방번호:날짜
            Long expiredTime = 1L; // 만료 시간 1주일 부여

            redisChatUtils.setUpdateSets(redisKey, oldChatMessage, newChatMessage); // 키, old value, new value
            redisChatUtils.setExpired(redisKey, expiredTime);

            chatMessage = dao.lastMessage();
        }
        return chatMessage;
    }

    public ChatParticipate createChatRoom(ChatRoom chatRoom, String chatInviteUserList) throws Exception {
        ChatParticipate chatParticipate = new ChatParticipate();

        int result = dao.createChatRoom(chatRoom);

        if(result > 0) {
            chatRoom = dao.lastChatRoom();

            addChatParticipate(chatRoom, chatInviteUserList, chatParticipate);

            chatParticipate = dao.searchLastRoomUser(chatRoom);
        }

        return chatParticipate;
    }

    public void addChatParticipate(ChatRoom chatRoom, String chatInviteUserList, ChatParticipate chatParticipate) {
        /**
         * 2024-06-10, 자신의 정보도 채팅방 참여 테이블에 등록
         */
        chatParticipate.setChatRoomNum(chatRoom.getChatRoomNum());
        chatParticipate.setChatUserId(chatRoom.getChatSessionId());
        dao.addChatParticipate(chatParticipate);

        /**
         * 2024-06-10, 채팅방을 생성하기 전 초대된 유저들을 관리할 수 있는 채팅 참여 테이블에 등록
         */
        String[] chatInviteUserArray = chatInviteUserList.split(",");

        for(String inviteUserId : chatInviteUserArray) {
            chatParticipate.setChatRoomNum(chatRoom.getChatRoomNum());
            chatParticipate.setChatUserId(inviteUserId);
            dao.addChatParticipate(chatParticipate);
        }
    }

    public void exitChatRoom(String sessionId, String chatExitRoomList) {
        final int NO_PARTICIPATE = 0;
        /**
         * 2024-06-12, 채팅방 나가기를 구현합니다. 참가중인 채팅방에서 자신의 아이디를 뺍니다.
         */
        ChatParticipate chatParticipate = new ChatParticipate();
        String[] chatExitRoomArray = chatExitRoomList.split(",");

        for(String exitRoomNum : chatExitRoomArray) {
            chatParticipate.setChatRoomNum(Long.parseLong(exitRoomNum));
            chatParticipate.setChatUserId(sessionId);
            dao.participateExitChatRoom(chatParticipate);

            /**
             * 2024-06-12, 참가 테이블의 인원이 0명인 경우, 해당되는 채팅방을 삭제합니다.
             *              채팅방 삭제 시, 채팅방에 해당하는 메시지들을 cascade delete 합니다.
             */
            if(dao.isChatRoomParticipate(chatParticipate) == NO_PARTICIPATE) dao.deleteChatRoom(chatParticipate);
        }
    }

    public List<ChatMessage> searchMessages(ChatMessage chatMessage) throws Exception {
        return dao.searchMessages(chatMessage);
    }

    public List<ChatParticipate> searchRoom(ChatRoom chatRoom) throws Exception {
        return dao.searchRoom(chatRoom);
    }

    public List<ChatParticipate> searchRoomUser(ChatRoom chatRoom) throws Exception {
        return dao.searchRoomUser(chatRoom);
    }

    public List<User> chatUserList(String chatUserId) {
        return dao.chatUserList(chatUserId);
    }

    public int getUnreadMessage(ChatParticipate chatParticipate) throws Exception {
        return dao.getUnreadMessage(chatParticipate);
    }

    public User chatUserProfile(String chatUserId) {
        return dao.chatUserProfile(chatUserId);
    }

    public int chatUserProfileMsgUpdate(String profileStatusMsg, String chatUserId) {
        User user = new User();
        user.setUserChatStatusMsg(profileStatusMsg);
        user.setUserId(chatUserId);
        return dao.chatUserProfileMsgUpdate(user);
    }

    public ChatRoom getChatRoomInfo(ChatParticipate chatParticipate) {
        return dao.getChatRoomInfo(chatParticipate);
    }

    public int getChatRoomUserCount(ChatParticipate chatParticipate) {
        return dao.getChatRoomUserCount(chatParticipate);
    }

    public List<ChatParticipate> getChatRoomUserList(ChatParticipate chatParticipate) {
        return dao.getChatRoomUserList(chatParticipate);
    }

    public List<User> getChatRoomCanInviteUserList(ChatRoom chatRoom) {
        return dao.getChatRoomCanInviteUserList(chatRoom);
    }

    public long addChatParticipate(ChatParticipate chatParticipate, String chatInviteUserList) {
        /**
         * 2024-06-13, 초대한 유저들을 참가 테이블에 추가
         */
        String[] chatInviteUserArray = chatInviteUserList.split(",");
        for(String inviteUserId : chatInviteUserArray) {
            chatParticipate.setChatUserId(inviteUserId);
            dao.addChatParticipate(chatParticipate);
        }
        return chatParticipate.getChatRoomNum();
    }

    public long participateExitChatRoom(ChatParticipate chatParticipate, String chatExitUserList) {
        String[] chatExitUserArray = chatExitUserList.split(",");

        for(String exitUser : chatExitUserArray) {
            chatParticipate.setChatRoomNum(chatParticipate.getChatRoomNum());
            chatParticipate.setChatUserId(exitUser);
            dao.participateExitChatRoom(chatParticipate);
        }

        return chatParticipate.getChatRoomNum();
    }

    public int isp2pChatRoom(String chatCounterpartId, String chatUserId, String type) {
        final int NO_CHATROOM = -1;
        final int NO_CREATE_P2P_CHATROOM = 0;

        int result = dao.isp2pChatRoom(chatCounterpartId, chatUserId, type);

        if (result > 0) { // 1대1 채팅방이 존재 (해당 채팅방 번호를 return)
            return result;
        } else { // 1대1 채팅방이 존재하지 않음 (방을 생성하고 해당 채팅방의 번호를 return)
            /**
             * 2024-06-13, 1 대 1 채팅방 생성
             */
            String roomName = "";
            User user = dao.chatUserProfile(chatCounterpartId);
            roomName += user.getUsername();
            user = dao.chatUserProfile(chatUserId);
            roomName += ", " + user.getUsername();
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setRoomName(roomName);
            chatRoom.setChatSessionId(chatUserId);
            result = dao.createp2pChatRoom(chatRoom);

            /**
             * 2024-06-14, 방이 잘 생성되었다면 상대방 초대
             */
            if(result > NO_CREATE_P2P_CHATROOM) {
                addp2pChatParticipate(chatCounterpartId, chatUserId);
                return dao.lastChatRoomNum();
            } else {
                return NO_CHATROOM;
            }
        }
    }

    public void addp2pChatParticipate(String chatCounterpartId, String chatUserId) {
        ChatParticipate chatParticipate = new ChatParticipate();
        chatParticipate.setChatRoomNum(dao.lastChatRoomNum());
        chatParticipate.setChatUserId(chatUserId);
        dao.addp2pChatParticipate(chatParticipate); // 마지막으로 생성된 1대1 채팅방에 자신을 참가시킨다.
        chatParticipate.setChatRoomNum(dao.lastChatRoomNum());
        chatParticipate.setChatUserId(chatCounterpartId);
        dao.addp2pChatParticipate(chatParticipate); // 마지막으로 생성된 1대1 채팅방에 상대방을 참가시킨다.
    }

    public List<User> chatRoomParticipateList(ChatRoom chatRoom) {
        return dao.chatRoomParticipateList(chatRoom);
    }
}
