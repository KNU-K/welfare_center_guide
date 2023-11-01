create table bookmark(
	bookmark_id int primary key,
    u_id int,
    sf_id int,
    foreign key(u_id) references user(u_id),
    foreign key(sf_id) references senior_facility(sf_id)
);