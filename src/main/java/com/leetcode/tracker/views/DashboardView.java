package com.leetcode.tracker.views;

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

import java.util.List;

/**
 * Dashboard view showing overview statistics and recent activity
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("仪表板 | LeetCode Memory Tracker")
public class DashboardView extends VerticalLayout {
    
    private final UserService userService;
    private final QuestionService questionService;
    private final ReviewService reviewService;
    
    // For demo purposes, we'll use a default user
    private User currentUser;
    
    public DashboardView(UserService userService, QuestionService questionService, ReviewService reviewService) {
        this.userService = userService;
        this.questionService = questionService;
        this.reviewService = reviewService;
        
        // Initialize demo user
        initializeDemoUser();
        
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        
        createHeader();
        createStatsCards();
        createRecentActivity();
    }
    
    private void initializeDemoUser() {
        // Create a demo user for testing
        try {
            currentUser = userService.createUser("demo", "demo@example.com", "password", "Demo User");
        } catch (Exception e) {
            // User might already exist
            currentUser = userService.findByUsername("demo").orElse(null);
        }
    }
    
    private void createHeader() {
        H2 header = new H2("欢迎回来, " + (currentUser != null ? currentUser.getDisplayName() : "用户"));
        header.addClassNames(LumoUtility.Margin.Bottom.LARGE);
        add(header);
    }
    
    private void createStatsCards() {
        HorizontalLayout statsLayout = new HorizontalLayout();
        statsLayout.setWidthFull();
        statsLayout.setSpacing(true);
        
        // Total Questions Card
        Div totalQuestionsCard = createStatCard("总题目数", 
            String.valueOf(questionService.getAllQuestions().size()), 
            "题目总数");
        statsLayout.add(totalQuestionsCard);
        
        // Solved Questions Card
        if (currentUser != null) {
            List<UserQuestionReview> userReviews = reviewService.getUserReviews(currentUser);
            long solvedCount = userReviews.stream()
                .filter(review -> review.getStatus() == UserQuestionReview.ReviewStatus.IN_PROGRESS || 
                                review.getStatus() == UserQuestionReview.ReviewStatus.MASTERED)
                .count();
            
            Div solvedCard = createStatCard("已解决", String.valueOf(solvedCount), "已解决题目");
            statsLayout.add(solvedCard);
            
            // Due Reviews Card
            List<UserQuestionReview> dueReviews = reviewService.getDueReviews(currentUser);
            Div dueCard = createStatCard("待复习", String.valueOf(dueReviews.size()), "今日待复习");
            statsLayout.add(dueCard);
            
            // Current Streak Card
            Div streakCard = createStatCard("连续天数", 
                String.valueOf(currentUser.getCurrentStreak()), 
                "当前连续天数");
            statsLayout.add(streakCard);
        }
        
        add(statsLayout);
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
    
    private void createRecentActivity() {
        H2 activityHeader = new H2("最近活动");
        activityHeader.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(activityHeader);
        
        Div activityCard = new Div();
        activityCard.addClassNames(
            LumoUtility.Background.BASE,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Padding.LARGE,
            LumoUtility.BoxShadow.SMALL
        );
        
        if (currentUser != null) {
            List<UserQuestionReview> recentReviews = reviewService.getUserReviews(currentUser);
            if (recentReviews.isEmpty()) {
                activityCard.add(new Span("暂无活动记录"));
            } else {
                VerticalLayout activityList = new VerticalLayout();
                activityList.setSpacing(true);
                
                recentReviews.stream()
                    .limit(5)
                    .forEach(review -> {
                        Span activityItem = new Span(
                            String.format("复习了题目: %s (%s)", 
                                review.getQuestion().getTitle(),
                                review.getQuestion().getDifficulty().getDisplayName())
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
}
