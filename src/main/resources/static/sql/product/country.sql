drop table Country;

CREATE TABLE Country (
                         countryNo      VARCHAR(10) NOT NULL PRIMARY KEY,
                         countryName    VARCHAR(30) NOT NULL,
                         MBTIFront      VARCHAR(8)
);




select * from Country;

insert into Country (countryNo,countryName,MBTIFront) values ('01','영국','IS');
insert into Country (countryNo,countryName,MBTIFront) values ('02','프랑스','EN');
insert into Country (countryNo,countryName,MBTIFront) values ('03','스위스','ES');
insert into Country (countryNo,countryName,MBTIFront) values ('04','이탈리아','ES');
insert into Country (countryNo,countryName,MBTIFront) values ('05','독일','IS');
insert into Country (countryNo,countryName,MBTIFront) values ('06','체코','IN');
insert into Country (countryNo,countryName,MBTIFront) values ('07','헝가리','EN');
insert into Country (countryNo,countryName,MBTIFront) values ('08','오스트리아','IN');
insert into Country (countryNo,countryName,MBTIFront) values ('09','슬로베니아','IS');

-- ----------------------

-- Drop the table if it already exists
DROP TABLE IF EXISTS country;

-- Create the country table with proper naming conventions
CREATE TABLE country (
                         country_no      VARCHAR(10) NOT NULL PRIMARY KEY,
                         country_name    VARCHAR(30) NOT NULL,
                         mbti_front      VARCHAR(8)
);

-- Select all records from the country table
SELECT * FROM country;

-- Insert records into the country table
INSERT INTO country (country_no, country_name, mbti_front) VALUES ('01', '영국', 'IS');
INSERT INTO country (country_no, country_name, mbti_front) VALUES ('02', '프랑스', 'EN');
INSERT INTO country (country_no, country_name, mbti_front) VALUES ('03', '스위스', 'ES');
INSERT INTO country (country_no, country_name, mbti_front) VALUES ('04', '이탈리아', 'ES');
INSERT INTO country (country_no, country_name, mbti_front) VALUES ('05', '독일', 'IS');
INSERT INTO country (country_no, country_name, mbti_front) VALUES ('06', '체코', 'IN');
INSERT INTO country (country_no, country_name, mbti_front) VALUES ('07', '헝가리', 'EN');
INSERT INTO country (country_no, country_name, mbti_front) VALUES ('08', '오스트리아', 'IN');
INSERT INTO country (country_no, country_name, mbti_front) VALUES ('09', '슬로베니아', 'IS');
