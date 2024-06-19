package com.example.jhta_3team_finalproject.handler.chat;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.domain.chat.ChatRoom;
import com.example.jhta_3team_finalproject.domain.chat.FileItemMultipartFile;
import com.example.jhta_3team_finalproject.domain.chat.ChatMessage;
import com.example.jhta_3team_finalproject.service.chat.ChatService;
import com.example.jhta_3team_finalproject.service.chat.ChatSseService;
import com.example.jhta_3team_finalproject.service.chat.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class SocketHandler extends TextWebSocketHandler {

    @Autowired
    ChatService chatService;

    @Autowired
    RedisService redisService;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired // aws img test
    AmazonS3Client amazonS3Client;

    @Autowired
    ChatSseService chatSseService;

    @Value("${temp.chat.savefolder}")
    String FILE_UPLOAD_PATH;

    List<HashMap<String, Object>> rls = new ArrayList<>(); // 웹소켓 세션을 담아둘 리스트 ---roomListSessions

    private String S3Bucket = "mybucketchatupload"; // Bucket 이름 aws img test
    static int fileUploadIdx = 0;
    static String fileUploadSession = "";
    List<ChatMessage> chatMessageList;
    String sessionId;
    String s3url;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:/static/upload/chat");
        FILE_UPLOAD_PATH = resource.getURI().getPath();

        // 메시지 발송
        String msg = message.getPayload(); // JSON형태의 String메시지를 받는다.
        JSONObject obj = jsonToObjectParser(msg); // JSON데이터를 JSONObject로 파싱한다.
        String roomNum = (String) obj.get("roomNumber"); // 방의 번호
        String content = (String) obj.get("msg"); // 메시지
        String chatUserId = (String) obj.get("chatUserId"); // 유저의 아이디
        String fileName = (String) obj.get("fileName"); // 파일의 원본 이름

        log.info("{}", roomNum);
        log.info(content);
        log.info(chatUserId);
        log.info(fileName);

        // 상태를 저장하기 위해 vo에 값을 넣어주고 insert
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomNum(Long.valueOf(roomNum));
        chatMessage.setSenderId(chatUserId);
        chatMessage.setMessageContent(content);
        chatMessage.setReadCount(1);
        /**
         * 2024-06-04, URL 을 변경해줄 때 업데이트 해주기 위해 임시 URL 지정
         */
        if(fileName != null && !fileName.equals("")) {
            Random random = new Random();
            s3url = String.valueOf(random.nextLong());
            chatMessage.setFileUrl(s3url);
        }
        chatMessage.setFileOriginName(fileName);

        chatMessage = chatService.createMessage(chatMessage);

        obj.put("sessionId", chatMessage.getSenderId());
        obj.put("readCount", chatMessage.getReadCount());
        obj.put("sendTime", chatMessage.getSendTime().getTime());
        obj.put("userName", chatMessage.getUsername());
        obj.put("userProfileImage", chatMessage.getUserProfilePicture());

        if(chatMessage != null) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setChatRoomNum(Long.valueOf(roomNum));
            chatRoom.setChatSessionId(chatUserId);
            List<User> users = chatService.chatRoomParticipateList(chatRoom);

            /**
             * 2024-06-14, SSE 비동기 처리로 채팅방 목록 업데이트
             */
            users.forEach(user ->
                CompletableFuture.runAsync(() ->
                chatSseService.chatRoomListRefresh(user, "chatRoomListRefresh"))
                .exceptionally(throwable -> {
                    // 개발자 담당자한테 web hook 및 전달할 있게 처리하기.
                    log.error("Exception occurred: " + throwable.getMessage());
                    return null;
                })
            );
        }

        HashMap<String, Object> temp = new HashMap<String, Object>();
        if (rls.size() > 0) {
            for (int i = 0; i < rls.size(); i++) {
                String roomNumber = (String) rls.get(i).get("roomNumber"); // 세션리스트의 저장된 방번호를 가져와서
                if (roomNumber.equals(roomNum)) { // 같은값의 방이 존재한다면
                    temp = rls.get(i); // 해당 방번호의 세션리스트의 존재하는 모든 object값을 가져온다.
                    break;
                }
            }

            // 해당 방의 세션들만 찾아서 메시지를 발송해준다.
            for (String k : temp.keySet()) {
                if (k.equals("roomNumber")) { // 다만 방번호일 경우에는 건너뛴다.
                    continue;
                }

                WebSocketSession wss = (WebSocketSession) temp.get(k);
                if (wss != null) {
                    try {
                        wss.sendMessage(new TextMessage(obj.toJSONString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 2024-06-04, 채팅 파일 업로드 -> S3 버킷 저장소 사용 구현
     */
    @Override
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) { // 바이너리 메시지 발송
        //바이너리 메시지 발송
        ByteBuffer byteBuffer = message.getPayload(); // (3)
        String fileName = "file.jpg";

        log.info("{} - FILE_UPLOAD_PATH", FILE_UPLOAD_PATH);

        File dir = new File("/home");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File Old_File = new File(FILE_UPLOAD_PATH + "/file.jpg"); // (4) 삭제할 파일을 찾아준다
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(Old_File);
            fileOutputStream.close(); // (5) 파일 삭제시 FileOutputStream을 닫아줘야함
            Old_File.delete(); // 삭제
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File(FILE_UPLOAD_PATH, fileName); // (6) 파일을 새로 생성해준다

        FileOutputStream fileOutputStream = null;
        FileChannel fileChannel = null;
        try {
            byteBuffer.flip(); // (7) byteBuffer를 읽기 위해 세팅
            fileOutputStream = new FileOutputStream(file, true); // (8) 생성을 위해 OutputStream을 연다.
            fileChannel = fileOutputStream.getChannel(); // (9) 채널을 열고
            byteBuffer.compact(); // (10) 파일을 복사한다.
            fileChannel.write(byteBuffer); // (11) 파일을 쓴다.
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (fileChannel != null) {
                    fileChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String imageurl = "";
        ChatMessage chatMessage = new ChatMessage();
        try {
            FileItem fileItem = new DiskFileItem("mainFile", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile()); // (12) File을 MultiPartFile로 변환하는 과정 우선 file을 DiskFileItem에 넣어준다

            try {
                InputStream input = new FileInputStream(file); // (13) 파일에서 바이트 파일로 읽을 수 있게 해줌
                OutputStream os = fileItem.getOutputStream(); // (14) OutputStream을 생성해준다
                IOUtils.copy(input, os); // (15) 복사
                // Or faster..
                // IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
            } catch (IOException ex) {
                // do something.
            }

            MultipartFile multipartFile = new FileItemMultipartFile(fileItem); // (16) File을 MultipartFile로 변환시키기

            String originalName = UUID.randomUUID().toString(); // aws s3 저장과정
            long multipartFileSize = multipartFile.getSize(); // aws s3 저장과정

            ObjectMetadata objectMetaData = new ObjectMetadata(); // aws s3 저장과정
            objectMetaData.setContentType(multipartFile.getContentType()); // aws s3 저장과정
            objectMetaData.setContentLength(multipartFileSize); // aws s3 저장과정

            amazonS3Client.putObject(new PutObjectRequest(S3Bucket, originalName, multipartFile.getInputStream(), objectMetaData).withCannedAcl(CannedAccessControlList.PublicRead)); // aws s3 저장과정

            /**
             * 2024-06-04 임시 URL을 S3 버킷이 관리하는 파일 URL로 변경
             */
            imageurl = amazonS3Client.getUrl(S3Bucket, originalName).toString(); // aws s3 저장된 이미지 불러오기
            log.info("imageurl : {}", imageurl);
            chatMessage.setFileUrl(s3url);
            chatMessage.setS3url(imageurl);

            chatMessage = chatService.updateMsgImageUrl(chatMessage);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        byteBuffer.position(0); // (17) 파일을 저장하면서 position값이 변경되었으므로 0으로 초기화한다.
        //파일쓰기가 끝나면 이미지를 발송한다.
        HashMap<String, Object> temp = rls.get(fileUploadIdx);
        for (String k : temp.keySet()) {
            if (k.equals("roomNumber")) {
                continue;
            }
            WebSocketSession webSocketSession = (WebSocketSession) temp.get(k);
            try {
                JSONObject obj = new JSONObject();
                obj.put("type", "imgurl");
                obj.put("sessionId", chatMessage.getSenderId());
                obj.put("fileUrl", imageurl);
                obj.put("fileOriginName", chatMessage.getFileOriginName());
                obj.put("msg", chatMessage.getMessageContent());
                obj.put("sendTime", chatMessage.getSendTime().getTime());
                obj.put("readCount", chatMessage.getReadCount());
                obj.put("userName", chatMessage.getUsername());
                obj.put("userProfileImage", chatMessage.getUserProfilePicture());
                webSocketSession.sendMessage(new TextMessage(obj.toJSONString())); //초기화된 버퍼를 발송한다.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("소켓 설립");
        // 소켓 연결
        super.afterConnectionEstablished(session);
        boolean flag = false;
        String url = session.getUri().toString();
        String buffer = url.split("/chatting/")[1];
        String roomNumber = buffer.split("&")[0];
        sessionId = buffer.split("&")[1];
        String type = buffer.split("&")[2];

        log.info("{} : 방번호 ", roomNumber);
        log.info("{} : 유저아이디", sessionId);

        // 방번호를 기준으로 다 받아온다.
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomNum(Long.valueOf(roomNumber));
        log.info("{}", chatMessage);
        log.info("{}", chatMessage.getChatRoomNum());
        chatMessageList = redisService.getRedisChatMessage(chatMessage);
        //chatMessageList = chattingService.searchMessages(chatMessage);

        log.info("{} : 입니다.", chatMessageList);

        int idx = rls.size(); // 방의 사이즈를 조사한다.
        if (rls.size() > 0) {
            for (int i = 0; i < rls.size(); i++) {
                String rN = (String) rls.get(i).get("roomNumber");
                if (rN.equals(roomNumber)) {
                    flag = true;
                    idx = i;
                    break;
                }
            }
        }

        if (flag) { // 존재하는 방이라면 세션만 추가한다.
            HashMap<String, Object> map = rls.get(idx);
            map.put(session.getId(), session);
        } else { // 최초 생성하는 방이라면 방번호와 세션을 추가한다.
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("roomNumber", roomNumber);
            map.put(session.getId(), session);
            rls.add(map);
        }

        // 포문으로 연속 메시지를 보낸다. list 크기 만큼 돌린다.
        for (int i = 0; i < chatMessageList.size(); i++) {
            String content = chatMessageList.get(i).getMessageContent();
            String senderId = chatMessageList.get(i).getSenderId(); // 2024-06-08, 현재는 아이디 -> 나중에 이름으로 가져올 예정
            String fileUrl = chatMessageList.get(i).getFileUrl();
            int readCount = chatMessageList.get(i).getReadCount();
            Date sendTime = chatMessageList.get(i).getSendTime();
            String userName = chatMessageList.get(i).getUsername();
            String userProfileImage = chatMessageList.get(i).getUserProfilePicture();

            log.info("{} 번째", i);
            // 세션등록이 끝나면 발급받은 세션 ID 값의 메시지를 발송한다.
            JSONObject obj = new JSONObject();
            log.info(session.getId());
            log.info("{}", session);
            obj.put("type", "getId");
            //obj.put(session.getId(),session); // 활성화하면 새로고침 시 소켓이 종료되면서 같은 세션 값을 가지고 있던 obj들이 제거되었었음.
            obj.put("sessionId", senderId); // 유저 아이디임. // 위를 활성화하면 세션과 관련된 obj들이 제거되면서 이 컬럼과 js 조건문이 만나는 조건에서 의도치 않은 결과가 나왔었음.
            obj.put("msg", content);
            obj.put("readCount", readCount);
            obj.put("fileUrl", fileUrl);
            obj.put("fileOriginName", chatMessage.getFileOriginName());
            obj.put("sendTime", sendTime.getTime()); // milliseconds 밀리초로 구해진 값으로 JS와 호환
            obj.put("userName", userName);
            obj.put("userProfileImage", userProfileImage);

            /**
             * 2024-06-12, 타임스탬프인 경우 put 한다.
             */
            if(chatMessageList.get(i).getType() != null &&
               chatMessageList.get(i).getType().equals(ChatMessage.MessageType.TIMESTAMP)) {
               obj.put("timeStamp", chatMessageList.get(i).getTimeStamp());
            }

            session.sendMessage(new TextMessage(obj.toJSONString()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("소켓종료");
        // 소켓 종료
        if (rls.size() > 0) { // 소켓이 종료되면 해당 세션값들을 찾아서 지운다.
            for (int i = 0; i < rls.size(); i++) {
                //rls.get(i).remove(session.getId()); // 세션값과 관련된 obj들을 제거함.
                rls.get(i).remove(session.getId());
                // 만약 위에서 세션값을 설정 안해준다면 기존 세션값을 지우고 새 소켓을 만들 시 새로운 세션으로 시작.
                // obj.put(session.getId(),session);의 session.getId() 속성값을
                // map의 session.getId() 속성값과  비교하였을때 서로 일치하는 세션값을 가진 obj를 제거함.
            }
        }
        log.info("{}", rls);
        super.afterConnectionClosed(session, status);
    }

    private static JSONObject jsonToObjectParser(String jsonStr) {
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(jsonStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return obj;
    }
}