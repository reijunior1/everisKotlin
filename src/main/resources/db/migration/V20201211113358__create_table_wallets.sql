create TABLE wallets (
	id uuid NOT NULL UNIQUE,
	owner uuid NOT NULL,
	token uuid NOT NULL,
	type varchar(50),
	CONSTRAINT wallets_pkey PRIMARY KEY (id)
);

create UNIQUE INDEX idx_wallets_owner_token ON wallets(owner, token);