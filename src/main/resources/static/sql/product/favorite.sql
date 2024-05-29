drop table favorite;

CREATE TABLE Favorite (
                          favorId       VARCHAR(10) NOT NULL PRIMARY KEY,
                          tripNo        VARCHAR(10),
                          optionId      VARCHAR(10),
                          FOREIGN KEY (tripNo) REFERENCES Trip(tripNo),
                          FOREIGN KEY (optionId) REFERENCES TripOption(optionId)
);
