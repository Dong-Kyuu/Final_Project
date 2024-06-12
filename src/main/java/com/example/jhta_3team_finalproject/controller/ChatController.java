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
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/chat")
public class ChatController {

    private final ChatService chatService;
    List<ChatParticipate> chatRoomList;
    List<User> chatUserList;
    static int roomNumber = 0;

    @RequestMapping(value = "chatview")
    public String chatView() {
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
        chatRoomList = chatService.searchRoomUser(chatRoom);
        return chatRoomList;
    }

    @RequestMapping("getLastMessageContent")
    public @ResponseBody ChatMessage getLastMessageContent(@RequestParam HashMap<String, String> params) throws Exception {
        String lastMessageNum = params.get("lastMessageNum");
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessageNum(Long.valueOf(lastMessageNum));
        chatMessage = chatService.getLastMessageContent(chatMessage);
        return chatMessage;
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

    @RequestMapping("getRoom")
    public @ResponseBody List<ChatParticipate> getRoom(@RequestParam HashMap<Object, Object> params) throws Exception {
        log.info("관리자용 채팅방 전체 구하기");
        ChatRoom chatRoomEmpty = new ChatRoom();
        chatRoomList = chatService.searchRoom(chatRoomEmpty);
        return chatRoomList;
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
        //chatRoom.setChatSessionId(chatUserId);
        chatRoom.setChatSessionId("admin"); // 2024-06-08 테스트용
        chatRoomList = chatService.searchRoomUser(chatRoom);

        mv.setViewName("chat/roomMgr");
        mv.addObject("type", type);
        mv.addObject("name", name);
        mv.addObject("roomButton", roomButton);
        mv.addObject("chatUserId", chatUserId);
        mv.addObject("chatRoomList", chatRoomList);
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
                                           @RequestParam(value = "userButton") String userButton) {
        mv.setViewName("chat/chatUserMgr");
        mv.addObject("type", type);
        mv.addObject("name", name);
        mv.addObject("userButton", userButton);
        return mv;
    }

    @RequestMapping(value = "chatUserMgrView")
    public ModelAndView chatUserMgrView(ModelAndView mv,
                                        @RequestParam(value = "type") String type,
                                        @RequestParam(value = "name") String name,
                                        @RequestParam(value = "roomButton") String roomButton,
                                        @RequestParam(value = "userButton") String userButton) {
        mv.setViewName("chat/chatUserMgr");
        mv.addObject("type", type);
        mv.addObject("name", name);
        mv.addObject("roomButton", roomButton);
        mv.addObject("userButton", userButton);
        return mv;
    }

//    @RequestMapping("moveChatting")
//    public ModelAndView chatting(@RequestParam HashMap<Object, Object> params) {
//        log.info("채팅방 이동");
//
//        ModelAndView mv = new ModelAndView();
//        int roomNumber = Integer.parseInt((String) params.get("roomNumber"));
//
//        List<ChatRoom> new_list = chatRoomList.stream().filter(o -> o.getChatRoomNum() == roomNumber)
//                .collect(Collectors.toList());
//        if (new_list != null && new_list.size() > 0) {
//            mv.addObject("roomName", params.get("roomName"));
//            mv.addObject("roomNumber", params.get("roomNumber"));
//            mv.setViewName("chat/testchat");
//        } else {
//            mv.setViewName("chat/testroom");
//        }
//        return mv;
//    }

//    @RequestMapping("testchat")
//    public ModelAndView chat() {
//        ModelAndView mv = new ModelAndView();
//        mv.setViewName("chat/testchat");
//        return mv;
//    }
//
//    @RequestMapping("testroom")
//    public ModelAndView room() {
//        ModelAndView mv = new ModelAndView();
//        mv.setViewName("chat/testroom");
//        return mv;
//    }

}