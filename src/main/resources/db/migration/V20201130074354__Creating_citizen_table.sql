create TABLE public.citizens (
	id uuid NOT NULL UNIQUE,
	cpf varchar(255) NOT NULL UNIQUE,
	name varchar(255),
	email varchar(255),
	phone varchar(255),
	flow varchar(255),
	CONSTRAINT citizens_pkey PRIMARY KEY (id)
);
