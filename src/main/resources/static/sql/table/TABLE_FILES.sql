CREATE TABLE TABLE_FILES
(
    board_num          INT,
    file_name          VARCHAR(255) NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    upload_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (board_num) REFERENCES board (BOARD_NUM) ON DELETE CASCADE
);