drop table Purchase;

CREATE TABLE Purchase (
                          purchaseNo VARCHAR(30) NOT NULL PRIMARY KEY,
                          cartNo VARCHAR(10) NOT NULL,
                          member_id VARCHAR(20) NOT NULL,
                          purchaseDate DATE NOT NULL,
                          customerFirstNameKor VARCHAR(10) NOT NULL,
                          customerLastNameKor VARCHAR(10),
                          customerFirstNameEng VARCHAR(10) NOT NULL,
                          customerLastNameEng VARCHAR(10),
                          customerPassportNo VARCHAR(20) NOT NULL,
                          customerGender VARCHAR(3) NOT NULL, -- '남' 또는 '여'
                          customerPhone VARCHAR(15) NOT NULL,
                          customerEmail VARCHAR(30) NOT NULL,
                          FOREIGN KEY (cartNo) REFERENCES Cart(cartNo),
                          FOREIGN KEY (member_id) REFERENCES Member(member_id)
);

select* from Purchase;