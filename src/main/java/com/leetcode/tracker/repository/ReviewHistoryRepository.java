package com.leetcode.tracker.repository;

import com.leetcode.tracker.entity.ReviewHistory;
import com.leetcode.tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for ReviewHistory entity
 */
@Repository
public interface ReviewHistoryRepository extends JpaRepository<ReviewHistory, Long> {
    
    /**
     * Find review history by user
     */
    List<ReviewHistory> findByUser(User user);
    
    /**
     * Find review history by user and question
     */
    List<ReviewHistory> findByUserAndQuestion(User user, com.leetcode.tracker.entity.Question question);
    
    /**
     * Find review history by user and rating
     */
    List<ReviewHistory> findByUserAndReviewRating(User user, ReviewHistory.ReviewRating rating);
    
    /**
     * Find review history between dates
     */
    @Query("SELECT rh FROM ReviewHistory rh WHERE rh.user = :user AND " +
           "rh.reviewTime BETWEEN :startDate AND :endDate")
    List<ReviewHistory> findByUserAndReviewTimeBetween(
            @Param("user") User user, 
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate);
    
    /**
     * Count reviews by rating for a user
     */
    long countByUserAndReviewRating(User user, ReviewHistory.ReviewRating rating);
    
    /**
     * Find average time spent on reviews for a user
     */
    @Query("SELECT AVG(rh.timeSpentSeconds) FROM ReviewHistory rh WHERE rh.user = :user AND rh.timeSpentSeconds IS NOT NULL")
    Double findAverageTimeSpentByUser(@Param("user") User user);
    
    /**
     * Find recent review history for a user
     */
    @Query("SELECT rh FROM ReviewHistory rh WHERE rh.user = :user ORDER BY rh.reviewTime DESC")
    List<ReviewHistory> findRecentReviewsByUser(@Param("user") User user);
    
    /**
     * Find review history by date range and rating
     */
    @Query("SELECT rh FROM ReviewHistory rh WHERE rh.user = :user AND " +
           "rh.reviewTime BETWEEN :startDate AND :endDate AND rh.reviewRating = :rating")
    List<ReviewHistory> findByUserAndReviewTimeBetweenAndReviewRating(
            @Param("user") User user, 
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate,
            @Param("rating") ReviewHistory.ReviewRating rating);
}
