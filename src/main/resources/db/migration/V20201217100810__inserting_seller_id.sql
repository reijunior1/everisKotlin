alter table bank_accounts add column seller_id uuid;

alter table bank_accounts add constraint bank_accounts_fk foreign key (seller_id) references sellers (id);