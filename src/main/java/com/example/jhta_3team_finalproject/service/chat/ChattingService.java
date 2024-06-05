package com.example.jhta_3team_finalproject.service.chat;



import com.example.jhta_3team_finalproject.cache.RedisUtils;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.domain.chat.ChatMessage;
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
public class ChattingService {

    private final ChatMapper dao;
    private final RedisUtils redisUtils;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    LocalTime localTime = LocalTime.of(0, 0, 0);
    LocalDate localDate = LocalDate.now(); // 오늘 0시 0분 0초

    public int createMessage(ChatMessage chatMessage) throws Exception {
        int result = dao.createMessage(chatMessage);
        if (result > 0) {
            /**
             * 2024-06-05, insert가 성공적으로 된 경우 redis에도 저장
             */
            LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            String roomKey = String.valueOf(chatMessage.getChatRoomNum());
            String dateKey = simpleDateFormat.format(date.getTime());
            String redisKey = roomKey + ":" + dateKey; // 방번호:날짜
            Long expiredTime = 1L; // 만료 시간 1주일 부여
            redisUtils.setInitSets(redisKey, chatMessage); // 키, 값
            redisUtils.setExpired(redisKey, expiredTime);
        }
        return result;
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
        user.setUserChatStatusMsg(profileStatusMsg);
        user.setUserId(chatUserId);
        return dao.chatUserProfileMsgUpdate(user);
    }
}
