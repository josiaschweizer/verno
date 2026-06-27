CREATE TABLE participant_sibling
(
    participant_id BIGINT NOT NULL,
    sibling_id     BIGINT NOT NULL,

    CONSTRAINT pk_participant_sibling
        PRIMARY KEY (participant_id, sibling_id),

    CONSTRAINT fk_participant_sibling_participant
        FOREIGN KEY (participant_id)
            REFERENCES participant (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_participant_sibling_sibling
        FOREIGN KEY (sibling_id)
            REFERENCES participant (id)
            ON DELETE CASCADE,

    CONSTRAINT chk_participant_not_self
        CHECK (participant_id <> sibling_id)
);

CREATE INDEX idx_participant_sibling_participant
    ON participant_sibling (participant_id);

CREATE INDEX idx_participant_sibling_sibling
    ON participant_sibling (sibling_id);