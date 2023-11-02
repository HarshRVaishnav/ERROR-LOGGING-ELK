    create table order_tbl (
       order_number integer generated by default as identity,
        comments varchar(255),
        customer_number integer,
        order_date timestamp,
        shipped_date timestamp,
        status varchar(255),
        primary key (order_number)
    )