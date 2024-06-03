drop table Trip;

CREATE TABLE Trip (
                      tripNo      VARCHAR(10) NOT NULL PRIMARY KEY,
                      tripName    VARCHAR(30) NOT NULL,
                      tripPrice   INT(10),
                      tripStock   INT(8),
                      tripMaxStock INT(8),
                      regDate     DATE,
                      expireDate  DATE,
                      tripDate    DATE,
                      fileId      VARCHAR(10), -- 상품번호와 동일하게
                      tripMainIMG VARCHAR(150),
                      tripCategory VARCHAR(15),
                      optionIds   VARCHAR(300),
                      FOREIGN KEY (fileId) REFERENCES TripFile(fileId)
);




select*from Trip;

insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(1,'서유럽15일',3000000,0,40,sysdate(),'2024-05-12','2024-06-12','1','/image/product/mainIMG/WEU1.png','WEU','010101,020101,030101,010102,020102');

insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(2,'서유럽15일',4000000,0,40,sysdate(),'2024-05-13','2024-06-13','2','/image/product/mainIMG/WEU2.png','WEU','010101,020101,030101,010102,020102');

insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(3,'서유럽15일',2000000,0,40,sysdate(),'2024-05-14','2024-06-14','3','/image/product/mainIMG/WEU3.png','WEU','010101,020101,030101,010102,020102');

insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(4,'서유럽15일',1900000,0,40,sysdate(),'2024-05-15','2024-06-15','4','/image/product/mainIMG/WEU4.png','WEU','010101,020101,030101,010102,020102');

insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(5,'서유럽15일',3000000,0,40,sysdate(),'2024-05-16','2024-06-16','5','/image/product/mainIMG/WEU5.png','WEU','010101,020101,030101,010102,020102');

insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(6,'서유럽15일',3500000,0,40,sysdate(),'2024-05-17','2024-06-17','6','/image/product/mainIMG/WEU6.png','WEU','010101,020101,030101,010102,020102');

insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(7,'서유럽15일',3200000,0,40,sysdate(),'2024-05-18','2024-06-18','7','/image/product/mainIMG/WEU7.png','WEU','010101,020101,030101,010102,020102');

insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(8,'서유럽15일',5000000,0,40,sysdate(),'2024-05-19','2024-06-19','8','/image/product/mainIMG/WEU8.png','WEU','010101,020101,030101,010102,020102');



insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(9,'센트럴유럽11일',3000000,0,40,sysdate(),'2024-05-13','2024-06-13','9','/image/product/mainIMG/CEU1.png','CEU','030101,010102,020102');

insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(10,'센트럴유럽11일',4000000,0,40,sysdate(),'2024-05-14','2024-06-14','10','/image/product/mainIMG/CEU2.png','CEU','030101,010102,020102');

insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(11,'센트럴유럽11일',2000000,0,40,sysdate(),'2024-05-15','2024-06-15','11','/image/product/mainIMG/CEU3.png','CEU','030101,010102,020102');

insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(12,'센트럴유럽11일',6000000,0,40,sysdate(),'2024-05-16','2024-06-16','12','/image/product/mainIMG/CEU4.png','CEU','030101,010102,020102');

insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(13,'센트럴유럽11일',3200000,0,40,sysdate(),'2024-05-17','2024-06-17','13','/image/product/mainIMG/CEU5.png','CEU','030101,010102,020102');



insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(14,'스위트유럽',4000000,0,40,sysdate(),'2024-05-13','2024-06-13','14','/image/product/mainIMG/SEU1.png','SEU','010101,020101,030101,010102,020102');

insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(15,'스위트유럽',4000000,0,40,sysdate(),'2024-05-14','2024-06-14','15','/image/product/mainIMG/SEU2.png','SEU','010101,020101,030101,010102,020102');

insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(16,'스위트유럽',4000000,0,40,sysdate(),'2024-05-15','2024-06-15','16','/image/product/mainIMG/SEU3.png','SEU','010101,020101,030101,010102,020102');

insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(17,'스위트유럽',4000000,0,40,sysdate(),'2024-05-16','2024-06-16','17','/image/product/mainIMG/SEU4.png','SEU','010101,020101,030101,010102,020102');

insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(18,'스위트유럽',4000000,0,40,sysdate(),'2024-05-17','2024-06-17','18','/image/product/mainIMG/SEU5.png','SEU','010101,020101,030101,010102,020102');

insert into Trip(tripNo,tripName,tripPrice,tripStock,tripMaxStock,regDate,expireDate,tripDate,fileId,tripMainIMG,tripCategory,optionIds) 
values(19,'스위트유럽',4000000,0,40,sysdate(),'2024-05-18','2024-06-18','19','/image/product/mainIMG/SEU6.png','SEU','010101,020101,030101,010102,020102');







SELECT * FROM (SELECT ROW_NUMBER() OVER (ORDER BY regDate ASC) AS rnum, 
	                 tripNo, tripPrice, tripStock, tripMaxStock, regDate, expireDate, tripDate,
	                 fileId, tripMainIMG, tripCategory FROM Trip) WHERE rnum BETWEEN 1 AND 9;
	                 
select count(*) from Trip;

DELETE FROM Trip;


