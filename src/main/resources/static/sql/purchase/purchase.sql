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

-- --------------------

drop table if exists purchase;

create table purchase (
                          purchase_no varchar(30) not null primary key,
                          cart_no varchar(10) not null,
                          member_id varchar(20) not null,
                          purchase_date date not null,
                          customer_first_name_kor varchar(10) not null,
                          customer_last_name_kor varchar(10),
                          customer_first_name_eng varchar(10) not null,
                          customer_last_name_eng varchar(10),
                          customer_passport_no varchar(20) not null,
                          customer_gender varchar(3) not null, -- '남' 또는 '여'
                          customer_phone varchar(15) not null,
                          customer_email varchar(30) not null,
                          foreign key (cart_no) references cart(cart_no),
                          foreign key (member_id) references member(member_id)
);

select * from purchase;
