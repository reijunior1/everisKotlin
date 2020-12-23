create TABLE public.sellers (
	id uuid NOT NULL UNIQUE,
	cnpj varchar(20) NOT NULL UNIQUE,
	name varchar(255),
	email varchar(255),
	phone varchar(50),
	flow varchar(20),
	CONSTRAINT sellers_pkey PRIMARY KEY (id)
);