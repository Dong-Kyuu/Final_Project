drop table favorite;

CREATE TABLE Favorite (
                          favorId       VARCHAR(10) NOT NULL PRIMARY KEY,
                          tripNo        VARCHAR(10),
                          optionId      VARCHAR(10),
                          FOREIGN KEY (tripNo) REFERENCES Trip(tripNo),
                          FOREIGN KEY (optionId) REFERENCES TripOption(optionId)
);


-- --------------

-- Drop the table if it already exists
DROP TABLE IF EXISTS favorite;

-- Create the favorite table with proper naming conventions
CREATE TABLE favorite (
                          favor_id   VARCHAR(10) NOT NULL PRIMARY KEY,
                          trip_no    VARCHAR(10),
                          option_id  VARCHAR(10),
                          FOREIGN KEY (trip_no) REFERENCES trip(trip_no),
                          FOREIGN KEY (option_id) REFERENCES trip_option(option_id)
);
