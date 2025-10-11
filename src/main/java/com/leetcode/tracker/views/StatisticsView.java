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
@PageTitle("统计信息 | LeetCode Memory Tracker")
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
        H2 header = new H2("统计信息");
        header.addClassNames(LumoUtility.Margin.Bottom.LARGE);
        add(header);
    }
    
    private void createOverviewStats() {
        H2 sectionTitle = new H2("总体统计");
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
            
            Div totalSolvedCard = createStatCard("已解决题目", String.valueOf(totalSolved), "开始复习的题目数");
            statsLayout.add(totalSolvedCard);
            
            // Total reviews
            Div totalReviewsCard = createStatCard("总复习次数", String.valueOf(reviewStats.totalReviews), "累计复习次数");
            statsLayout.add(totalReviewsCard);
            
            // Current streak
            Div streakCard = createStatCard("当前连续天数", String.valueOf(currentUser.getCurrentStreak()), "连续复习天数");
            statsLayout.add(streakCard);
            
            // Longest streak
            Div longestStreakCard = createStatCard("最长连续天数", String.valueOf(currentUser.getLongestStreak()), "历史最长连续天数");
            statsLayout.add(longestStreakCard);
        }
        
        add(statsLayout);
    }
    
    private void createDifficultyStats() {
        H2 sectionTitle = new H2("难度分布");
        sectionTitle.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(sectionTitle);
        
        HorizontalLayout difficultyLayout = new HorizontalLayout();
        difficultyLayout.setWidthFull();
        difficultyLayout.setSpacing(true);
        
        for (Question.Difficulty difficulty : Question.Difficulty.values()) {
            long count = questionService.getQuestionCountByDifficulty(difficulty);
            Div difficultyCard = createStatCard(difficulty.getDisplayName(), String.valueOf(count), "题目数量");
            difficultyLayout.add(difficultyCard);
        }
        
        add(difficultyLayout);
    }
    
    private void createTagStats() {
        H2 sectionTitle = new H2("标签分布");
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
        H2 sectionTitle = new H2("复习统计");
        sectionTitle.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(sectionTitle);
        
        if (currentUser != null) {
            ReviewService.ReviewStats reviewStats = reviewService.getReviewStats(currentUser);
            
            HorizontalLayout reviewStatsLayout = new HorizontalLayout();
            reviewStatsLayout.setWidthFull();
            reviewStatsLayout.setSpacing(true);
            
            Div forgotCard = createStatCard("忘记", String.valueOf(reviewStats.forgotCount), "忘记次数");
            Div hardCard = createStatCard("困难", String.valueOf(reviewStats.hardCount), "困难次数");
            Div goodCard = createStatCard("良好", String.valueOf(reviewStats.goodCount), "良好次数");
            Div easyCard = createStatCard("简单", String.valueOf(reviewStats.easyCount), "简单次数");
            
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
                
                Span avgTimeText = new Span(String.format("平均复习时间: %.1f 秒", reviewStats.averageTime));
                avgTimeText.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.BOLD);
                
                avgTimeCard.add(avgTimeText);
                add(avgTimeCard);
            }
        }
    }
    
    private void createRecentActivity() {
        H2 sectionTitle = new H2("最近活动");
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
                activityCard.add(new Span("暂无复习记录"));
            } else {
                VerticalLayout activityList = new VerticalLayout();
                activityList.setSpacing(true);
                
                recentReviews.stream()
                    .limit(10)
                    .forEach(review -> {
                        String timeStr = review.getReviewTime().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"));
                        Span activityItem = new Span(
                            String.format("[%s] 复习了 %s - 评价: %s", 
                                timeStr,
                                review.getQuestion().getTitle(),
                                review.getReviewRating().getDisplayName())
                        );
                        activityList.add(activityItem);
                    });
                
                activityCard.add(activityList);
            }
        } else {
            activityCard.add(new Span("请先登录"));
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
