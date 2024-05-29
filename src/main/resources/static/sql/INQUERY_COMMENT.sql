drop table inquery_comm;
#
cascade constraints purge;

create table inquery_comm
(
    num               bigint auto_increment
        primary key,
    id                varchar(30) null,
    content           varchar(200) null,
    reg_date          datetime null,
    comment_board_num int null,
    comment_re_lev    int null,
    comment_re_seq    int null,
    comment_re_ref    int null,
    check (`comment_re_lev` in (0, 1, 2))
);

-- 게시판 글이 삭제되면 참조하는 댓글도 삭제됩니다.

#
drop sequence comm_seq;
#
# -- 시퀀스를 생성합니다.
#
create sequence comm_seq;
#
#
delete
from inquery_comm;
#
delete
from inquery_board;
#
#
select *
from comm;
#
# insert into board (BOARD_NUM, BOARD_SUBJECT, BOARD_NAME, BOARD_RE_REF)
# values(1, '처음', 'admin', 1);
#
insert into board (BOARD_NUM, BOARD_SUBJECT, BOARD_NAME, BOARD_RE_REF)
# values(2, '둘째', 'admin', 2);
#
insert into board (BOARD_NUM, BOARD_SUBJECT, BOARD_NAME, BOARD_RE_REF)
# values(3, '셋째', 'admin', 3);
#
# insert into comm (num, id, comment_board_num) values(1,'admin',1);
#
insert into comm (num, id, comment_board_num) values(2,'admin',1);
#
insert into comm (num, id, comment_board_num) values(3,'admin',2);
#
insert into comm (num, id, comment_board_num) values(4,'admin',2);
#
#
update board
    #
set board_subject = '오늘도 행복하세요' #
where board_num=1;
#
# -- 1. comm 테이블에서 comment_board_num 별 갯수를 구합니다.
# COMMENT_BOARD_NUM CNT
# 1				  2
# 2				  2
#
#
select comment_board_num,
       count(*) cnt #
from comm #
group by comment_board_num;
#
# -- 2. board와 조인을 합니다.
# BOARD_NUM	BOARD_SUBJECT	CNT
# 1			오늘도 행복하세요		2
# 2			둘째				2
#
#
select board_num,
       board_subject,
       cnt
           #
from board
         join (select comment_board_num,
                      count(*) cnt #
               from comm #
               group by comment_board_num) # on board_num = comment_board_num;
#
# BOARD_NUM	BOARD_SUBJECT	CNT
# 1			오늘도 행복하세요		2
# 2			둘째				2
# 3			세째				NULL
#
# -- 2단계) cnt가 null인 경우 0으로 만들어요
#
select board_num,
       board_subject,
       nvl(cnt, 0) as cnt #
from board
         left outer join (select comment_board_num,
                                 count(*) cnt #
                          from comm #
                          group by comment_board_num) # on board_num = comment_board_num #
order by BOARD_RE_REF desc,
    # BOARD_RE_SEQ asc;
#
# -- 1. member에 있는 memberfile이 필요합니다.
# -- 2. order by comment_re_ref asc, comment_re_seq asc (등록순)
# -- 3. order by comment_re_ref desc, comment_re_seq asc (최신순)
#
select comm.*,
       member.memberfile
           #
from comm
         inner join member # on comm.id = member.id #
where comment_board_num = 3
    #
order by comment_re_ref asc,
    # comment_re_seq asc;
	


