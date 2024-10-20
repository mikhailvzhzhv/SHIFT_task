create table if not exists sellers (
    id serial primary key,
    name varchar(20) not null,
    contact_info varchar(50) not null,
    registration_date timestamp not null
);