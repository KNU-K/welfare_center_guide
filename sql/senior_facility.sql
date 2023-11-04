use moapp;
create table senior_facility(
	sf_id int primary key auto_increment,
    sf_name varchar(255),
    sf_tel varchar(255),
    sf_latitude double,
    sf_longitude double,
    sf_addr varchar(40)
);
