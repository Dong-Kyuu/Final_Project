package com.example.jhta_3team_finalproject.controller;


import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.domain.chat.ChatMessage;
import com.example.jhta_3team_finalproject.domain.chat.ChatParticipate;
import com.example.jhta_3team_finalproject.domain.chat.ChatRoom;
import com.example.jhta_3team_finalproject.domain.chat.ChatRoomView;
import com.example.jhta_3team_finalproject.service.chat.ChatService;
import com.example.jhta_3team_finalproject.service.chat.ChatSseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/chat")
public class ChatController {

    private final ChatService chatService;
    List<ChatParticipate> chatParticiPateList;
    List<User> chatUserList;

    @RequestMapping(value = "chatview")
    public String chatMainViewer() {
        return "chat/chat-page";
    }

    @RequestMapping(value = "chat")
    public String chatViewer() {
        return "chat/chat";
    }

    @RequestMapping(value = "chatUserProfile")
    public @ResponseBody User chatUserProfile(@RequestParam(value = "chatUserId") String chatUserId) {
        // 자신의 프로필을 가져옵니다.
        return chatService.chatUserProfile(chatUserId);
    }

    @RequestMapping(value = "chatUserProfileMsg")
    public @ResponseBody int chatUserProfileMsg(
                                  @RequestParam(value = "profileStatusMsg", defaultValue = "") String profileStatusMsg,
                                  @RequestParam(value = "chatUserId") String chatUserId) {
        // 사원의 프로필 상태 메시지를 업데이트합니다.
        return chatService.chatUserProfileMsgUpdate(profileStatusMsg, chatUserId);
    }

    @RequestMapping(value = "chatUserList")
    public @ResponseBody List<User> chatUserList(@RequestParam(value = "chatUserId") String chatUserId) {
        // 사원 리스트를 불러옵니다.
        return chatService.chatUserList(chatUserId);
    }

    @RequestMapping("userChatRoomList")
    public @ResponseBody List<ChatParticipate> userChatRoomList(ChatRoom chatRoom) throws Exception {
        log.info("아이디별 채팅방 구하기");
        chatParticiPateList = chatService.searchRoomUser(chatRoom);
        return chatParticiPateList;
    }

    @RequestMapping("getChatRoomInfo")
    public @ResponseBody Map<String, Object> getChatRoomInfo(ChatParticipate chatParticipate) {
        Map<String, Object> chatInfoMap = new HashMap<>();
        /**
         * 2024-06-13, 채팅방 이름 가져오기
         */
        ChatRoom chatRoom = chatService.getChatRoomInfo(chatParticipate);

        /**
         * 2024-06-13, 채팅방 인원 수 가져오기
         */
        int userCount = chatService.getChatRoomUserCount(chatParticipate);

        /**
         * 2024-06-13, 해당 채팅방 참가 유저 리스트 가져오기
         */
        chatParticiPateList = chatService.getChatRoomUserList(chatParticipate);

        chatInfoMap.put("roomName", chatRoom.getRoomName());
        chatInfoMap.put("userCount", userCount);
        chatInfoMap.put("chatRoomPartList", chatParticiPateList);
        return chatInfoMap;
    }

    @RequestMapping("initChatRoomVisitTime")
    public @ResponseBody int initChatRoomVisitTime(ChatParticipate chatParticipate) {
        return chatService.initChatRoomVisitTime(chatParticipate);
    }

    @RequestMapping(value = "getChatMessages")
    public @ResponseBody List<ChatMessage> getChatMessages(ChatMessage chatMessage) throws Exception {
        return chatService.getChatMessages(chatMessage);
    }

    @RequestMapping(value = "searchMessages")
    public @ResponseBody List<ChatMessage> searchMessages(ChatMessage chatMessage) throws Exception {
        return chatService.searchChatMessages(chatMessage);
    }

    @RequestMapping(value = "isp2pChatRoom")
    public @ResponseBody int isp2pChatRoom(@RequestParam(value = "chatCounterpartId") String chatCounterpartId,
                                           @RequestParam(value = "chatUserId") String chatUserId,
                                           @RequestParam(value = "type") String type){

        return chatService.isp2pChatRoom(chatCounterpartId, chatUserId, type);
    }

    @RequestMapping(value = "chatRoomCreateView")
    public ModelAndView chatRoomCreateView(ModelAndView mv, ChatRoomView chatRoomView) {

        chatUserList = chatService.chatUserList(chatRoomView.getChatUserId());

        mv.setViewName("chat/roomMgr");
        mv.addObject("type", chatRoomView.getType());
        mv.addObject("name", chatRoomView.getName());
        mv.addObject("roomButton", chatRoomView.getRoomButton());
        mv.addObject("chatUserId", chatRoomView.getChatUserId());
        mv.addObject("chatUserList", chatUserList);

        return mv;
    }

