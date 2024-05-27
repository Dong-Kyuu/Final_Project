-- 순서대로 삭제
# DROP TABLE com_message;
# DROP TABLE com_chat_participate;
# DROP TABLE com_chat_room;
# DROP TABLE member;

-- 채팅방
-- auto-generated definition
create table com_chat_room
(
    chat_room_num    bigint auto_increment
        primary key,
    chat_session_id  varchar(50)                        not null,
    room_name        varchar(50)                        not null,
    room_create_date datetime default CURRENT_TIMESTAMP not null
);


-- 메시지
create table com_message
(
    message_num     bigint auto_increment
        primary key,
    message_content varchar(300)                       not null,
    read_count      int                                not null,
    send_time       datetime default CURRENT_TIMESTAMP not null,
    sender_id       varchar(50)                        not null,
    chat_room_num   bigint                             not null,
    constraint FK_CHAT_MESSAGE_NUM
        foreign key (chat_room_num) references com_chat_room (chat_room_num),
    constraint FK_SENDER_MESSAGE_NUM
        foreign key (sender_id) references users (id)
);


-- 채팅 참여
create table com_chat_participate
(
    chat_user_id    varchar(50)                        not null,
    chat_room_num   bigint                             not null,
    chat_entry_time datetime default CURRENT_TIMESTAMP not null,
    chat_visit_time datetime                           not null,
    constraint FK_CHAT_PART_NUM
        foreign key (chat_room_num) references com_chat_room (chat_room_num),
    constraint FK_SENDER_PART_NUM
        foreign key (chat_user_id) references users (id)
);


-- 시퀀스 대신 사용
#<selectKey resultType="int" order="BEFORE" keyProperty="chatroom_num">
#    select nvl(max(chatroom_num),0)+1 from com_chat_room
#</selectKey>