--liquibase formatted sql

--changeset achumachenko:20241023213900

-- Inserting the default roles into the roles table
insert into roles (id, role)
values ('b800a2b3-4029-4408-b4ac-1c89f94fdf4d', 'DEFAULT_USER'), ('01602ae4-0606-4bf5-a221-7daada1d72c3', 'ADMIN');
