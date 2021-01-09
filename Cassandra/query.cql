
-- Q1
drop materialized view if exists ecommerce.Q1;

create materialized view ecommerce.Q1 AS
select *
from ecommerce.users
where age < 25
      and
      zone = 'Norte'
      and
      name is not null
      and
      email is not null
      and
      lastname is not null
primary key (name, email, lastname, age, zone)
with comment = 'Q1';

select * from ecommerce.Q1;

-- Q2
drop materialized view if exists ecommerce.Q2;

create materialized view ecommerce.Q2 AS
select *
from ecommerce.users
where age is not null
      and
      zone is not null
      and
      name is not null
      and
      email = 'juan@email.com'
      and
      lastname is not null
primary key (name, email, lastname, age, zone)
with comment = 'Q2';

select * from ecommerce.Q2;

-- Q3
drop materialized view if exists ecommerce.Q3;

create materialized view ecommerce.Q3 AS
select *
from ecommerce.users
where age is not null
      and
      zone = 'Norte'
      and
      name is not null
      and
      email is not null
      and
      lastname is not null
primary key (name, email, lastname, age, zone)
with comment = 'Q3';

select count(*) as count_zone_north from ecommerce.Q3;

-- Q4
drop materialized view if exists ecommerce.MaxAgeNorth;

create materialized view ecommerce.MaxAgeNorth AS
select *
from ecommerce.users
where age = 32
      and
      zone = 'Norte'
      and
      name is not null
      and
      email is not null
      and
      lastname is not null
primary key (name, email, lastname, age, zone)
with comment = 'MaxAgeNorth' and clustering order by (age asc);

drop materialized view if exists ecommerce.MaxAgeSouth;

create materialized view ecommerce.MaxAgeSouth AS
select *
from ecommerce.users
where age = 25
      and
      zone = 'Sur'
      and
      name is not null
      and
      email is not null
      and
      lastname is not null
primary key (name, email, lastname, age, zone)
with comment = 'MaxAgeSouth' and clustering order by (age asc);

select * from ecommerce.MaxAgeNorth limit 1;
select * from ecommerce.MaxAgeSouth limit 1;

-- Q5
select avg(age) as avg_age from ecommerce.Q3;

-- Q6
drop materialized view if exists ecommerce.Q6;

create materialized view ecommerce.Q6 AS
select *
from ecommerce.users
where age is not null
      and
      zone is not null
      and
      name = 'Juan'
      and
      email is not null
      and
      lastname is not null
primary key (name, email, lastname, age, zone)
with comment = 'Q6' and clustering order by (lastname asc);

select * from ecommerce.Q6;

-- Q7
drop materialized view if exists ecommerce.Q7;

create materialized view ecommerce.Q7 AS
select *
from ecommerce.users
where age = 18
      and
      zone is not null
      and
      name = 'Juan'
      and
      email is not null
      and
      lastname is not null
primary key (name, email, lastname, age, zone)
with comment = 'Q7';

select * from ecommerce.Q7;