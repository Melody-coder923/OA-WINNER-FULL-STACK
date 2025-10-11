package com.leetcode.tracker.repository;

import com.leetcode.tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by username
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Find users with longest streaks for leaderboard
     */
    @Query("SELECT u FROM User u ORDER BY u.longestStreak DESC, u.currentStreak DESC")
    java.util.List<User> findTopUsersByStreak();
    
    /**
     * Find users with most solved problems
     */
    @Query("SELECT u FROM User u ORDER BY u.totalSolved DESC")
    java.util.List<User> findTopUsersBySolved();
}
