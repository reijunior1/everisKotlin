create table transactions (
	id uuid not null primary key,
	hash varchar(255) not null,
	date timestamp not null,
	debtor_id uuid not null,
	creditor_id uuid not null,
	amount bigint not null,
	type varchar(32) not null
);