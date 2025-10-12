-- Sample data for LeetCode Memory Tracker
-- This file contains initial data for testing and demonstration

-- Insert sample questions
INSERT IGNORE INTO question (id, title, difficulty, leetcode_url, leetcode_number, description) VALUES
(1, 'Two Sum', 'EASY', 'https://leetcode.com/problems/two-sum/', 1, 'Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.'),
(2, 'Add Two Numbers', 'MEDIUM', 'https://leetcode.com/problems/add-two-numbers/', 2, 'You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order, and each of their nodes contains a single digit.'),
(3, 'Longest Substring Without Repeating Characters', 'MEDIUM', 'https://leetcode.com/problems/longest-substring-without-repeating-characters/', 3, 'Given a string s, find the length of the longest substring without repeating characters.'),
(4, 'Median of Two Sorted Arrays', 'HARD', 'https://leetcode.com/problems/median-of-two-sorted-arrays/', 4, 'Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.'),
(5, 'Longest Palindromic Substring', 'MEDIUM', 'https://leetcode.com/problems/longest-palindromic-substring/', 5, 'Given a string s, return the longest palindromic substring in s.'),
(6, 'Zigzag Conversion', 'MEDIUM', 'https://leetcode.com/problems/zigzag-conversion/', 6, 'The string "PAYPALISHIRING" is written in a zigzag pattern on a given number of rows.'),
(7, 'Reverse Integer', 'EASY', 'https://leetcode.com/problems/reverse-integer/', 7, 'Given a signed 32-bit integer x, return x with its digits reversed.'),
(8, 'String to Integer (atoi)', 'MEDIUM', 'https://leetcode.com/problems/string-to-integer-atoi/', 8, 'Implement the myAtoi(string s) function, which converts a string to a 32-bit signed integer.'),
(9, 'Palindrome Number', 'EASY', 'https://leetcode.com/problems/palindrome-number/', 9, 'Given an integer x, return true if x is a palindrome, and false otherwise.'),
(10, 'Regular Expression Matching', 'HARD', 'https://leetcode.com/problems/regular-expression-matching/', 10, 'Given an input string s and a pattern p, implement regular expression matching with support for \'.\' and \'*\'.');

-- Insert question tags
INSERT IGNORE INTO question_tags (question_id, tag) VALUES
(1, 'Array'), (1, 'Hash Table'),
(2, 'Linked List'), (2, 'Math'),
(3, 'String'), (3, 'Sliding Window'),
(4, 'Array'), (4, 'Binary Search'), (4, 'Divide and Conquer'),
(5, 'String'), (5, 'Dynamic Programming'),
(6, 'String'),
(7, 'Math'),
(8, 'String'),
(9, 'Math'),
(10, 'String'), (10, 'Dynamic Programming'), (10, 'Backtracking');

-- Insert sample user (demo user)
INSERT IGNORE INTO user (id, username, email, password, display_name, total_solved, current_streak, longest_streak) VALUES
(1, 'demo', 'demo@example.com', 'password', 'Demo User', 0, 0, 0);

-- Insert sample user question reviews
INSERT IGNORE INTO user_question_review (user_id, question_id, status, next_review_time, current_interval_index, total_reviews) VALUES
(1, 1, 'IN_PROGRESS', DATE_ADD(NOW(), INTERVAL 1 DAY), 0, 0),
(1, 2, 'IN_PROGRESS', DATE_ADD(NOW(), INTERVAL 2 DAY), 1, 1),
(1, 3, 'MASTERED', DATE_ADD(NOW(), INTERVAL 7 DAY), 3, 3),
(1, 7, 'IN_PROGRESS', DATE_ADD(NOW(), INTERVAL 1 DAY), 0, 0);

-- Insert sample review history
INSERT IGNORE INTO review_history (user_id, question_id, review_rating, review_time, time_spent_seconds, notes, interval_before, interval_after) VALUES
(1, 2, 'GOOD', DATE_SUB(NOW(), INTERVAL 1 DAY), 300, 'Need more practice with linked list operations', 0, 1),
(1, 3, 'EASY', DATE_SUB(NOW(), INTERVAL 3 DAY), 180, 'Sliding window approach is clear', 2, 3),
(1, 3, 'GOOD', DATE_SUB(NOW(), INTERVAL 7 DAY), 240, 'Need to pay attention to edge cases', 1, 2),
(1, 3, 'HARD', DATE_SUB(NOW(), INTERVAL 10 DAY), 450, 'First attempt, unclear approach', 0, 1);
