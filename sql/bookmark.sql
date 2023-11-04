create table bookmark(
	bookmark_id int auto_increment primary key,
    u_id int,
    sf_id int,
    foreign key(u_id) references user(u_id)
    on delete cascade
    on update cascade,
    
    foreign key(sf_id) references senior_facility(sf_id)
    on delete cascade
    on update cascade
);