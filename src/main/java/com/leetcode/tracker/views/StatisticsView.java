package com.leetcode.tracker.views;

import com.leetcode.tracker.entity.Question;
import com.leetcode.tracker.entity.ReviewHistory;
import com.leetcode.tracker.entity.User;
import com.leetcode.tracker.entity.UserQuestionReview;
import com.leetcode.tracker.service.QuestionService;
import com.leetcode.tracker.service.ReviewService;
import com.leetcode.tracker.service.UserService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Statistics view showing detailed analytics and progress
 */
@Route(value = "statistics", layout = MainLayout.class)
@PageTitle("Statistics | LeetCode Memory Tracker")
public class StatisticsView extends VerticalLayout {
    
    private final UserService userService;
    private final QuestionService questionService;
    private final ReviewService reviewService;
    
    private User currentUser;
    
    public StatisticsView(UserService userService, QuestionService questionService, ReviewService reviewService) {
        this.userService = userService;
        this.questionService = questionService;
        this.reviewService = reviewService;
        
        // Initialize demo user
        initializeDemoUser();
        
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        
        createHeader();
        createOverviewStats();
        createDifficultyStats();
        createTagStats();
        createReviewStats();
        createRecentActivity();
    }
    
    private void initializeDemoUser() {
        currentUser = userService.findByUsername("demo").orElse(null);
    }
    
    private void createHeader() {
        H2 header = new H2("Statistics");
        header.addClassNames(LumoUtility.Margin.Bottom.LARGE);
        add(header);
    }
    
    private void createOverviewStats() {
        H2 sectionTitle = new H2("Overview Statistics");
        sectionTitle.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(sectionTitle);
        
        HorizontalLayout statsLayout = new HorizontalLayout();
        statsLayout.setWidthFull();
        statsLayout.setSpacing(true);
        
        if (currentUser != null) {
            List<UserQuestionReview> userReviews = reviewService.getUserReviews(currentUser);
            ReviewService.ReviewStats reviewStats = reviewService.getReviewStats(currentUser);
            
            // Total solved
            long totalSolved = userReviews.stream()
                .filter(review -> review.getStatus() == UserQuestionReview.ReviewStatus.IN_PROGRESS || 
                                review.getStatus() == UserQuestionReview.ReviewStatus.MASTERED)
                .count();
            
            Div totalSolvedCard = createStatCard("Solved Questions", String.valueOf(totalSolved), "Questions started reviewing");
            statsLayout.add(totalSolvedCard);
            
            // Total reviews
            Div totalReviewsCard = createStatCard("Total Reviews", String.valueOf(reviewStats.totalReviews), "Total review count");
            statsLayout.add(totalReviewsCard);
            
            // Current streak
            Div streakCard = createStatCard("Current Streak", String.valueOf(currentUser.getCurrentStreak()), "Current review streak");
            statsLayout.add(streakCard);
            
            // Longest streak
            Div longestStreakCard = createStatCard("Longest Streak", String.valueOf(currentUser.getLongestStreak()), "Longest review streak");
            statsLayout.add(longestStreakCard);
        }
        
        add(statsLayout);
    }
    
    private void createDifficultyStats() {
        H2 sectionTitle = new H2("Difficulty Distribution");
        sectionTitle.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(sectionTitle);
        
        HorizontalLayout difficultyLayout = new HorizontalLayout();
        difficultyLayout.setWidthFull();
        difficultyLayout.setSpacing(true);
        
        for (Question.Difficulty difficulty : Question.Difficulty.values()) {
            long count = questionService.getQuestionCountByDifficulty(difficulty);
            Div difficultyCard = createStatCard(difficulty.getDisplayName(), String.valueOf(count), "Question count");
            difficultyLayout.add(difficultyCard);
        }
        
        add(difficultyLayout);
    }
    
