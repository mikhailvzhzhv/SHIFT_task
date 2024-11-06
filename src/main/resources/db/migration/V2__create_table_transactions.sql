create table if not exists transactions (
    id serial primary key,
    amount integer not null,
    payment_type varchar(8) not null,
    transaction_date timestamp not null,
    seller_id integer references sellers(id) on delete set null,
    check ((payment_type = 'CASH') or (payment_type = 'CARD') or (payment_type = 'TRANSFER'))
);