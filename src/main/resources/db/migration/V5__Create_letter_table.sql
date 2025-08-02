CREATE TABLE letters
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    chat_room_id BIGINT                              NOT NULL,
    fairy_id     BIGINT                              NOT NULL,
    user_input   TEXT                                NOT NULL,
    contents     TEXT                                NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_id   VARCHAR(255)                        NOT NULL,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    updated_id   VARCHAR(255)                        NOT NULL,
    deleted_at   TIMESTAMP NULL
);