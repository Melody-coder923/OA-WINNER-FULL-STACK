package com.leetcode.tracker.views;

import com.leetcode.tracker.entity.ReviewHistory;
import com.leetcode.tracker.entity.User;
import com.leetcode.tracker.entity.UserQuestionReview;
import com.leetcode.tracker.service.ReviewService;
import com.leetcode.tracker.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Today's review view showing due questions for review
 */
@Route(value = "today-review", layout = MainLayout.class)
@PageTitle("今日复习 | LeetCode Memory Tracker")
public class TodayReviewView extends VerticalLayout {
    
    private final ReviewService reviewService;
    private final UserService userService;
    
    private User currentUser;
    private VerticalLayout reviewList;
    
    public TodayReviewView(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
        
        // Initialize demo user
        initializeDemoUser();
        
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        
        createHeader();
        createReviewList();
    }
    
    private void initializeDemoUser() {
        currentUser = userService.findByUsername("demo").orElse(null);
    }
    
    private void createHeader() {
        H2 header = new H2("今日复习");
        header.addClassNames(LumoUtility.Margin.Bottom.LARGE);
        add(header);
    }
    
    private void createReviewList() {
        reviewList = new VerticalLayout();
        reviewList.setSpacing(true);
        reviewList.setWidthFull();
        
        if (currentUser == null) {
            reviewList.add(new Span("请先登录"));
            add(reviewList);
            return;
        }
        
        List<UserQuestionReview> dueReviews = reviewService.getDueReviews(currentUser);
        
        if (dueReviews.isEmpty()) {
            Div noReviewsCard = new Div();
            noReviewsCard.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Padding.LARGE,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.TextAlignment.CENTER
            );
            noReviewsCard.add(new Span("🎉 太棒了！今天没有需要复习的题目"));
            reviewList.add(noReviewsCard);
        } else {
            for (UserQuestionReview review : dueReviews) {
                reviewList.add(createReviewCard(review));
            }
        }
        
        add(reviewList);
    }
    
    private Div createReviewCard(UserQuestionReview review) {
        Div card = new Div();
        card.addClassNames(
            LumoUtility.Background.BASE,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Padding.LARGE,
            LumoUtility.BoxShadow.SMALL
        );
        
        // Question info
        Span title = new Span(review.getQuestion().getTitle());
        title.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.BOLD);
        
        Span difficulty = new Span("难度: " + review.getQuestion().getDifficulty().getDisplayName());
        difficulty.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);
        
        Span tags = new Span("标签: " + String.join(", ", review.getQuestion().getTags()));
        tags.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);
        
        Span nextReview = new Span("下次复习: " + 
            (review.getNextReviewTime() != null ? 
                review.getNextReviewTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : 
                "未设置"));
        nextReview.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);
        
        // Review button
        Button reviewButton = new Button("开始复习", e -> openReviewDialog(review));
        reviewButton.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        
        VerticalLayout cardContent = new VerticalLayout(title, difficulty, tags, nextReview, reviewButton);
        cardContent.setSpacing(false);
        cardContent.setPadding(false);
        
        card.add(cardContent);
        return card;
    }
    
    private void openReviewDialog(UserQuestionReview review) {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");
        
        H2 dialogTitle = new H2("复习: " + review.getQuestion().getTitle());
        
        // Rating selection
        RadioButtonGroup<ReviewHistory.ReviewRating> ratingGroup = new RadioButtonGroup<>();
        ratingGroup.setLabel("复习评价");
        ratingGroup.setItems(ReviewHistory.ReviewRating.values());
        ratingGroup.setItemLabelGenerator(ReviewHistory.ReviewRating::getDisplayName);
        
        // Time spent
        TextField timeSpentField = new TextField("花费时间 (秒)");
        timeSpentField.setPlaceholder("例如: 300");
        
        // Notes
        TextArea notesField = new TextArea("复习笔记");
        notesField.setPlaceholder("记录你的思路、解法或需要注意的地方...");
        notesField.setHeight("100px");
        
        // Buttons
        Button submitButton = new Button("提交复习", e -> {
            submitReview(review, ratingGroup.getValue(), timeSpentField.getValue(), notesField.getValue());
            dialog.close();
        });
        submitButton.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        
        Button cancelButton = new Button("取消", e -> dialog.close());
        
        HorizontalLayout buttonLayout = new HorizontalLayout(submitButton, cancelButton);
        buttonLayout.setSpacing(true);
        
        FormLayout formLayout = new FormLayout(ratingGroup, timeSpentField, notesField, buttonLayout);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        
        VerticalLayout dialogContent = new VerticalLayout(dialogTitle, formLayout);
        dialogContent.setSpacing(true);
        dialogContent.setPadding(false);
        
        dialog.add(dialogContent);
        dialog.open();
    }
    
    private void submitReview(UserQuestionReview review, ReviewHistory.ReviewRating rating, 
                            String timeSpentStr, String notes) {
        if (rating == null) {
            Notification.show("请选择复习评价", 3000, Notification.Position.MIDDLE);
            return;
        }
        
        try {
            Integer timeSpent = timeSpentStr != null && !timeSpentStr.isEmpty() ? 
                Integer.parseInt(timeSpentStr) : null;
            
            reviewService.submitReview(currentUser, review.getQuestion(), rating, timeSpent, notes);
            
            Notification.show("复习提交成功！", 3000, Notification.Position.MIDDLE);
            
            // Refresh the review list
            reviewList.removeAll();
            createReviewList();
            
        } catch (NumberFormatException e) {
            Notification.show("请输入有效的时间（数字）", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("提交失败: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
