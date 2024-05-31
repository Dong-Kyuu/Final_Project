package com.example.jhta_3team_finalproject.controller;


import com.example.jhta_3team_finalproject.domain.User;
import com.example.jhta_3team_finalproject.domain.chat.ChatRoom;
import com.example.jhta_3team_finalproject.service.ChattingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/chat")
public class ChatController {

    private final ChattingService chattingService;

    List<ChatRoom> chatRoomList;
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
    public @ResponseBody User chatUserProfile(@RequestParam(value = "chat_user_id") String chat_user_id) {
        // 사원 리스트를 불러옵니다.
        return chattingService.chatUserProfile(chat_user_id);
    }

    @RequestMapping(value = "chatUserProfileMsg")
    public @ResponseBody int chatUserProfileMsg(@RequestParam(value = "profile_status_msg", defaultValue = "") String profile_status_msg,
                                  @RequestParam(value = "chat_user_id") String chat_user_id) {
        // 사원의 프로필 상태 메시지를 업데이트합니다.
        return chattingService.chatUserProfileMsgUpdate(profile_status_msg, chat_user_id);
    }

    @RequestMapping(value = "chatUserList")
    public @ResponseBody List<User> chatUserList(@RequestParam(value = "chat_user_id") String chat_user_id) {
        // 사원 리스트를 불러옵니다.
        return chattingService.chatUserList(chat_user_id);
    }


    @RequestMapping("userChatRoomList")
    public @ResponseBody List<ChatRoom> userChatRoomList(@RequestParam HashMap<String, String> params) throws Exception {
        log.info("아이디별 채팅방 구하기");
        String chat_user_id = params.get("chat_user_id");
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChat_session_id(chat_user_id);
        chatRoomList = chattingService.searchRoomUser(chatRoom);
        return chatRoomList;
    }

    @RequestMapping("getRoom")
    public @ResponseBody List<ChatRoom> getRoom(@RequestParam HashMap<Object, Object> params) throws Exception {
        log.info("관리자용 채팅방 전체 구하기");
        ChatRoom chatRoom_empty = new ChatRoom();
        chatRoomList = chattingService.searchRoom(chatRoom_empty);
        return chatRoomList;
    }

    @RequestMapping(value = "chatRoomCreateView")
    public ModelAndView chatRoomCreateView(ModelAndView mv,
                                           @RequestParam(value = "type") String type,
                                           @RequestParam(value = "name") String name,
                                           @RequestParam(value = "roomButton") String roomButton,
                                           @RequestParam(value = "chat_user_id") String chat_user_id) {
        mv.setViewName("chat/roomMgr");
        mv.addObject("type", type);
        mv.addObject("name", name);
        mv.addObject("roomButton", roomButton);
        mv.addObject("chat_user_id", chat_user_id);
        return mv;
    }

    @RequestMapping("createRoomProcess")
    public @ResponseBody List<ChatRoom> createRoomProcess(@RequestParam HashMap<Object, Object> params) throws Exception {
        String roomName = (String) params.get("roomName");
        String sessionId = (String) params.get("sessionId");

        ChatRoom chatRoom_empty = new ChatRoom();
        chatRoomList = chattingService.searchRoom(chatRoom_empty);

        if (roomName != null && !roomName.trim().equals("")) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setChat_room_num(++roomNumber);
            chatRoom.setRoom_name(roomName);
            chatRoom.setChat_session_id(sessionId);
            chattingService.createChatRoom(chatRoom);
            chatRoomList = chattingService.searchRoom(chatRoom);
        }

        return chatRoomList;
    }

    @RequestMapping(value = "chatRoomExitView")
    public ModelAndView chatRoomExitView(ModelAndView mv,
                                         @RequestParam(value = "type") String type,
                                         @RequestParam(value = "name") String name,
                                         @RequestParam(value = "roomButton") String roomButton) {
        mv.setViewName("chat/roomMgr");
        mv.addObject("type", type);
        mv.addObject("name", name);
        mv.addObject("roomButton", roomButton);
        return mv;
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


    @RequestMapping("moveChating")
    public ModelAndView chating(@RequestParam HashMap<Object, Object> params) {
        log.info("채팅방 이동");

        ModelAndView mv = new ModelAndView();
        int roomNumber = Integer.parseInt((String) params.get("roomNumber"));

        List<ChatRoom> new_list = chatRoomList.stream().filter(o -> o.getChat_room_num() == roomNumber)
                .collect(Collectors.toList());
        if (new_list != null && new_list.size() > 0) {
            mv.addObject("roomName", params.get("roomName"));
            mv.addObject("roomNumber", params.get("roomNumber"));
            mv.setViewName("chat/testchat");
        } else {
            mv.setViewName("chat/testroom");
        }
        return mv;
    }

    @RequestMapping("testchat")
    public ModelAndView chat() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("chat/testchat");
        return mv;
    }

    @RequestMapping("testroom")
    public ModelAndView room() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("chat/testroom");
        return mv;
    }

}