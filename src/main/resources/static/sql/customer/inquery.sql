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
create table inquery_comment
(
    comm_num                 bigint auto_increment
        primary key,
    comm_id                  varchar(30)  null,
    comm_content             varchar(200) null,
    comm_reg_date            date         null,
    comm_board_num   bigint       null,
    comm_re_level    bigint       null,
    comm_re_sequence bigint       null,
    comm_re_referer  bigint       null,
    check (`comm_re_level` in (0, 1, 2))
);

