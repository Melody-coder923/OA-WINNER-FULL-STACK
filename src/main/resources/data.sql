-- Sample data for LeetCode Memory Tracker
-- This file contains initial data for testing and demonstration

-- Insert sample questions
INSERT IGNORE INTO question (id, title, difficulty, leetcode_url, leetcode_number, description) VALUES
(1, '两数之和', 'EASY', 'https://leetcode.cn/problems/two-sum/', 1, '给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。'),
(2, '两数相加', 'MEDIUM', 'https://leetcode.cn/problems/add-two-numbers/', 2, '给你两个非空的链表，表示两个非负的整数。它们每位数字都是按照逆序的方式存储的，并且每个节点只能存储一位数字。'),
(3, '无重复字符的最长子串', 'MEDIUM', 'https://leetcode.cn/problems/longest-substring-without-repeating-characters/', 3, '给定一个字符串 s ，请你找出其中不含有重复字符的最长子串的长度。'),
(4, '寻找两个正序数组的中位数', 'HARD', 'https://leetcode.cn/problems/median-of-two-sorted-arrays/', 4, '给定两个大小分别为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的中位数。'),
(5, '最长回文子串', 'MEDIUM', 'https://leetcode.cn/problems/longest-palindromic-substring/', 5, '给你一个字符串 s，找到 s 中最长的回文子串。'),
(6, 'Z字形变换', 'MEDIUM', 'https://leetcode.cn/problems/zigzag-conversion/', 6, '将一个给定字符串 s 根据给定的行数 numRows ，以从上往下、从左到右进行 Z 字形排列。'),
(7, '整数反转', 'EASY', 'https://leetcode.cn/problems/reverse-integer/', 7, '给你一个 32 位的有符号整数 x ，返回将 x 中的数字部分反转后的结果。'),
(8, '字符串转换整数 (atoi)', 'MEDIUM', 'https://leetcode.cn/problems/string-to-integer-atoi/', 8, '请你来实现一个 myAtoi(string s) 函数，使其能将字符串转换成一个 32 位有符号整数。'),
(9, '回文数', 'EASY', 'https://leetcode.cn/problems/palindrome-number/', 9, '给你一个整数 x ，如果 x 是一个回文整数，返回 true ；否则，返回 false 。'),
(10, '正则表达式匹配', 'HARD', 'https://leetcode.cn/problems/regular-expression-matching/', 10, '给你一个字符串 s 和一个字符规律 p，请你来实现一个支持 \'.\' 和 \'*\' 的正则表达式匹配。');

-- Insert question tags
INSERT IGNORE INTO question_tags (question_id, tag) VALUES
(1, '数组'), (1, '哈希表'),
(2, '链表'), (2, '数学'),
(3, '字符串'), (3, '滑动窗口'),
(4, '数组'), (4, '二分查找'), (4, '分治'),
(5, '字符串'), (5, '动态规划'),
(6, '字符串'),
(7, '数学'),
(8, '字符串'),
(9, '数学'),
(10, '字符串'), (10, '动态规划'), (10, '回溯');

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
(1, 2, 'GOOD', DATE_SUB(NOW(), INTERVAL 1 DAY), 300, '链表操作需要多练习', 0, 1),
(1, 3, 'EASY', DATE_SUB(NOW(), INTERVAL 3 DAY), 180, '滑动窗口思路清晰', 2, 3),
(1, 3, 'GOOD', DATE_SUB(NOW(), INTERVAL 7 DAY), 240, '边界条件需要注意', 1, 2),
(1, 3, 'HARD', DATE_SUB(NOW(), INTERVAL 10 DAY), 450, '第一次做，思路不清晰', 0, 1);
