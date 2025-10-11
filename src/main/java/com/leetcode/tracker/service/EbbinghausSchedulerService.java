package com.leetcode.tracker.service;

import com.leetcode.tracker.entity.ReviewHistory;
import com.leetcode.tracker.entity.UserQuestionReview;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Service implementing Ebbinghaus forgetting curve scheduling algorithm
 */
@Service
@Slf4j
public class EbbinghausSchedulerService {
    
    // Default intervals in days: 1, 2, 4, 7, 15, 30
    private final List<Integer> defaultIntervals = Arrays.asList(1, 2, 4, 7, 15, 30);
    
    @Value("${app.ebbinghaus.intervals:1,2,4,7,15,30}")
    private List<Integer> intervals;
    
    /**
     * Calculate next review time based on current rating and interval
     */
    public LocalDateTime calculateNextReviewTime(UserQuestionReview review, ReviewHistory.ReviewRating rating) {
        int currentIndex = review.getCurrentIntervalIndex();
        int nextIntervalDays = getNextInterval(currentIndex, rating);
        
        LocalDateTime nextReviewTime = LocalDateTime.now().plusDays(nextIntervalDays);
        
        log.debug("Calculated next review time: {} days from now for rating: {}", 
                 nextIntervalDays, rating.getDisplayName());
        
        return nextReviewTime;
    }
    
    /**
     * Get next interval based on current interval index and review rating
     */
    private int getNextInterval(int currentIndex, ReviewHistory.ReviewRating rating) {
        List<Integer> intervalsToUse = intervals.isEmpty() ? defaultIntervals : intervals;
        
        switch (rating) {
            case FORGOT:
                // Reset to first interval
                return intervalsToUse.get(0);
                
            case HARD:
                // Stay at current interval or go back one
                if (currentIndex > 0) {
                    return intervalsToUse.get(currentIndex - 1);
                }
                return intervalsToUse.get(0);
                
            case GOOD:
                // Move to next interval
                if (currentIndex < intervalsToUse.size() - 1) {
                    return intervalsToUse.get(currentIndex + 1);
                }
                // If at last interval, double it
                return intervalsToUse.get(currentIndex) * 2;
                
            case EASY:
                // Skip one interval or double current
                if (currentIndex < intervalsToUse.size() - 2) {
                    return intervalsToUse.get(currentIndex + 2);
                } else if (currentIndex < intervalsToUse.size() - 1) {
                    return intervalsToUse.get(currentIndex + 1) * 2;
                } else {
                    // If at last interval, triple it
                    return intervalsToUse.get(currentIndex) * 3;
                }
                
            default:
                return intervalsToUse.get(0);
        }
    }
    
    /**
     * Update review progress after a review session
     */
    public void updateReviewProgress(UserQuestionReview review, ReviewHistory.ReviewRating rating) {
        int currentIndex = review.getCurrentIntervalIndex();
        int newIndex = getNewIntervalIndex(currentIndex, rating);
        
        review.setCurrentIntervalIndex(newIndex);
        review.setNextReviewTime(calculateNextReviewTime(review, rating));
        review.setLastReviewTime(LocalDateTime.now());
        review.setTotalReviews(review.getTotalReviews() + 1);
        
        // Update status based on progress
        if (newIndex >= intervals.size() - 1 && rating == ReviewHistory.ReviewRating.EASY) {
            review.setStatus(UserQuestionReview.ReviewStatus.MASTERED);
        } else if (review.getStatus() == UserQuestionReview.ReviewStatus.NOT_STARTED) {
            review.setStatus(UserQuestionReview.ReviewStatus.IN_PROGRESS);
        }
        
        log.info("Updated review progress: interval index {} -> {}, next review: {}", 
                currentIndex, newIndex, review.getNextReviewTime());
    }
    
    /**
     * Get new interval index based on rating
     */
    private int getNewIntervalIndex(int currentIndex, ReviewHistory.ReviewRating rating) {
        List<Integer> intervalsToUse = intervals.isEmpty() ? defaultIntervals : intervals;
        
        switch (rating) {
            case FORGOT:
                return 0; // Reset to beginning
                
            case HARD:
                return Math.max(0, currentIndex - 1);
                
            case GOOD:
                return Math.min(intervalsToUse.size() - 1, currentIndex + 1);
                
            case EASY:
                return Math.min(intervalsToUse.size() - 1, currentIndex + 2);
                
            default:
                return currentIndex;
        }
    }
    
    /**
     * Initialize review for a new question
     */
    public void initializeReview(UserQuestionReview review) {
        review.setStatus(UserQuestionReview.ReviewStatus.IN_PROGRESS);
        review.setCurrentIntervalIndex(0);
        review.setNextReviewTime(LocalDateTime.now().plusDays(1)); // First review in 1 day
        review.setTotalReviews(0);
        review.setLastReviewTime(null);
        
        log.info("Initialized new review for question: {}", review.getQuestion().getTitle());
    }
    
    /**
     * Get intervals configuration
     */
    public List<Integer> getIntervals() {
        return intervals.isEmpty() ? defaultIntervals : intervals;
    }
}
