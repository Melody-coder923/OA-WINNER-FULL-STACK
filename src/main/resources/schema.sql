-- LeetCode Memory Tracker Database Schema
-- MySQL 8.x compatible

-- Create database (run this manually if needed)
-- CREATE DATABASE IF NOT EXISTS leetcode_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE leetcode_tracker;

-- User table
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    display_name VARCHAR(100),
    total_solved INT DEFAULT 0,
    current_streak INT DEFAULT 0,
    longest_streak INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_active TIMESTAMP NULL,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_current_streak (current_streak)
);

-- Question table
CREATE TABLE IF NOT EXISTS question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    difficulty ENUM('EASY', 'MEDIUM', 'HARD') NOT NULL,
    leetcode_url VARCHAR(500),
    leetcode_number INT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_difficulty (difficulty),
    INDEX idx_leetcode_number (leetcode_number),
    INDEX idx_title (title)
);

-- Question tags table (many-to-many relationship)
CREATE TABLE IF NOT EXISTS question_tags (
    question_id BIGINT NOT NULL,
    tag VARCHAR(50) NOT NULL,
    PRIMARY KEY (question_id, tag),
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE,
    INDEX idx_tag (tag)
);

-- User question review table
CREATE TABLE IF NOT EXISTS user_question_review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    status ENUM('NOT_STARTED', 'IN_PROGRESS', 'MASTERED', 'ARCHIVED') DEFAULT 'NOT_STARTED',
    next_review_time TIMESTAMP NULL,
    current_interval_index INT DEFAULT 0,
    total_reviews INT DEFAULT 0,
    last_review_time TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    UNIQUE KEY unique_user_question (user_id, question_id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE,
    INDEX idx_user_next_review (user_id, next_review_time, status),
    INDEX idx_next_review_time (next_review_time)
);

-- Review history table
CREATE TABLE IF NOT EXISTS review_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    review_rating ENUM('FORGOT', 'HARD', 'GOOD', 'EASY') NOT NULL,
    review_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    time_spent_seconds INT,
    notes TEXT,
    interval_before INT,
    interval_after INT,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE,
    INDEX idx_user_review_time (user_id, review_time),
    INDEX idx_question_review_time (question_id, review_time),
    INDEX idx_review_rating (review_rating)
);
