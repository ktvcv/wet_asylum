
drop keyspace if exists ecommerce;

create keyspace ecommerce
with replication = { 'class': 'SimpleStrategy', 'replication_factor': 1 };

drop table if exists ecommerce.users;

create table ecommerce.users (
    name     text,
    lastname text,
    email    text,
    age      tinyint,
    zone     text,
    primary key (name, lastname, email, age, zone)
);


insert into ecommerce.users (name, lastname, email, age, zone)
values ('Juan', 'Martínez', 'juan@email.com', 18, 'Norte');

insert into ecommerce.users (name, lastname, email, age, zone)
values ('María', 'García', 'maria@email.com', 25, 'Sur');

insert into ecommerce.users (name, lastname, email, age, zone)
values ('Domingo', '', 'domingo@email.com', 32, 'Norte');

insert into ecommerce.users (name, lastname, email, age, zone)
values ('Roberto', 'Luján', 'roberto@email.com', 21, 'Norte');

insert into ecommerce.users (name, lastname, email, age, zone)
values ('Mario', 'García', 'mario@email.com', 25, 'Sur');