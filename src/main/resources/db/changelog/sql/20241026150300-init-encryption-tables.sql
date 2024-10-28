--liquibase formatted sql

--changeset achumachenko:20241026150300

create table rsa_keys (
    id uuid primary key,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp,
    user_id uuid not null references users(id),
    public_key bytea not null,
    private_key bytea not null
);

create table block_cipher_keys (
    id uuid primary key,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp,
    user_id uuid not null references users(id),
    key_data text not null
);

create table encryption_results (
    id uuid primary key,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp,
    original_message_pdf bytea,
    result_pdf bytea,
    signature bytea,
    rsa_key_id uuid references rsa_keys(id),
    block_cipher_key_id uuid references block_cipher_keys(id)
);
