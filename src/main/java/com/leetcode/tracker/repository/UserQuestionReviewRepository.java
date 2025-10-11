package com.leetcode.tracker.repository;

import com.leetcode.tracker.entity.User;
import com.leetcode.tracker.entity.UserQuestionReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for UserQuestionReview entity
 */
@Repository
public interface UserQuestionReviewRepository extends JpaRepository<UserQuestionReview, Long> {
    
    /**
     * Find review by user and question
     */
    Optional<UserQuestionReview> findByUserAndQuestion(User user, com.leetcode.tracker.entity.Question question);
    
    /**
     * Find all reviews for a user
     */
    List<UserQuestionReview> findByUser(User user);
    
    /**
     * Find reviews by user and status
     */
    List<UserQuestionReview> findByUserAndStatus(User user, UserQuestionReview.ReviewStatus status);
    
    /**
     * Find reviews due for review (next review time <= now)
     */
    @Query("SELECT uqr FROM UserQuestionReview uqr WHERE uqr.user = :user AND " +
           "uqr.nextReviewTime <= :now AND uqr.status = 'IN_PROGRESS'")
    List<UserQuestionReview> findDueReviews(@Param("user") User user, @Param("now") LocalDateTime now);
    
    /**
     * Find reviews by user and question status
     */
    @Query("SELECT uqr FROM UserQuestionReview uqr WHERE uqr.user = :user AND " +
           "uqr.question.difficulty = :difficulty AND uqr.status = :status")
    List<UserQuestionReview> findByUserAndQuestionDifficultyAndStatus(
            @Param("user") User user, 
            @Param("difficulty") com.leetcode.tracker.entity.Question.Difficulty difficulty,
            @Param("status") UserQuestionReview.ReviewStatus status);
    
    /**
     * Count total reviews for a user
     */
    long countByUser(User user);
    
    /**
     * Count reviews by status for a user
     */
    long countByUserAndStatus(User user, UserQuestionReview.ReviewStatus status);
    
    /**
     * Find reviews created between dates
     */
    @Query("SELECT uqr FROM UserQuestionReview uqr WHERE uqr.user = :user AND " +
           "uqr.createdAt BETWEEN :startDate AND :endDate")
    List<UserQuestionReview> findByUserAndCreatedAtBetween(
            @Param("user") User user, 
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find reviews that need to be updated for daily review generation
     */
    @Query("SELECT uqr FROM UserQuestionReview uqr WHERE uqr.nextReviewTime <= :now AND " +
           "uqr.status = 'IN_PROGRESS'")
    List<UserQuestionReview> findReviewsNeedingUpdate(@Param("now") LocalDateTime now);
}
