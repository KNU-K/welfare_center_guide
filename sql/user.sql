
create table user(
    u_id int primary key auto_increment,
    u_name varchar(255),
    u_email varchar(255),
    u_provider ENUM('naver','kakao', 'google'),
    u_provider_id varchar(255)
);
