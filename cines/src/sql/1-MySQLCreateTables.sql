-- Indexes for primary keys have been explicitly created.

-- ---------- Table for validation queries from the connection pool. ----------

DROP TABLE IF EXISTS PingTable;
CREATE TABLE PingTable (foo CHAR(1));

-- ------------------------------ Drop Tables ----------------------------------

DROP TABLE IF EXISTS Buy;
DROP TABLE IF EXISTS SessionMovie;
DROP TABLE IF EXISTS Movie;
DROP TABLE IF EXISTS Room;
DROP TABLE IF EXISTS Cinema;
DROP TABLE IF EXISTS UserProfile;
DROP TABLE IF EXISTS Province;

-- ------------------------------ UserProfile ----------------------------------

CREATE TABLE UserProfile (
	usrId BIGINT NOT NULL AUTO_INCREMENT,
	loginName VARCHAR(30) COLLATE latin1_bin NOT NULL,
	dni VARCHAR(9) NOT NULL, 
	enPassword VARCHAR(13) NOT NULL, 
	firstName VARCHAR(30) NOT NULL,
	lastName VARCHAR(40) NOT NULL, 
	email VARCHAR(60) NOT NULL,
	role VARCHAR(30),
	CONSTRAINT UserProfile_PK PRIMARY KEY (usrId),
	CONSTRAINT LoginName_UniqueKey UNIQUE (loginName),
	CONSTRAINT Dni_UniqueKey UNIQUE (dni))
	ENGINE = InnoDB;

CREATE INDEX UserProfileIndexByLoginName ON UserProfile (loginName);


-- ------------------------------ Province ----------------------------------

CREATE TABLE Province (
	provinceId BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(30) NOT NULL,
	CONSTRAINT Province_PK PRIMARY KEY (provinceId),
	CONSTRAINT ProvinceName_UniqueKey UNIQUE (name)) 
	ENGINE = InnoDB;

-- ------------------------------ Cinema ----------------------------------

CREATE TABLE Cinema (
	cinemaId BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(40) NOT NULL,
	price DOUBLE PRECISION NOT NULL,
	provinceId BIGINT NOT NULL,
	CONSTRAINT Cinema_PK PRIMARY KEY (cinemaId),
	CONSTRAINT CinemaName_UniqueKey UNIQUE (name),
	CONSTRAINT CinemaProvince_FK FOREIGN KEY (provinceId) REFERENCES Province(provinceId))
	ENGINE = InnoDB;

-- ------------------------------ Room ----------------------------------

CREATE TABLE Room (
	roomId BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(20) NOT NULL,
	capacity INT NOT NULL,
	cinemaId BIGINT NOT NULL,
	CONSTRAINT Room_PK PRIMARY KEY (roomId),
	CONSTRAINT RoomNameInCinema_UniqueKey UNIQUE (name, cinemaId),
	CONSTRAINT RoomCinema_FK FOREIGN KEY (cinemaId) REFERENCES Cinema(cinemaId))
	ENGINE = InnoDB;

-- ------------------------------ Movie ----------------------------------

CREATE TABLE Movie (
	movieId BIGINT NOT NULL AUTO_INCREMENT,
	title VARCHAR(40) NOT NULL,
	summary VARCHAR(100) NOT NULL,
	duration INT NOT NULL,
	initDate DATE NOT NULL,
	endDate DATE NOT NULL,
	CONSTRAINT Movie_PK PRIMARY KEY (movieId),
	CONSTRAINT MovieTitle_UniqueKey UNIQUE (title))
	ENGINE = InnoDB;


-- ------------------------------ SessionMovie ----------------------------------

CREATE TABLE SessionMovie (
	sessionMovieId BIGINT NOT NULL AUTO_INCREMENT,
	dateSession TIMESTAMP NOT NULL,
	avaliableSeats INT NOT NULL,
	price DOUBLE PRECISION NOT NULL,
	roomId BIGINT NOT NULL,
	movieId BIGINT NOT NULL,
	version BIGINT,
	CONSTRAINT SessionMovie_PK PRIMARY KEY (sessionMovieId),
	CONSTRAINT SessionMovieRoom_FK FOREIGN KEY (roomId) REFERENCES Room(roomId),
	CONSTRAINT SessionMovieMovie_FK FOREIGN KEY (movieId) REFERENCES Movie(movieId))
	ENGINE = InnoDB;

-- ------------------------------ Buy ----------------------------------

CREATE TABLE Buy (
	buyId BIGINT NOT NULL AUTO_INCREMENT,
	tickets INT NOT NULL,
	delivered BOOLEAN NOT NULL,
	cardNumber VARCHAR(16) NOT NULL,
	expirationDate DATE NOT NULL,
	buyDate TIMESTAMP NOT NULL,
	sessionMovieId BIGINT NOT NULL,
	usrId BIGINT NOT NULL,
	CONSTRAINT Buy_PK PRIMARY KEY (buyId),
	CONSTRAINT validTickets CHECK (tickets > 0 and tickets < 11),
	CONSTRAINT BuySessionMovie_FK FOREIGN KEY (sessionMovieId) REFERENCES SessionMovie(sessionMovieId),
	CONSTRAINT BuyUserProfile_FK FOREIGN KEY (usrId) REFERENCES UserProfile(usrId))
	ENGINE = InnoDB;