SELECT * FROM (SELECT ROW_NUMBER() OVER (ORDER BY tripNo ASC) AS rnum, 
	                 tripNo, tripPrice, tripStock, tripMaxStock, regDate, expireDate, tripDate, 
	                 fileId, tripMainIMG, tripCategory FROM Trip) 
	                 WHERE tripCategory = 'WEU';

-- ---------------------

-- Drop the table if it already exists
DROP TABLE IF EXISTS trip;

-- Create the Trip table with proper naming conventions
CREATE TABLE trip (
                      trip_no         VARCHAR(10) NOT NULL PRIMARY KEY,
                      trip_name       VARCHAR(30) NOT NULL,
                      trip_price      INT(10),
                      trip_stock      INT(8),
                      trip_max_stock  INT(8),
                      reg_date        DATE,
                      expire_date     DATE,
                      trip_date       DATE,
                      file_id         VARCHAR(10), -- 상품번호와 동일하게
                      trip_main_img   VARCHAR(150),
                      trip_category   VARCHAR(15),
                      option_ids      VARCHAR(300),
                      FOREIGN KEY (file_id) REFERENCES trip_file(file_id)
);

-- Insert sample data into the Trip table
INSERT INTO trip(trip_no, trip_name, trip_price, trip_stock, trip_max_stock, reg_date, expire_date, trip_date, file_id, trip_main_img, trip_category, option_ids)
VALUES
    (1, '서유럽15일', 3000000, 0, 40, SYSDATE(), '2024-05-12', '2024-06-12', '1', '/image/product/mainIMG/WEU1.png', 'WEU', '010101,020101,030101,010102,020102'),
    (2, '서유럽15일', 4000000, 0, 40, SYSDATE(), '2024-05-13', '2024-06-13', '2', '/image/product/mainIMG/WEU2.png', 'WEU', '010101,020101,030101,010102,020102'),
    (3, '서유럽15일', 2000000, 0, 40, SYSDATE(), '2024-05-14', '2024-06-14', '3', '/image/product/mainIMG/WEU3.png', 'WEU', '010101,020101,030101,010102,020102'),
    (4, '서유럽15일', 1900000, 0, 40, SYSDATE(), '2024-05-15', '2024-06-15', '4', '/image/product/mainIMG/WEU4.png', 'WEU', '010101,020101,030101,010102,020102'),
    (5, '서유럽15일', 3000000, 0, 40, SYSDATE(), '2024-05-16', '2024-06-16', '5', '/image/product/mainIMG/WEU5.png', 'WEU', '010101,020101,030101,010102,020102'),
    (6, '서유럽15일', 3500000, 0, 40, SYSDATE(), '2024-05-17', '2024-06-17', '6', '/image/product/mainIMG/WEU6.png', 'WEU', '010101,020101,030101,010102,020102'),
    (7, '서유럽15일', 3200000, 0, 40, SYSDATE(), '2024-05-18', '2024-06-18', '7', '/image/product/mainIMG/WEU7.png', 'WEU', '010101,020101,030101,010102,020102'),
    (8, '서유럽15일', 5000000, 0, 40, SYSDATE(), '2024-05-19', '2024-06-19', '8', '/image/product/mainIMG/WEU8.png', 'WEU', '010101,020101,030101,010102,020102'),
    (9, '센트럴유럽11일', 3000000, 0, 40, SYSDATE(), '2024-05-13', '2024-06-13', '9', '/image/product/mainIMG/CEU1.png', 'CEU', '030101,010102,020102'),
    (10, '센트럴유럽11일', 4000000, 0, 40, SYSDATE(), '2024-05-14', '2024-06-14', '10', '/image/product/mainIMG/CEU2.png', 'CEU', '030101,010102,020102'),
    (11, '센트럴유럽11일', 2000000, 0, 40, SYSDATE(), '2024-05-15', '2024-06-15', '11','/image/product/mainIMG/CEU3.png', 'CEU', '030101,010102,020102'),
    (12, '센트럴유럽11일', 6000000, 0, 40, SYSDATE(), '2024-05-16', '2024-06-16', '12', '/image/product/mainIMG/CEU4.png', 'CEU', '030101,010102,020102'),
    (13, '센트럴유럽11일', 3200000, 0, 40, SYSDATE(), '2024-05-17', '2024-06-17', '13', '/image/product/mainIMG/CEU5.png', 'CEU', '030101,010102,020102'),
    (14, '스위트유럽', 4000000, 0, 40, SYSDATE(), '2024-05-13', '2024-06-13', '14', '/image/product/mainIMG/SEU1.png', 'SEU', '010101,020101,030101,010102,020102'),
    (15, '스위트유럽', 4000000, 0, 40, SYSDATE(), '2024-05-14', '2024-06-14', '15', '/image/product/mainIMG/SEU2.png', 'SEU', '010101,020101,030101,010102,020102'),
    (16, '스위트유럽', 4000000, 0, 40, SYSDATE(), '2024-05-15', '2024-06-15', '16', '/image/product/mainIMG/SEU3.png', 'SEU', '010101,020101,030101,010102,020102'),
    (17, '스위트유럽', 4000000, 0, 40, SYSDATE(), '2024-05-16', '2024-06-16', '17', '/image/product/mainIMG/SEU4.png', 'SEU', '010101,020101,030101,010102,020102'),
    (18, '스위트유럽', 4000000, 0, 40, SYSDATE(), '2024-05-17', '2024-06-17', '18', '/image/product/mainIMG/SEU5.png', 'SEU', '010101,020101,030101,010102,020102');

