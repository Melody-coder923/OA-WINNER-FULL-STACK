package com.leetcode.tracker.service;

import com.leetcode.tracker.entity.*;
import com.leetcode.tracker.repository.ReviewHistoryRepository;
import com.leetcode.tracker.repository.UserQuestionReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for review operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    
    private final UserQuestionReviewRepository userQuestionReviewRepository;
    private final ReviewHistoryRepository reviewHistoryRepository;
    private final EbbinghausSchedulerService schedulerService;
    private final UserService userService;
    
    /**
     * Start reviewing a question for a user
     */
    @Transactional
    public UserQuestionReview startReview(User user, Question question) {
        Optional<UserQuestionReview> existingReview = 
            userQuestionReviewRepository.findByUserAndQuestion(user, question);
        
        if (existingReview.isPresent()) {
            return existingReview.get();
        }
        
        UserQuestionReview review = UserQuestionReview.builder()
                .user(user)
                .question(question)
                .status(UserQuestionReview.ReviewStatus.NOT_STARTED)
                .build();
        
        schedulerService.initializeReview(review);
        UserQuestionReview savedReview = userQuestionReviewRepository.save(review);
        
        log.info("Started review for user {} and question: {}", 
                user.getUsername(), question.getTitle());
        return savedReview;
    }
    
    /**
     * Submit a review rating
     */
    @Transactional
    public void submitReview(User user, Question question, ReviewHistory.ReviewRating rating, 
                           Integer timeSpentSeconds, String notes) {
        
        UserQuestionReview review = userQuestionReviewRepository
                .findByUserAndQuestion(user, question)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        
        // Create review history record
        ReviewHistory reviewHistory = ReviewHistory.builder()
                .user(user)
                .question(question)
                .reviewRating(rating)
                .timeSpentSeconds(timeSpentSeconds)
                .notes(notes)
                .intervalBefore(review.getCurrentIntervalIndex())
                .build();
        
        // Update review progress
        schedulerService.updateReviewProgress(review, rating);
        reviewHistory.setIntervalAfter(review.getCurrentIntervalIndex());
        
        // Save both entities
        reviewHistoryRepository.save(reviewHistory);
        userQuestionReviewRepository.save(review);
        
        // Update user's last active time
        userService.updateLastActive(user);
        
        log.info("Submitted review for user {} and question {}: rating={}, time={}s", 
                user.getUsername(), question.getTitle(), rating.getDisplayName(), timeSpentSeconds);
    }
    
    /**
     * Get due reviews for a user
     */
    public List<UserQuestionReview> getDueReviews(User user) {
        return userQuestionReviewRepository.findDueReviews(user, LocalDateTime.now());
    }
    
    /**
     * Get all reviews for a user
     */
    public List<UserQuestionReview> getUserReviews(User user) {
        return userQuestionReviewRepository.findByUser(user);
    }
    
    /**
     * Get reviews by status for a user
     */
    public List<UserQuestionReview> getUserReviewsByStatus(User user, UserQuestionReview.ReviewStatus status) {
        return userQuestionReviewRepository.findByUserAndStatus(user, status);
    }
    
    /**
     * Get review history for a user
     */
    public List<ReviewHistory> getReviewHistory(User user) {
        return reviewHistoryRepository.findByUser(user);
    }
    
    /**
     * Get review history for a specific question
     */
    public List<ReviewHistory> getQuestionReviewHistory(User user, Question question) {
        return reviewHistoryRepository.findByUserAndQuestion(user, question);
    }
    
    /**
     * Get recent review history
     */
    public List<ReviewHistory> getRecentReviewHistory(User user) {
        return reviewHistoryRepository.findRecentReviewsByUser(user);
    }
    
    /**
     * Get review statistics for a user
     */
    public ReviewStats getReviewStats(User user) {
        List<ReviewHistory> allReviews = reviewHistoryRepository.findByUser(user);
        long totalReviews = allReviews.size();
        long forgotCount = reviewHistoryRepository.countByUserAndReviewRating(user, ReviewHistory.ReviewRating.FORGOT);
        long hardCount = reviewHistoryRepository.countByUserAndReviewRating(user, ReviewHistory.ReviewRating.HARD);
        long goodCount = reviewHistoryRepository.countByUserAndReviewRating(user, ReviewHistory.ReviewRating.GOOD);
        long easyCount = reviewHistoryRepository.countByUserAndReviewRating(user, ReviewHistory.ReviewRating.EASY);
        
        Double averageTime = reviewHistoryRepository.findAverageTimeSpentByUser(user);
        
        return new ReviewStats(totalReviews, forgotCount, hardCount, goodCount, easyCount, averageTime);
    }
    
    /**
     * Mark question as mastered
     */
    @Transactional
    public void markAsMastered(User user, Question question) {
        UserQuestionReview review = userQuestionReviewRepository
                .findByUserAndQuestion(user, question)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        
        review.setStatus(UserQuestionReview.ReviewStatus.MASTERED);
        userQuestionReviewRepository.save(review);
        
        log.info("Marked question as mastered for user {}: {}", 
                user.getUsername(), question.getTitle());
    }
    
    /**
     * Reset review progress
     */
    @Transactional
    public void resetReviewProgress(User user, Question question) {
        UserQuestionReview review = userQuestionReviewRepository
                .findByUserAndQuestion(user, question)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        
        schedulerService.initializeReview(review);
        userQuestionReviewRepository.save(review);
        
        log.info("Reset review progress for user {}: {}", 
                user.getUsername(), question.getTitle());
    }
    
    /**
     * Review statistics data class
     */
    public static class ReviewStats {
        public final long totalReviews;
        public final long forgotCount;
        public final long hardCount;
        public final long goodCount;
        public final long easyCount;
        public final Double averageTime;
        
        public ReviewStats(long totalReviews, long forgotCount, long hardCount, 
                          long goodCount, long easyCount, Double averageTime) {
            this.totalReviews = totalReviews;
            this.forgotCount = forgotCount;
            this.hardCount = hardCount;
            this.goodCount = goodCount;
            this.easyCount = easyCount;
            this.averageTime = averageTime;
        }
    }
}
