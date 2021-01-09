update ecommerce.users
set lastname = 'Alarcón'
where name = 'Roberto' and email = 'roberto@email.com' and age = 21 and zone = 'Norte';

drop materialized view if exists ecommerce.Adults;

create materialized view ecommerce.Adults AS
select *
from ecommerce.users
where age >= 18
      and
      age <= 30
      and
      zone is not null
      and
      name is not null
      and
      email is not null
      and
      lastname is not null
primary key (name, email, age, zone)
with comment = 'Adults';

select * from ecommerce.Adults;

delete from ecommerce.users
where name = 'Mario' and lastname = 'García';