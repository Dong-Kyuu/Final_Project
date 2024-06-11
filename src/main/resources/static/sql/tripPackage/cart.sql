DROP TABLE IF EXISTS cart;

CREATE TABLE cart (
                      cart_no          VARCHAR(10) NOT NULL PRIMARY KEY,
                      trip_no          VARCHAR(10),
                      option_ids       VARCHAR(300),
                      cart_total       INT(10),
                      cart_option_total INT(10),
                      FOREIGN KEY (trip_no) REFERENCES trip(trip_no)
);

-- Select all records from the cart table
SELECT * FROM cart;

-- Delete all records from the cart table
DELETE FROM cart;

-- Insert records into the cart table
INSERT INTO cart (cart_no, trip_no, option_ids, cart_total) VALUES ('0', NULL, NULL, NULL);
INSERT INTO cart (cart_no, trip_no, option_ids, cart_total) VALUES ('1', NULL, NULL, NULL);
INSERT INTO cart (cart_no, trip_no, option_ids, cart_total) VALUES ('2', NULL, NULL, NULL);

-- Count records in the cart table with cart_no '0'
SELECT COUNT(*) FROM cart WHERE cart_no = '0';

-- Select records from the cart table where cart_no is '0'
SELECT * FROM cart WHERE cart_no = '0';

-- Delete records from the cart table where cart_no is '0'
DELETE FROM cart WHERE cart_no = '0';