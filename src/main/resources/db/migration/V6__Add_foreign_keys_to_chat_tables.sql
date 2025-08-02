-- Add foreign key constraints to chat tables
-- This migration converts indexes to foreign keys for better data integrity

-- Drop existing indexes that will be replaced with foreign keys
ALTER TABLE chat_rooms DROP INDEX idx_chat_rooms_user_id;
ALTER TABLE chats DROP INDEX idx_chats_chat_room_id;

-- Add foreign key constraints
ALTER TABLE chat_rooms 
    ADD CONSTRAINT fk_chat_rooms_user_id 
    FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE chats 
    ADD CONSTRAINT fk_chats_chat_room_id 
    FOREIGN KEY (chat_room_id) REFERENCES chat_rooms(id);

-- Note: idx_chat_rooms_deleted_at and idx_chats_deleted_at indexes are kept for performance