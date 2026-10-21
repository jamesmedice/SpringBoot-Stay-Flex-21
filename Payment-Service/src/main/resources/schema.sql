CREATE SCHEMA IF NOT EXISTS FINANCE;

CREATE TABLE IF NOT EXISTS FINANCE.T_ACCOUNT (
    id UUID PRIMARY KEY,
    available_balance NUMERIC(19, 2) NOT NULL CHECK (available_balance >= 0),
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS FINANCE.T_DEPOSIT (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL REFERENCES FINANCE.T_ACCOUNT(id),
    idempotency_key VARCHAR(150) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL CHECK (amount > 0),
    created_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT uq_deposit_idempotency UNIQUE (account_id, idempotency_key)
);

CREATE TABLE IF NOT EXISTS FINANCE.T_PAYMENT_AUTHORIZATION (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL REFERENCES FINANCE.T_ACCOUNT(id),
    cardholder_id UUID NOT NULL,
    idempotency_key VARCHAR(150) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL CHECK (amount > 0),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT uq_authorization_idempotency UNIQUE (account_id, idempotency_key)
);

CREATE TABLE IF NOT EXISTS FINANCE.T_ACCOUNT_USER (
    account_id UUID NOT NULL REFERENCES FINANCE.T_ACCOUNT(id),
    user_id UUID NOT NULL,
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT pk_account_user PRIMARY KEY (account_id, user_id),

    CONSTRAINT chk_account_user_role CHECK (role IN ('OWNER', 'MEMBER')),

    CONSTRAINT chk_account_user_status CHECK (status IN ('ACTIVE', 'PENDING', 'REVOKED'))
);
