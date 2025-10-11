package com.leetcode.tracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User entity representing a user account
 */
@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "display_name")
    private String displayName;
    
    @Column(name = "total_solved")
    @Builder.Default
    private Integer totalSolved = 0;
    
    @Column(name = "current_streak")
    @Builder.Default
    private Integer currentStreak = 0;
    
    @Column(name = "longest_streak")
    @Builder.Default
    private Integer longestStreak = 0;
    
    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "last_active")
    private LocalDateTime lastActive;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserQuestionReview> questionReviews;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReviewHistory> reviewHistories;
    
    @PreUpdate
    public void preUpdate() {
        this.lastActive = LocalDateTime.now();
    }
}
