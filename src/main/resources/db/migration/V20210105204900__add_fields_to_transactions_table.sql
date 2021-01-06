
delete from transactions;
alter table transactions add column debtorName varchar(255) null;
alter table transactions add column creditorName varchar(255) null;
alter table transactions add column receipt text null;
alter table transactions add column status varchar(32) null;