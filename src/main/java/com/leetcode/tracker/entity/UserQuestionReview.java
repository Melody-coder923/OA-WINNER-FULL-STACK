package com.leetcode.tracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * UserQuestionReview entity tracking review progress per user per question
 */
@Entity
@Table(name = "user_question_review")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQuestionReview {
    
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
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ReviewStatus status = ReviewStatus.NOT_STARTED;
    
    @Column(name = "next_review_time")
    private LocalDateTime nextReviewTime;
    
    @Column(name = "current_interval_index")
    @Builder.Default
    private Integer currentIntervalIndex = 0;
    
    @Column(name = "total_reviews")
    @Builder.Default
    private Integer totalReviews = 0;
    
    @Column(name = "last_review_time")
    private LocalDateTime lastReviewTime;
    
    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Review status for tracking progress
     */
    public enum ReviewStatus {
        NOT_STARTED("未开始"),
        IN_PROGRESS("进行中"),
        MASTERED("已掌握"),
        ARCHIVED("已归档");
        
        private final String displayName;
        
        ReviewStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
