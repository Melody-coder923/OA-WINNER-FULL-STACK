package com.leetcode.tracker.service;

import com.leetcode.tracker.entity.User;
import com.leetcode.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for User entity operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    /**
     * Create a new user
     */
    @Transactional
    public User createUser(String username, String email, String password, String displayName) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
        
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }
        
        User user = User.builder()
                .username(username)
                .email(email)
                .password(password) // In production, this should be hashed
                .displayName(displayName != null ? displayName : username)
                .totalSolved(0)
                .currentStreak(0)
                .longestStreak(0)
                .createdAt(LocalDateTime.now())
                .lastActive(LocalDateTime.now())
                .build();
        
        User savedUser = userRepository.save(user);
        log.info("Created new user: {}", username);
        return savedUser;
    }
    
    /**
     * Find user by username
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Find user by email
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Find user by ID
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Update user's last active time
     */
    @Transactional
    public void updateLastActive(User user) {
        user.setLastActive(LocalDateTime.now());
        userRepository.save(user);
    }
    
    /**
     * Update user's statistics
     */
    @Transactional
    public void updateUserStats(User user) {
        // This would typically involve calculating stats from review data
        // For now, we'll just save the user
        userRepository.save(user);
    }
    
    /**
     * Update user's streak
     */
    @Transactional
    public void updateStreak(User user, int newStreak) {
        user.setCurrentStreak(newStreak);
        if (newStreak > user.getLongestStreak()) {
            user.setLongestStreak(newStreak);
        }
        userRepository.save(user);
        log.info("Updated streak for user {}: {}", user.getUsername(), newStreak);
    }
    
    /**
     * Get top users by streak
     */
    public List<User> getTopUsersByStreak() {
        return userRepository.findTopUsersByStreak();
    }
    
    /**
     * Get top users by solved problems
     */
    public List<User> getTopUsersBySolved() {
        return userRepository.findTopUsersBySolved();
    }
    
    /**
     * Validate user credentials (simplified - in production use proper authentication)
     */
    public boolean validateCredentials(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() && user.get().getPassword().equals(password);
    }
    
    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Delete user
     */
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        log.info("Deleted user with ID: {}", userId);
    }
}
