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