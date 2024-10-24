--liquibase formatted sql

--changeset achumachenko:20241023213900

-- Create enum type for roles
create type role_enum as enum ('DEFAULT_USER', 'ADMIN');

-- Creating the users table
create table users (
    id uuid primary key,
    username varchar(50) not null unique,
    password varchar(255) not null,
    email varchar(100) unique,
    phone varchar(20) unique,
    created_at timestamp without time zone default current_timestamp,
    updated_at timestamp without time zone default current_timestamp
);

-- Creating the roles table
create table roles (
    id uuid primary key,
    role role_enum not null unique
);

-- Creating the user_roles table for the many-to-many relationship
create table user_roles (
    user_id uuid not null,
    role_id uuid not null,
    primary key (user_id, role_id),
    constraint fk_user
        foreign key (user_id) references users(id)
            on delete cascade,
    constraint fk_role
        foreign key (role_id) references roles(id)
            on delete cascade
);
