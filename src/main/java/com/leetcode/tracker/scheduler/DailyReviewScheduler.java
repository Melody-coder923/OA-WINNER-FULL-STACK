package com.leetcode.tracker.scheduler;

import com.leetcode.tracker.entity.User;
import com.leetcode.tracker.entity.UserQuestionReview;
import com.leetcode.tracker.repository.UserQuestionReviewRepository;
import com.leetcode.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Scheduled tasks for daily operations
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DailyReviewScheduler {
    
    private final UserQuestionReviewRepository userQuestionReviewRepository;
    private final UserService userService;
    
    /**
     * Daily review generator - runs at 1 AM
     * Marks questions as due for review based on next review time
     */
    @Scheduled(cron = "${app.scheduler.daily-review-generator:0 0 1 * * ?}")
    @Transactional
    public void generateDailyReviews() {
        log.info("Starting daily review generation at {}", LocalDateTime.now());
        
        LocalDateTime now = LocalDateTime.now();
        List<UserQuestionReview> reviewsNeedingUpdate = 
            userQuestionReviewRepository.findReviewsNeedingUpdate(now);
        
        int updatedCount = reviewsNeedingUpdate.size();
        // The reviews are already marked as due by the query
        // We could add additional logic here if needed
        
        log.info("Daily review generation completed. {} reviews marked as due for review", updatedCount);
    }
    
    /**
     * Streak updater - runs at midnight
     * Updates user streaks based on daily activity
     */
    @Scheduled(cron = "${app.scheduler.streak-updater:0 0 0 * * ?}")
    @Transactional
    public void updateUserStreaks() {
        log.info("Starting streak update at {}", LocalDateTime.now());
        
        List<User> allUsers = userService.getAllUsers();
        int updatedCount = 0;
        
        for (User user : allUsers) {
            boolean hasActivityToday = hasActivityToday(user);
            
            if (hasActivityToday) {
                // User was active today, increment streak
                int newStreak = user.getCurrentStreak() + 1;
                userService.updateStreak(user, newStreak);
                updatedCount++;
            } else {
                // User was not active today, reset streak
                if (user.getCurrentStreak() > 0) {
                    userService.updateStreak(user, 0);
                    updatedCount++;
                }
            }
        }
        
        log.info("Streak update completed. {} users updated", updatedCount);
    }
    
    /**
     * Check if user has activity today
     * This is a simplified check - in production, you might want to check
     * for actual review submissions or other activities
     */
    private boolean hasActivityToday(User user) {
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime tomorrow = today.plusDays(1);
        
        List<UserQuestionReview> reviewsToday = userQuestionReviewRepository
            .findByUserAndCreatedAtBetween(user, today, tomorrow);
        
        return !reviewsToday.isEmpty() || 
               (user.getLastActive() != null && user.getLastActive().isAfter(today));
    }
    
    /**
     * Weekly statistics update - runs every Sunday at 2 AM
     */
    @Scheduled(cron = "0 0 2 * * SUN")
    @Transactional
    public void updateWeeklyStatistics() {
        log.info("Starting weekly statistics update at {}", LocalDateTime.now());
        
        // This could include updating user statistics, generating reports, etc.
        // For now, we'll just log the event
        
        log.info("Weekly statistics update completed");
    }
    
    /**
     * Cleanup old review history - runs monthly
     */
    @Scheduled(cron = "0 0 3 1 * ?")
    @Transactional
    public void cleanupOldReviewHistory() {
        log.info("Starting monthly cleanup at {}", LocalDateTime.now());
        
        // This could include archiving or deleting very old review history
        // For now, we'll just log the event
        
        log.info("Monthly cleanup completed");
    }
}
