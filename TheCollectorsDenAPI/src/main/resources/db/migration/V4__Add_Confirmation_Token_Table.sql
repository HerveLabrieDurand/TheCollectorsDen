CREATE TABLE confirmation_tokens
(
    confirmation_token_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id               INT NOT NULL,
    token                 VARCHAR(255),
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);