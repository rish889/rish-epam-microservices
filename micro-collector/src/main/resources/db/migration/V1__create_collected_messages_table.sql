CREATE TABLE collected_messages (
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    collected_at TIMESTAMP NOT NULL
);
