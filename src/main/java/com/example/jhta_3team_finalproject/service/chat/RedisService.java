package com.example.jhta_3team_finalproject.service.chat;

import com.example.jhta_3team_finalproject.domain.chat.ChatMessage;
import com.example.jhta_3team_finalproject.mybatis.mapper.chat.ChatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final ChatMapper dao;

    public List<ChatMessage> getChatMessage(long num, Date date) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomNum(num);
        chatMessage.setSendTime(date);
        return dao.searchMessages(chatMessage);
    }
}
