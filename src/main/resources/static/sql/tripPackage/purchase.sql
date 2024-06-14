-- 기존 테이블이 존재하면 삭제합니다.
DROP TABLE IF EXISTS purchase;

CREATE TABLE purchase (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          imp_uid VARCHAR(255) UNIQUE NOT NULL,
                          merchant_uid VARCHAR(255) NOT NULL,
                          buyer_no INT,
                          amount DECIMAL(10, 2),
                          status VARCHAR(50) DEFAULT 'PENDING',
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          trip_no INT, -- trip_no 열 추가
                          CONSTRAINT fk_buyer_no FOREIGN KEY (buyer_no) REFERENCES customer (customer_no),
                          CONSTRAINT fk_trip_no FOREIGN KEY (trip_no) REFERENCES trip (trip_no) -- trip 테이블의 기본키를 외래키로 설정
);
-- 테이블 생성 후 전체 레코드를 조회합니다.
SELECT * FROM purchase;