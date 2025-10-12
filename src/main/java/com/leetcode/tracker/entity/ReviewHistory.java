package com.leetcode.tracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * ReviewHistory entity tracking historical review sessions
 */
@Entity
@Table(name = "review_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "review_rating", nullable = false)
    private ReviewRating reviewRating;
    
    @Column(name = "review_time")
    @Builder.Default
    private LocalDateTime reviewTime = LocalDateTime.now();
    
    @Column(name = "time_spent_seconds")
    private Integer timeSpentSeconds;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "interval_before")
    private Integer intervalBefore;
    
    @Column(name = "interval_after")
    private Integer intervalAfter;
    
    /**
     * Review rating based on Ebbinghaus forgetting curve
     */
    public enum ReviewRating {
        FORGOT("Forgot", 0),
        HARD("Hard", 1),
        GOOD("Good", 2),
        EASY("Easy", 3);
        
        private final String displayName;
        private final int value;
        
        ReviewRating(String displayName, int value) {
            this.displayName = displayName;
            this.value = value;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public int getValue() {
            return value;
        }
    }
}
