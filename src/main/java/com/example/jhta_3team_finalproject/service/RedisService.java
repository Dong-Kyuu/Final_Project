package com.example.jhta_3team_finalproject.service;

import com.example.jhta_3team_finalproject.domain.chat.ChatMessage;
import com.example.jhta_3team_finalproject.mybatis.mapper.ChatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final ChatMapper dao;

    public List<ChatMessage> getChatMessage() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomNum(5);
        return dao.searchMessages(chatMessage);
    }
}