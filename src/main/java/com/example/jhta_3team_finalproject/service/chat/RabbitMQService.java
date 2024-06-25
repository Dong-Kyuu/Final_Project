//package com.example.jhta_3team_finalproject.service.chat;//package com.example.jhta_3team_finalproject.service.chat;
//
//import com.example.jhta_3team_finalproject.domain.User.User;
//import com.example.jhta_3team_finalproject.domain.chat.ChatMessage;
//import com.example.jhta_3team_finalproject.domain.chat.ChatRoom;
//import com.example.jhta_3team_finalproject.handler.chat.SocketHandler;
//import com.example.jhta_3team_finalproject.mybatis.mapper.chat.ChatMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
///**
// * Queue 로 메세지를 발핼한 때에는 RabbitTemplate 의 ConvertAndSend 메소드를 사용하고
// * Queue 에서 메세지를 구독할때는 @RabbitListener 을 사용
// *
// **/
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class RabbitMQService {
//
//    @Value("${rabbitmq.queue.name}")
//    private String queueName;
//
//    @Value("${rabbitmq.exchange.name}")
//    private String exchangeName;
//
//    @Value("${rabbitmq.routing.key}")
//    private String routingKey;
//
//    private final RabbitTemplate rabbitTemplate;
//
//    private final SocketHandler socketHandler;
//
//    private final ChatService chatService;
//
//    private final ChatSseService chatSseService;
//
//    private final ChatMapper dao;
//
//    /**
//     * 1. Queue 로 메세지를 발행
//     * 2. Producer 역할 -> Direct Exchange 전략
//     */
//    public void sendMessage(ChatMessage chatMessage) throws Exception {
//        log.info("Send Message : {}", chatMessage.toString());
//
//        /**
//         * 2024-06-24, 모든 부서 채팅방의 방 번호를 구함
//         */
//        List<ChatRoom> deptChatRoomList = dao.getDeptChatRoomNum();
//        deptChatRoomList.forEach(chatRoom -> {
//            chatMessage.setChatRoomNum(chatRoom.getChatRoomNum());
//            this.rabbitTemplate.convertAndSend(exchangeName, routingKey, chatMessage);
//        });
//    }
//
//    /**
//     * 1. Queue 에서 메세지를 구독
//     */
//    @Transactional
//    @RabbitListener(queues = "${rabbitmq.queue.name}")
//    public void receiveMessage(ChatMessage chatMessage) throws Exception {
//        log.info("Received Message : {}", chatMessage.toString());
//        chatMessage = chatService.createMessage(chatMessage);
//
//        if(chatMessage != null) {
//            ChatRoom chatRoom = new ChatRoom();
//            chatRoom.setChatRoomNum(Long.valueOf(chatMessage.getChatRoomNum()));
//            chatRoom.setChatSessionId(chatMessage.getSenderId());
//            List<User> users = chatService.chatRoomParticipateList(chatRoom);
//
//            /**
//             * 2024-06-24, SSE 비동기 처리로 채팅방 목록 업데이트
//             */
//            users.forEach(user ->
//                CompletableFuture.runAsync(() ->
//                    chatSseService.chatRoomListRefresh(user, "chatRoomListRefresh"))
//                .exceptionally(throwable -> {
//                    // 개발자 담당자한테 web hook 및 전달할 있게 처리하기.
//                    log.error("Exception occurred: " + throwable.getMessage());
//                    return null;
//                })
//            );
//        }
//
//        socketHandler.sendEmergencyMessage(chatMessage);
//    }
//
//}