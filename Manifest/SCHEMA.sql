

CREATE TABLE "FINANCE"."T_USER" (
    uuid UUID PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(15),
    dob DATE CHECK (dob < CURRENT_DATE),
    created_at DATE
);