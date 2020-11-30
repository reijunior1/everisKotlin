create TABLE public.citizens (
	id uuid NOT NULL,
	cpf varchar(255) NULL,
	email varchar(255) NULL,
	onboarded bool NOT NULL,
	phone varchar(255) NOT NULL,
	CONSTRAINT citizens_pkey PRIMARY KEY (id)
);