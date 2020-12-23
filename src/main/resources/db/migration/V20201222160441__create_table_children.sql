create table children (
	id uuid primary key,
	name varchar(255),
	category_id uuid,
	citizen_id uuid not null
);

alter table children add constraint child_citizen_fk foreign key (citizen_id) references citizens (id);