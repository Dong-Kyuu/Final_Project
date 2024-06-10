-- Drop the table if it already exists
DROP TABLE IF EXISTS city;

-- Create the city table with proper naming conventions
CREATE TABLE city (
                      city_no         VARCHAR(10) NOT NULL PRIMARY KEY,
                      city_name       VARCHAR(30) NOT NULL,
                      country_no      VARCHAR(10),
                      FOREIGN KEY (country_no) REFERENCES country(country_no)
);

-- Select all records from the city table
SELECT * FROM city;

-- Insert records into the city table
INSERT INTO city (city_no, city_name, country_no) VALUES ('0101', '런던', '01');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0102', '파리', '02');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0202', '스트라스부르', '02');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0302', '콜마르', '02');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0402', '뮐루즈', '02');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0103', '인터라켄', '03');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0203', '루체른', '03');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0104', '밀라노', '04');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0204', '베네치아', '04');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0304', '피렌체', '04');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0404', '로마', '04');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0105', '뮌헨', '05');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0205', '뉘른베르크', '05');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0106', '프라하', '06');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0206', '체스키 크롬로프', '06');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0107', '부다페스트', '07');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0108', '잘츠부르크', '08');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0208', '비엔나', '08');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0308', '할슈타트', '08');
INSERT INTO city (city_no, city_name, country_no) VALUES ('0109', '류블라냐', '09');