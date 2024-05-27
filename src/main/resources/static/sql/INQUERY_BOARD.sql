drop table inquery_board cascade constraints purge;

create table inquery_board (
	inq_num		   NUMBER, 	  	-- 글 번호
	inq_name	   VARCHAR2(30), 	-- 작성자
	inq_pass	   VARCHAR2(30), 	-- 비밀번호
	inq_subject	   VARCHAR2(300), 	-- 제목
	inq_content	   LONG, -- 내용
	inq_file	   VARCHAR2(50),	-- 첨부될 파일
    inq_original   VARCHAR2(300),
	inq_re_ref	   NUMBER,			-- 답변 글 작성시 참조
	inq_re_lev	   NUMBER,			-- 답변 글의 깊이
	inq_re_seq	   NUMBER,			-- 답변 글의 순서
	inq_readcount  NUMBER,			-- 글의 조회수
	inq_date DATE default sysdate, 
	PRIMARY KEY(inq_num)
);

select * from inquery_board;

-- 인덱스 생성 (이미 생성되어 있음)
CREATE INDEX idx_inq_num on inquery_board(inq_num);

-- 둘이 동시에 실행 (no index)
EXPLAIN plan FOR
select * 
from inquery_board; -- 확인 할 쿼리를 plan table로 저장
SELECT * FROM TABLE(dbms_xplan.display); -- Plan table 출력 

-- 둘이 동시에 실행 (inq_num index)
EXPLAIN plan FOR
select * /*+ INDEX(inq SYS_C007796) */
from inquery_board inq; -- 확인 할 쿼리를 plan table로 저장
SELECT * FROM TABLE(dbms_xplan.display); -- Plan table 출력 

-- 인덱스 조회
SELECT * FROM USER_INDEXES WHERE TABLE_NAME = 'INQUERY_BOARD';

-- 키(제약조건) 조회
SELECT * FROM USER_CONSTRAINTS WHERE TABLE_NAME = 'INQUERY_BOARD';

-- 인덱스 조회 - 중요 컬럼
SELECT *
FROM ALL_IND_COLUMNS
WHERE TABLE_NAME='INQUERY_BOARD'
ORDER BY INDEX_NAME, COLUMN_POSITION;

-- 1에서 100 개의 게시물 작성
BEGIN
FOR i IN 1..100 LOOP
    IF (mod(i,2)=0) THEN
        INSERT INTO boot3.INQUERY_BOARD(INQ_NUM, INQ_NAME, INQ_PASS, INQ_SUBJECT, INQ_CONTENT, INQ_FILE, INQ_ORIGINAL, INQ_RE_REF, INQ_RE_LEV, INQ_RE_SEQ, INQ_READCOUNT)
        VALUES(i, 'admin', 1234, CONCAT('1:1 문의 테스트 게시물',i), CONCAT('1:1 문의 테스트 게시물 내용',i), '', '', i, 0, 0, 0);
    ELSE
        INSERT INTO boot3.INQUERY_BOARD(INQ_NUM, INQ_NAME, INQ_PASS, INQ_SUBJECT, INQ_CONTENT, INQ_FILE, INQ_ORIGINAL, INQ_RE_REF, INQ_RE_LEV, INQ_RE_SEQ, INQ_READCOUNT)
        VALUES(i, 'admin', null, CONCAT('1:1 문의 테스트 게시물',i), CONCAT('1:1 문의 테스트 게시물 내용',i), '', '', i, 0, 0, 0);
    END IF;
END LOOP;
END;
/

select * from boot3.INQUERY_BOARD;



