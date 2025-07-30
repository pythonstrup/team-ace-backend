CREATE TABLE chat_rooms (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_id VARCHAR(255),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_id VARCHAR(255),
    deleted_at TIMESTAMP NULL,
    INDEX idx_chat_rooms_user_id (user_id),
    INDEX idx_chat_rooms_deleted_at (deleted_at)
);

CREATE TABLE chats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    chat_room_id BIGINT NOT NULL,
    type VARCHAR(10) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_id VARCHAR(255),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_id VARCHAR(255),
    deleted_at TIMESTAMP NULL,
    INDEX idx_chats_chat_room_id (chat_room_id),
    INDEX idx_chats_deleted_at (deleted_at)
);