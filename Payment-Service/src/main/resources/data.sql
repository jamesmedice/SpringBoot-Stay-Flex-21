-- src/main/resources/data.sql
-- Example seed data. Match column names to your entity mappings.

INSERT INTO FINANCE.T_ACCOUNT (id, available_balance, version)
VALUES
    ('11111111-1111-1111-1111-111111111111', 1000.00, 0),
    ('22222222-2222-2222-2222-222222222222', 50.00, 0);

INSERT INTO FINANCE.T_ACCOUNT_USER (account_id, user_id, role, status)
VALUES
    (
        '11111111-1111-1111-1111-111111111111',
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        'OWNER',
        'ACTIVE'
    ),
    (
        '11111111-1111-1111-1111-111111111111',
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
        'MEMBER',
        'ACTIVE'
    ),
    (
        '22222222-2222-2222-2222-222222222222',
        'cccccccc-cccc-cccc-cccc-cccccccccccc',
        'OWNER',
        'ACTIVE'
    );

-- No authorization rows are required initially.