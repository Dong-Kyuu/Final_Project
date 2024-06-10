-- Drop the table if it already exists
DROP TABLE IF EXISTS country;

-- Create the country table with proper naming conventions
CREATE TABLE country (
                         country_no      VARCHAR(10) NOT NULL PRIMARY KEY,
                         country_name    VARCHAR(30) NOT NULL
);

-- Select all records from the country table
SELECT * FROM country;

-- Insert records into the country table
INSERT INTO country (country_no, country_name) VALUES ('01', '영국');
INSERT INTO country (country_no, country_name) VALUES ('02', '프랑스');
INSERT INTO country (country_no, country_name) VALUES ('03', '스위스');
INSERT INTO country (country_no, country_name) VALUES ('04', '이탈리아');
INSERT INTO country (country_no, country_name) VALUES ('05', '독일');
INSERT INTO country (country_no, country_name) VALUES ('06', '체코');
INSERT INTO country (country_no, country_name) VALUES ('07', '헝가리');
INSERT INTO country (country_no, country_name) VALUES ('08', '오스트리아');
INSERT INTO country (country_no, country_name) VALUES ('09', '슬로베니아');