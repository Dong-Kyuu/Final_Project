drop table MBTI;--MBTI테이블은 없애는게 좋을


CREATE TABLE MBTI (
                      MBTIId      VARCHAR(16) NOT NULL PRIMARY KEY,
                      countryNo   VARCHAR(10),
                      cityNo      VARCHAR(10),
                      FOREIGN KEY (countryNo) REFERENCES Country(countryNo),
                      FOREIGN KEY (cityNo) REFERENCES City(cityNo)
);


select * from mbti;

-- -----------------------
-- Drop the table if it already exists
DROP TABLE IF EXISTS mbti;

-- Create the mbti table with proper naming conventions
CREATE TABLE mbti (
                      mbti_id      VARCHAR(16) NOT NULL PRIMARY KEY,
                      country_no   VARCHAR(10),
                      city_no      VARCHAR(10),
                      FOREIGN KEY (country_no) REFERENCES country(country_no),
                      FOREIGN KEY (city_no) REFERENCES city(city_no)
);

-- Select all records from the mbti table
SELECT * FROM mbti;