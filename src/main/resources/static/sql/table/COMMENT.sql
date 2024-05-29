CREATE TABLE table_comments
(
    num       INT PRIMARY KEY AUTO_INCREMENT,
    id        VARCHAR(30),
    content   VARCHAR(200),
    reg_date  DATE,
    board_num INT,
    FOREIGN KEY (board_num) REFERENCES board (BOARD_NUM) ON DELETE CASCADE
);

drop sequence com_seq;
create sequence com_seq;

select *
from mbt.TABLE_COMENTS