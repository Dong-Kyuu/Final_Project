drop table MBTI;--MBTI테이블은 없애는게 좋을


CREATE TABLE MBTI (
                      MBTIId      VARCHAR(16) NOT NULL PRIMARY KEY,
                      countryNo   VARCHAR(10),
                      cityNo      VARCHAR(10),
                      FOREIGN KEY (countryNo) REFERENCES Country(countryNo),
                      FOREIGN KEY (cityNo) REFERENCES City(cityNo)
);


select * from mbti;