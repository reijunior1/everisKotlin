alter table transactions alter column debtor_wallet_id drop not null;
alter table transactions alter column creditor_wallet_id drop not null;
