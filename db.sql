create table tourists(
	id serial PRIMARY KEY,
	name VARCHAR (50) NOT NULL,
	surname VARCHAR (50) NOT NULL,
	sex VARCHAR(10) NOT NULL,
	country VARCHAR(50) NOT NULL,
	notes text(500) ,
	birthdate DATE NOT NULL,
	flights INTEGER[]	
);

create table flights(
	id serial PRIMARY KEY,
	departure_time TIMESTAMP,
	arival_time TIMESTAMP,
	places INTEGER,
	tourists INTEGER[] ,
	ticket_price FLOAT
);

INSERT INTO tourists (name,surname,sex,country,notes,birthdate) 
values ('Jan','Kowalski','male','Poland','example note','1990-05-22');

INSERT INTO tourists (name,surname,sex,country,notes,birthdate) 
values ('Olaf','Grudka','male','Poland','example note','1992-05-22');
INSERT INTO tourists (name,surname,sex,country,notes,birthdate) 
values ('Leon','Nowak','male','Poland','example note','1992-05-22');

UPDATE tourists SET flights = array_append(flights,1) WHERE id=1;

UPDATE tourists SET flights = array_remove(flights,1) WHERE id=14;


insert into flights (departure_time, arival_time,places,ticket_price) 
values ('2019-07-11','2019-08-15',12,60000);