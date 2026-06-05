ALTER TABLE tickets
    ADD COLUMN assigned_user_id BIGINT;

ALTER TABLE tickets
    ADD CONSTRAINT fk_tickets_assigned_user
        FOREIGN KEY (assigned_user_id)
            REFERENCES users(id);