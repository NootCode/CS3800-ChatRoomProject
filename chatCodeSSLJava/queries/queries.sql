create database chat_app

create table account (
    username varchar(50),
    password varchar(50),
    profile_picture BLOB,
    PRIMARY KEY(username)
);

create table friend_list (
    friend_username varchar(50),
    username varchar(50),
    PRIMARY KEY(friend_username),
    FOREIGN KEY (friend_username)  REFERENCES account (username)
);

create table messages (
    message_id int NOT NULL AUTO_INCREMENT,
    sender varchar(50),
    receiver varchar(50),
    message varchar(2000),
    PRIMARY KEY (message_id),
    FOREIGN KEY (sender) REFERENCES account (username),
    FOREIGN KEY (receiver) REFERENCES account (username)
)

show tables;

show databases;

insert into account (username, password) VALUES ("debug_user", "123");

insert into account (username, password) VALUES ("debug_user2", "123");

select * from account;

select * from messages where sender = "test" order by message_id DESC limit 50;

select * from messages where receiver = "test123";

insert into messages (sender, receiver, message) values("test", "test123", "(test123) test")