    @RequestMapping("createRoomProcess")
    public @ResponseBody ChatParticipate createRoomProcess(ChatRoom chatRoom) throws Exception {
        ChatParticipate chatParticipate = new ChatParticipate();

        if (chatRoom.getRoomName() != null && !chatRoom.getRoomName().trim().equals("")) {
            chatParticipate = chatService.createChatRoom(chatRoom);
            return chatParticipate;
        }

        return chatParticipate;
    }

    @RequestMapping(value = "chatRoomExitView")
    public ModelAndView chatRoomExitView(ModelAndView mv, ChatRoomView chatRoomView) throws Exception {

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatManagerId(chatRoomView.getChatUserId());
        chatParticiPateList = chatService.searchRoomUser(chatRoom);

        mv.setViewName("chat/roomMgr");
        mv.addObject("type", chatRoomView.getType());
        mv.addObject("name", chatRoomView.getName());
        mv.addObject("roomButton", chatRoomView.getRoomButton());
        mv.addObject("chatUserId", chatRoomView.getChatUserId());
        mv.addObject("chatRoomList", chatParticiPateList);
        return mv;
    }

    @RequestMapping("exitRoomProcess")
    public @ResponseBody List<ChatParticipate> exitRoomProcess(ChatParticipate chatParticipate) throws Exception {
        if (chatParticipate.getChatUserId() != null && !chatParticipate.getChatUserId().trim().equals("")) {
            chatService.exitChatRoom(chatParticipate.getChatUserId(), chatParticipate.getChatExitRoomList());
            ChatRoom  chatRoom = new ChatRoom();
            chatRoom.setChatManagerId(chatParticipate.getChatUserId());
            chatParticiPateList = chatService.searchRoomUser(chatRoom);
            return chatParticiPateList;
        }
        return chatParticiPateList;
    }

    @RequestMapping(value = "chatUserInviteView")
    public ModelAndView chatUserInviteView(ModelAndView mv, ChatRoomView chatRoomView) {
        /**
         * 2024-06-13, 채팅방에 없는 유저 리스트 가져오기 (초대할 수 있는 유저 리스트)
         */
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatRoomNum(Long.parseLong(chatRoomView.getChatRoomNum()));
        List<User> userList = chatService.getChatRoomCanInviteUserList(chatRoom);

        mv.setViewName("chat/chatUserMgr");
        mv.addObject("type", chatRoomView.getType());
        mv.addObject("name", chatRoomView.getName());
        mv.addObject("userButton", chatRoomView.getUserButton());
        mv.addObject("chatUserId", chatRoomView.getChatUserId());
        mv.addObject("chatRoomNum", chatRoomView.getChatRoomNum());
        mv.addObject("userList", userList);

        return mv;
    }

    @RequestMapping("inviteChatUser")
    public @ResponseBody long inviteChatUser(ChatParticipate chatParticipate) throws Exception {
        /**
         * 2024-06-13, 채팅방 유저 초대
         */
        return chatService.addChatParticipate(chatParticipate);
    }

    @RequestMapping(value = "chatUserMgrView")
    public ModelAndView chatUserMgrView(ModelAndView mv, ChatRoomView chatRoomView) {
        /**
         * 2024-06-13, 유저 관리 클릭 시 해당 채팅방 유저 리스트 가져오기
         */
        ChatParticipate chatParticipate = new ChatParticipate();
        chatParticipate.setChatRoomNum(Long.parseLong(chatRoomView.getChatRoomNum()));
        chatParticipate.setChatUserId(chatRoomView.getChatUserId());
        List<ChatParticipate> chatParticipates = chatService.getChatRoomUserList(chatParticipate);

        mv.setViewName("chat/chatUserMgr");
        mv.addObject("type", chatRoomView.getType());
        mv.addObject("name", chatRoomView.getName());
        mv.addObject("userButton", chatRoomView.getUserButton());
        mv.addObject("chatRoomNum", chatRoomView.getChatRoomNum());
        mv.addObject("chatUserId", chatRoomView.getChatUserId());
        mv.addObject("userList", chatParticipates);

        return mv;
    }

    @RequestMapping("exitChatUser")
    public @ResponseBody long exitChatUser(ChatParticipate chatParticipate) throws Exception {
        /**
         * 2024-06-13, 채팅방 유저 추방
         */
        return chatService.participateExitChatRoom(chatParticipate);
    }
}