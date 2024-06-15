package com.example.jhta_3team_finalproject.controller;


import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.domain.chat.ChatMessage;
import com.example.jhta_3team_finalproject.domain.chat.ChatParticipate;
import com.example.jhta_3team_finalproject.domain.chat.ChatRoom;
import com.example.jhta_3team_finalproject.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/chat")
public class ChatController {

    private final ChatService chatService;
    List<ChatParticipate> chaParticiPateList;
    List<User> chatUserList;
    static int roomNumber = 0;

    @RequestMapping(value = "chatview")
    public String chatMainViewer() {
        return "chat/blank-page";
    }

    @RequestMapping(value = "chat")
    public String chatViewer() {
        return "chat/chat";
    }

    @RequestMapping(value = "chatUserProfile")
    public @ResponseBody User chatUserProfile(@RequestParam(value = "chatUserId") String chatUserId) {
        // 사원 리스트를 불러옵니다.
        return chatService.chatUserProfile(chatUserId);
    }

    @RequestMapping(value = "chatUserProfileMsg")
    public @ResponseBody int chatUserProfileMsg(@RequestParam(value = "profileStatusMsg", defaultValue = "") String profileStatusMsg,
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
    public @ResponseBody List<ChatParticipate> userChatRoomList(@RequestParam HashMap<String, String> params) throws Exception {
        log.info("아이디별 채팅방 구하기");
        String chatSessionId = params.get("chatUserId");
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatSessionId(chatSessionId);
        chaParticiPateList = chatService.searchRoomUser(chatRoom);
        return chaParticiPateList;
    }

    @RequestMapping("getRoom")
    public @ResponseBody List<ChatParticipate> getRoom(@RequestParam HashMap<Object, Object> params) throws Exception {
        log.info("관리자용 채팅방 전체 구하기");
        ChatRoom chatRoomEmpty = new ChatRoom();
        chaParticiPateList = chatService.searchRoom(chatRoomEmpty);
        return chaParticiPateList;
    }

    @RequestMapping("getUnreadMessage")
    public @ResponseBody int getUnreadMessage(@RequestParam HashMap<String, String> params) throws Exception {
        String sessionId = params.get("sessionId");
        String roomNumber = params.get("roomNumber");
        ChatParticipate chatParticipate = new ChatParticipate();
        chatParticipate.setChatUserId(sessionId);
        chatParticipate.setChatRoomNum(Long.valueOf(roomNumber));
        int unreadCount = chatService.getUnreadMessage(chatParticipate);
        return unreadCount;
    }

    @RequestMapping("getChatRoomInfo")
    public @ResponseBody Map<String, Object> getChatRoomInfo(
            @RequestParam(value = "chatRoomNum") String chatRoomNum,
            @RequestParam(value = "chatUserId") String chatUserId) {
        Map<String, Object> map = new HashMap<>();
        /**
         * 2024-06-13, 채팅방 이름 가져오기
         */
        ChatParticipate chatParticipate = new ChatParticipate();
        chatParticipate.setChatRoomNum(Long.parseLong(chatRoomNum));
        chatParticipate.setChatUserId(chatUserId);
        ChatRoom chatRoom = chatService.getChatRoomInfo(chatParticipate);

        /**
         * 2024-06-13, 채팅방 인원 수 가져오기
         */
        chatParticipate.setChatRoomNum(Long.parseLong(chatRoomNum));
        int userCount = chatService.getChatRoomUserCount(chatParticipate);

        /**
         * 2024-06-13, 해당 채팅방 참가 유저 리스트 가져오기
         */
        chaParticiPateList = chatService.getChatRoomUserList(chatParticipate);

        map.put("roomName", chatRoom.getRoomName());
        map.put("userCount", userCount);
        map.put("chatRoomPartList", chaParticiPateList);
        return map;
    }

    @RequestMapping(value = "isp2pChatRoom")
    public @ResponseBody int isp2pChatRoom(@RequestParam(value = "chatCounterpartId") String chatCounterpartId,
                                           @RequestParam(value = "chatUserId") String chatUserId,
                                           @RequestParam(value = "type") String type){

        return chatService.isp2pChatRoom(chatCounterpartId, chatUserId, type);
    }

    @RequestMapping(value = "chatRoomCreateView")
    public ModelAndView chatRoomCreateView(ModelAndView mv,
                                           @RequestParam(value = "type") String type,
                                           @RequestParam(value = "name") String name,
                                           @RequestParam(value = "roomButton") String roomButton,
                                           @RequestParam(value = "chatUserId") String chatUserId) {

        chatUserList = chatService.chatUserList(chatUserId);

        mv.setViewName("chat/roomMgr");
        mv.addObject("type", type);
        mv.addObject("name", name);
        mv.addObject("roomButton", roomButton);
        mv.addObject("chatUserId", chatUserId);
        mv.addObject("chatUserList", chatUserList);
        return mv;
    }

    @RequestMapping("createRoomProcess")
    public @ResponseBody ChatParticipate createRoomProcess(@RequestParam HashMap<Object, Object> params) throws Exception {
        String roomName = (String) params.get("roomName");
        String sessionId = (String) params.get("sessionId");
        String chatInviteUserList = (String) params.get("chatInviteUserList");
        ChatRoom chatRoom = new ChatRoom();
        ChatParticipate chatParticipate = new ChatParticipate();

        if (roomName != null && !roomName.trim().equals("")) {
            chatRoom.setRoomName(roomName);
            chatRoom.setChatSessionId(sessionId);
            chatParticipate = chatService.createChatRoom(chatRoom, chatInviteUserList);
            return chatParticipate;
        }

        return chatParticipate;
    }

    @RequestMapping(value = "chatRoomExitView")
    public ModelAndView chatRoomExitView(ModelAndView mv,
                                         @RequestParam(value = "type") String type,
                                         @RequestParam(value = "name") String name,
                                         @RequestParam(value = "roomButton") String roomButton,
                                         @RequestParam(value = "chatUserId") String chatUserId) throws Exception {

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatSessionId(chatUserId);
        chaParticiPateList = chatService.searchRoomUser(chatRoom);

        mv.setViewName("chat/roomMgr");
        mv.addObject("type", type);
        mv.addObject("name", name);
        mv.addObject("roomButton", roomButton);
        mv.addObject("chatUserId", chatUserId);
        mv.addObject("chatRoomList", chaParticiPateList);
        return mv;
    }

    @RequestMapping("exitRoomProcess")
    public @ResponseBody List<ChatParticipate> exitRoomProcess(@RequestParam HashMap<Object, Object> params) throws Exception {
        String sessionId = (String) params.get("sessionId");
        String chatExitRoomList = (String) params.get("chatExitRoomList");
        ChatRoom chatRoom = new ChatRoom();
        List<ChatParticipate> chatParticipate = new ArrayList<>();

        if (sessionId != null && !sessionId.trim().equals("")) {
            chatRoom.setChatSessionId(sessionId);
            chatService.exitChatRoom(sessionId, chatExitRoomList);
            chatParticipate = chatService.searchRoomUser(chatRoom);
            return chatParticipate;
        }
        return chatParticipate;
    }

    @RequestMapping(value = "chatUserInviteView")
    public ModelAndView chatUserInviteView(ModelAndView mv,
                                           @RequestParam(value = "type") String type,
                                           @RequestParam(value = "name") String name,
                                           @RequestParam(value = "userButton") String userButton,
                                           @RequestParam(value = "chatUserId") String chatUserId,
                                           @RequestParam(value = "chatRoomNum") String chatRoomNum) {

        /**
         * 2024-06-13, 채팅방에 없는 유저 리스트 가져오기 (초대할 수 있는 유저 리스트)
         */
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatRoomNum(Long.parseLong(chatRoomNum));
        List<User> user = chatService.getChatRoomCanInviteUserList(chatRoom);

        mv.setViewName("chat/chatUserMgr");
        mv.addObject("type", type);
        mv.addObject("name", name);
        mv.addObject("userButton", userButton);
        mv.addObject("chatUserId", chatUserId);
        mv.addObject("chatRoomNum", chatRoomNum);
        mv.addObject("userList", user);
        return mv;
    }

    @RequestMapping("inviteChatUser")
    public @ResponseBody long inviteChatUser(@RequestParam HashMap<Object, Object> params) throws Exception {
        final int NO_USER = -1;
        String chatRoomNum = (String) params.get("chatRoomNum");
        String chatInviteUserList = (String) params.get("chatInviteUserList");

        ChatParticipate chatParticipate = new ChatParticipate();
        chatParticipate.setChatRoomNum(Long.parseLong(chatRoomNum));

        /**
         * 2024-06-13, 채팅방 유저 초대
         */
        if (chatRoomNum != null && !chatRoomNum.trim().equals("")) {
            return chatService.addChatParticipate(chatParticipate, chatInviteUserList);
        }
        return NO_USER;
    }

    @RequestMapping(value = "chatUserMgrView")
    public ModelAndView chatUserMgrView(ModelAndView mv,
                                        @RequestParam(value = "type") String type,
                                        @RequestParam(value = "name") String name,
                                        @RequestParam(value = "userButton") String userButton,
                                        @RequestParam(value = "chatUserId") String chatUserId,
                                        @RequestParam(value = "chatRoomNum") String chatRoomNum) {

        /**
         * 2024-06-13, 유저 관리 클릭 시 해당 채팅방 유저 리스트 가져오기
         */
        ChatParticipate chatParticipate = new ChatParticipate();
        chatParticipate.setChatRoomNum(Long.parseLong(chatRoomNum));
        chatParticipate.setChatUserId(chatUserId);
        List<ChatParticipate> chatParticipates = chatService.getChatRoomUserList(chatParticipate);

        mv.setViewName("chat/chatUserMgr");
        mv.addObject("type", type);
        mv.addObject("name", name);
        //mv.addObject("roomButton", roomButton);
        mv.addObject("userButton", userButton);
        mv.addObject("chatRoomNum", chatRoomNum);
        mv.addObject("chatUserId", chatUserId);
        mv.addObject("userList", chatParticipates);
        return mv;
    }

    @RequestMapping("exitChatUser")
    public @ResponseBody long exitChatUser(@RequestParam HashMap<Object, Object> params) throws Exception {
        final int NO_USER = -1;
        String chatRoomNum = (String) params.get("chatRoomNum");
        String chatExitUserList = (String) params.get("chatExitUserList");

        ChatParticipate chatParticipate = new ChatParticipate();
        chatParticipate.setChatRoomNum(Long.parseLong(chatRoomNum));

        /**
         * 2024-06-13, 채팅방 유저 추방
         */
        if (chatRoomNum != null && !chatRoomNum.trim().equals("")) {
            return chatService.participateExitChatRoom(chatParticipate, chatExitUserList);
        }
        return NO_USER;
    }

    @RequestMapping("initChatRoomVisitTime")
    public @ResponseBody int initChatRoomVisitTime(ChatParticipate chatParticipate) {
        return chatService.initChatRoomVisitTime(chatParticipate);
    }

}