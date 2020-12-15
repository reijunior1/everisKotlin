create TABLE public.prices (
	id integer NOT NULL UNIQUE,
	price bigint,
	initial_date timestamp,
	final_date timestamp,
	CONSTRAINT values_pkey PRIMARY KEY (id)
);