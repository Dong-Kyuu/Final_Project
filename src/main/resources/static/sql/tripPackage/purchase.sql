-- 기존 테이블이 존재하면 삭제합니다.
DROP TABLE IF EXISTS purchase;

-- 새로운 purchase 테이블을 생성합니다.
CREATE TABLE purchase (
                          purchase_no VARCHAR(30) NOT NULL PRIMARY KEY,  -- 구매 번호, 기본 키
                          purchase_date DATE NOT NULL,  -- 구매 날짜
                          customer_no INT NOT NULL,  -- 고객 번호, 외래 키
                          cart_no VARCHAR(10) NOT NULL,  -- 장바구니 번호, 외래 키
                          FOREIGN KEY (customer_no) REFERENCES customer(customer_no),  -- customer 테이블 참조
                          FOREIGN KEY (cart_no) REFERENCES cart(cart_no)  -- cart 테이블 참조
);

-- 테이블 생성 후 전체 레코드를 조회합니다.
SELECT * FROM purchase;