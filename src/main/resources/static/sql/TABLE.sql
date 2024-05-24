select count(*) from board

select * from board

drop table comments cascade constraints purge;
CREATE TABLE comments (
                          num INT PRIMARY KEY AUTO_INCREMENT,
                          id VARCHAR(30),
                          content VARCHAR(200),
                          reg_date DATE,
                          board_num INT,
                          FOREIGN KEY (board_num) REFERENCES board(BOARD_NUM) ON DELETE CASCADE
);

drop sequence com_seq;
create sequence com_seq;

select * from comments;


insert into board(BOARD_NUM, BOARD_NAME, BOARD_PASS, BOARD_SUBJECT, BOARD_CONTENT, BOARD_RE_REF, BOARD_RE_LEV, BOARD_RE_SEQ, BOARD_READCOUNT)
values (1, "김동규", "1234", "첫번째 글입니다 ㅎㅇㅎㅇ", "그냥 뻘글입니다 지나가세요", 1, 0, 0, 0)


SELECT b.*, COALESCE(comm.cnt, 0) AS cnt
FROM board b LEFT JOIN (SELECT board_num, COUNT(*) AS cnt
                        FROM comments
                        GROUP BY board_num) comm
                       ON comm.board_num = b.board_num
ORDER BY b.BOARD_RE_REF DESC, b.BOARD_RE_SEQ ASC
    LIMIT 1, 10