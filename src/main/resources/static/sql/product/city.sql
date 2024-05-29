drop table City;

CREATE TABLE City (
                      cityNo        VARCHAR(10) NOT NULL PRIMARY KEY,
                      cityName      VARCHAR(30) NOT NULL,
                      countryNo     VARCHAR(10),
                      MBTIBack      VARCHAR(8),
                      FOREIGN KEY (countryNo) REFERENCES Country(countryNo)
);


select * from city;

insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0101','런던','01','TJ');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0102','파리','02','FJ');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0202','스트라스부르','02','FP');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0302','콜마르','02','TJ');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0402','뮐루즈','02','TP');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0103','인터라켄','03','FP');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0203','루체른','03','TJ');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0104','밀라노','04','FJ');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0204','베네치아','04','FP');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0304','피렌체','04','TP');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0404','로마','04','TJ');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0105','뮌헨','05','TP');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0205','뉘른베르크','05','FJ');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0106','프라하','06','FJ');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0206','체스키 크롬로프','06','FP');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0107','부다페스트','07','TP');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0108','잘츠부르크','08','TP');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0208','비엔나','08','TJ');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0308','할슈타트','08','FP');
insert into City (cityNo,cityName, countryNo, MBTIBack) values ('0109','류블라냐','09','FP');