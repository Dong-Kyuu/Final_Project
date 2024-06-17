-- 기존 테이블이 존재하면 삭제합니다.
DROP TABLE IF EXISTS customer;

-- 새 테이블을 생성합니다.
CREATE TABLE customer (
                          customer_no INT AUTO_INCREMENT PRIMARY KEY,  -- 고객 번호, 기본 키
                          customer_id VARCHAR(20) NOT NULL,  -- 고객 ID
                          customer_password VARCHAR(255) NOT NULL,  -- 고객 비밀번호
                          customer_name_kor VARCHAR(20) NOT NULL,  -- 고객 이름 (한글)
                          customer_name_eng VARCHAR(20)  NULL,  -- 고객 이름 (영어)
                          customer_gender VARCHAR(3) NOT NULL,  -- 고객 성별 ('남' 또는 '여')
                          customer_email VARCHAR(30) NOT NULL,  -- 고객 이메일
                          customer_phone VARCHAR(15) NOT NULL,  -- 고객 전화번호
                          customer_passport_no VARCHAR(20) NULL,  -- 고객 여권 번호
                          trip_no INT NULL, -- 구매한 여행상품
                          trip_group INT NULL -- 여행상품내 포함 그룹
);

-- 테이블 생성 후 전체 레코드를 조회합니다.
SELECT * FROM customer;

ALTER TABLE customer
    ADD COLUMN trip_no INT,
ADD COLUMN trip_group INT;

SELECT * FROM customer WHERE customer_id =1;