create table address
(
    id bigserial not null primary key,
    street varchar(50)
);

create sequence client_SEQ start with 1 increment by 1;

create table client
(
    id bigint not null primary key,
    name varchar(50),
    address_id bigint references address(id)
);

alter table client
    add constraint fk_client_address foreign key (address_id) references address (id);

create table phone
(
    id bigserial not null primary key,
    number varchar(50),
    client_id bigint references client(id)
);

alter table phone
    add constraint fk_phone_client foreign key (client_id) references client (id);