    private void createTagStats() {
        H2 sectionTitle = new H2("Tag Distribution");
        sectionTitle.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(sectionTitle);
        
        Div tagStatsCard = new Div();
        tagStatsCard.addClassNames(
            LumoUtility.Background.BASE,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Padding.LARGE,
            LumoUtility.BoxShadow.SMALL
        );
        
        List<String> allTags = questionService.getAllTags();
        Map<String, Long> tagCounts = allTags.stream()
            .collect(Collectors.toMap(
                tag -> tag,
                tag -> (long) questionService.findByTag(tag).size()
            ));
        
        VerticalLayout tagList = new VerticalLayout();
        tagList.setSpacing(true);
        
        for (Map.Entry<String, Long> entry : tagCounts.entrySet()) {
            HorizontalLayout tagRow = new HorizontalLayout();
            tagRow.setWidthFull();
            tagRow.setJustifyContentMode(JustifyContentMode.BETWEEN);
            
            Span tagName = new Span(entry.getKey());
            Span tagCount = new Span(String.valueOf(entry.getValue()));
            tagCount.addClassNames(LumoUtility.FontWeight.BOLD);
            
            tagRow.add(tagName, tagCount);
            tagList.add(tagRow);
        }
        
        tagStatsCard.add(tagList);
        add(tagStatsCard);
    }
    
    private void createReviewStats() {
        H2 sectionTitle = new H2("Review Statistics");
        sectionTitle.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(sectionTitle);
        
        if (currentUser != null) {
            ReviewService.ReviewStats reviewStats = reviewService.getReviewStats(currentUser);
            
            HorizontalLayout reviewStatsLayout = new HorizontalLayout();
            reviewStatsLayout.setWidthFull();
            reviewStatsLayout.setSpacing(true);
            
            Div forgotCard = createStatCard("Forgot", String.valueOf(reviewStats.forgotCount), "Forgot count");
            Div hardCard = createStatCard("Hard", String.valueOf(reviewStats.hardCount), "Hard count");
            Div goodCard = createStatCard("Good", String.valueOf(reviewStats.goodCount), "Good count");
            Div easyCard = createStatCard("Easy", String.valueOf(reviewStats.easyCount), "Easy count");
            
            reviewStatsLayout.add(forgotCard, hardCard, goodCard, easyCard);
            add(reviewStatsLayout);
            
            // Average time
            if (reviewStats.averageTime != null) {
                Div avgTimeCard = new Div();
                avgTimeCard.addClassNames(
                    LumoUtility.Background.BASE,
                    LumoUtility.BorderRadius.MEDIUM,
                    LumoUtility.Padding.LARGE,
                    LumoUtility.BoxShadow.SMALL
                );
                
                Span avgTimeText = new Span(String.format("Average review time: %.1f seconds", reviewStats.averageTime));
                avgTimeText.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.BOLD);
                
                avgTimeCard.add(avgTimeText);
                add(avgTimeCard);
            }
        }
    }
    
    private void createRecentActivity() {
        H2 sectionTitle = new H2("Recent Activity");
        sectionTitle.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(sectionTitle);
        
        Div activityCard = new Div();
        activityCard.addClassNames(
            LumoUtility.Background.BASE,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Padding.LARGE,
            LumoUtility.BoxShadow.SMALL
        );
        
        if (currentUser != null) {
            List<ReviewHistory> recentReviews = reviewService.getRecentReviewHistory(currentUser);
            
            if (recentReviews.isEmpty()) {
                activityCard.add(new Span("No review records"));
            } else {
                VerticalLayout activityList = new VerticalLayout();
                activityList.setSpacing(true);
                
                recentReviews.stream()
                    .limit(10)
                    .forEach(review -> {
                        String timeStr = review.getReviewTime().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"));
                        Span activityItem = new Span(
                            String.format("[%s] Reviewed %s - Rating: %s", 
                                timeStr,
                                review.getQuestion().getTitle(),
                                review.getReviewRating().getDisplayName())
                        );
                        activityList.add(activityItem);
                    });
                
                activityCard.add(activityList);
            }
        } else {
            activityCard.add(new Span("Please login first"));
        }
        
        add(activityCard);
    }
    
    private Div createStatCard(String title, String value, String description) {
        Div card = new Div();
        card.addClassNames(
            LumoUtility.Background.BASE,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Padding.LARGE,
            LumoUtility.BoxShadow.SMALL
        );
        card.setWidth("200px");
        
        Span titleSpan = new Span(title);
        titleSpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);
        
        Span valueSpan = new Span(value);
        valueSpan.addClassNames(LumoUtility.FontSize.XXXLARGE, LumoUtility.FontWeight.BOLD);
        
        Span descSpan = new Span(description);
        descSpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);
        
        VerticalLayout cardContent = new VerticalLayout(titleSpan, valueSpan, descSpan);
        cardContent.setSpacing(false);
        cardContent.setPadding(false);
        cardContent.setAlignItems(Alignment.START);
        
        card.add(cardContent);
        return card;
    }
}
