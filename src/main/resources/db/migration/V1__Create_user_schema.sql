-- Create user schema for Gamchi API
-- This migration creates the basic user table structure

CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    nickname VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  NOT NULL ,
    created_id VARCHAR(255) NOT NULL ,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL ,
    updated_id VARCHAR(255) NOT NULL ,
    deleted_at TIMESTAMP NULL,
    PRIMARY KEY (id)
);
