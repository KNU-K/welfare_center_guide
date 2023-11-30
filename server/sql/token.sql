create table token(
	u_id int not null,
    refresh_token varchar(255)	primary key not null,
	foreign key(u_id) references user(u_id)
	on update cascade
    on delete cascade
);
desc token;