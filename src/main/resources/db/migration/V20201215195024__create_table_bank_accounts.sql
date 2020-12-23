create table bank_accounts (
    id uuid not null unique,
    name varchar(255) not null,
    cnpj varchar(20) not null unique,
    agency varchar(20),
    account varchar(20)
);