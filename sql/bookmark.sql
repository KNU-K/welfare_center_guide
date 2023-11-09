create table bookmark(
	bookmark_id int auto_increment primary key,
    u_id int not null,
    sf_id int not null,
    foreign key(u_id) references user(u_id)
    on delete cascade
    on update cascade,
    
    foreign key(sf_id) references senior_facility(sf_id)
    on delete cascade
    on update cascade
);
alter table bookmark add unique index seq_index(u_id, sf_id);