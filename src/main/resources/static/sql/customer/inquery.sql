-- auto-generated definition
create table inquery_board
(
    inq_num       bigint auto_increment
        primary key,
    inq_name      varchar(30)                        null,
    inq_pass      varchar(30)                        null,
    inq_subject   varchar(300)                       null,
    inq_content   mediumtext                         null,
    inq_file      varchar(50)                        null,
    inq_original  varchar(300)                       null,
    inq_re_ref    int                                null,
    inq_re_lev    int                                null,
    inq_re_seq    int                                null,
    inq_readcount int                                null,
    inq_date      datetime default CURRENT_TIMESTAMP null
);

-- auto-generated definition
create table inquery_comm
(
    num                 bigint auto_increment
        primary key,
    id                  varchar(30)  null,
    content             varchar(200) null,
    reg_date            date         null,
    comment_board_num   bigint       null,
    comment_re_level    bigint       null,
    comment_re_sequence bigint       null,
    comment_re_referer  bigint       null,
    check (`comment_re_level` in (0, 1, 2))
);

