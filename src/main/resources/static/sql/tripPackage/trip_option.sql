-- Drop the table if it already exists
DROP TABLE IF EXISTS trip_option;

-- Create the TripOption table with proper naming conventions
CREATE TABLE trip_option (
                             option_id        VARCHAR(10) NOT NULL PRIMARY KEY,
                             option_name      VARCHAR(30) NOT NULL,
                             option_price     INT(10),
                             option_stock     INT(8),
                             option_max_stock INT(8),
                             option_date      DATE,
                             city_no          VARCHAR(10),
                             file_id          VARCHAR(255) null,
                             option_main_img  VARCHAR(150),
                                 FOREIGN KEY (city_no) REFERENCES city(city_no),
                             FOREIGN KEY (file_id) REFERENCES tripfile(file_id)
);

-- Insert sample data into the TripOption table
INSERT INTO trip_option(option_id, option_name, option_price, option_stock, option_max_stock, option_date, city_no, file_id, option_main_img)
VALUES
    ('010101', '런던해리포터투어', 30000, 0, 10, CURDATE(), '0101', NULL, '/image/product/optionIMG/해리포터.png'),
    ('020101', '런던빅벤투어', 20000, 0, 10, CURDATE(), '0101', NULL, '/image/product/optionIMG/빅벤.png'),
    ('030101', '런던토트넘투어', 70000, 0, 10, CURDATE(), '0101', NULL, '/image/product/optionIMG/토트넘.png'),
    ('010102', '파리디즈니투어', 90000, 0, 10, CURDATE(), '0102', NULL, '/image/product/optionIMG/디즈니.png'),
    ('020102', '파리루브르투어', 60000, 0, 10, CURDATE(), '0102', NULL, '/image/product/optionIMG/루브르.png'),
    ('030102', '파리몽쉘미셸투어', 80000, 0, 10, CURDATE(), '0102', NULL, '/image/product/optionIMG/몽쉘.png'),
    ('010202', '스트라스부르역사투어', 30000, 0, 10, CURDATE(), '0202', NULL, '/image/product/optionIMG/역사투어.png'),
    ('010103', '스위스융프라우호투어', 55000, 0, 10, CURDATE(), '0103', NULL, '/image/product/optionIMG/융프라우호.png'),
    ('020103', '스위스페러글라이딩', 120000, 0, 10, CURDATE(), '0103', NULL, '/image/product/optionIMG/페러글라이딩.png');

-- Delete all records from the TripOption table
DELETE FROM trip_option;

-- Select all records from the TripOption table
SELECT * FROM trip_option;