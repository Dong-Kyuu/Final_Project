drop table Cart;

CREATE TABLE Cart (
                      cartNo           VARCHAR(10) NOT NULL PRIMARY KEY,
                      tripNo           VARCHAR(10),
                      optionIds        VARCHAR(300),
                      cartTotal        INT(10),
                      cartOptionTotal  INT(10),
                      FOREIGN KEY (tripNo) REFERENCES Trip(tripNo)
);


select * from Cart;

delete from Cart;

INSERT INTO Cart (cartNo, tripNo, optionIds, cartTotal) VALUES ('0', null, null, null);
INSERT INTO Cart (cartNo, tripNo, optionIds, cartTotal) VALUES ('1', null, null, null);
INSERT INTO Cart (cartNo, tripNo, optionIds, cartTotal) VALUES ('2', null, null, null);

select count(*) from cart where cartNo = '0';
SELECT * FROM Cart WHERE cartNo = '0';

DELETE FROM Cart WHERE cartNo = '0';