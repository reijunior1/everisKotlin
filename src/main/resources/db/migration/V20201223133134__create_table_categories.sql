create table categories (
	id uuid primary key,
	category varchar(20),
	price bigint
);

alter table children add constraint child_category_fk foreign key (category_id) references categories (id);
