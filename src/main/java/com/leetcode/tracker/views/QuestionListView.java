package com.leetcode.tracker.views;

import com.leetcode.tracker.entity.Question;
import com.leetcode.tracker.entity.User;
import com.leetcode.tracker.entity.UserQuestionReview;
import com.leetcode.tracker.service.QuestionService;
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
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;

/**
 * Question list view for managing LeetCode problems
 */
@Route(value = "questions", layout = MainLayout.class)
@PageTitle("题目列表 | LeetCode Memory Tracker")
public class QuestionListView extends VerticalLayout {
    
    private final QuestionService questionService;
    private final ReviewService reviewService;
    private final UserService userService;
    
    private User currentUser;
    private VerticalLayout questionList;
    private TextField searchField;
    private Select<Question.Difficulty> difficultyFilter;
    private Select<String> tagFilter;
    
    public QuestionListView(QuestionService questionService, ReviewService reviewService, UserService userService) {
        this.questionService = questionService;
        this.reviewService = reviewService;
        this.userService = userService;
        
        // Initialize demo user
        initializeDemoUser();
        
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        
        createHeader();
        createFilters();
        createAddButton();
        createQuestionList();
    }
    
    private void initializeDemoUser() {
        currentUser = userService.findByUsername("demo").orElse(null);
    }
    
    private void createHeader() {
        H2 header = new H2("题目列表");
        header.addClassNames(LumoUtility.Margin.Bottom.LARGE);
        add(header);
    }
    
    private void createFilters() {
        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setSpacing(true);
        filterLayout.setAlignItems(Alignment.END);
        
        // Search field
        searchField = new TextField("搜索题目");
        searchField.setPlaceholder("输入题目名称...");
        searchField.addValueChangeListener(e -> refreshQuestionList());
        
        // Difficulty filter
        difficultyFilter = new Select<>();
        difficultyFilter.setLabel("难度");
        difficultyFilter.setItems(Question.Difficulty.values());
        difficultyFilter.setItemLabelGenerator(Question.Difficulty::getDisplayName);
        difficultyFilter.setPlaceholder("选择难度");
        difficultyFilter.addValueChangeListener(e -> refreshQuestionList());
        
        // Tag filter
        tagFilter = new Select<>();
        tagFilter.setLabel("标签");
        tagFilter.setItems(questionService.getAllTags());
        tagFilter.setPlaceholder("选择标签");
        tagFilter.addValueChangeListener(e -> refreshQuestionList());
        
        // Clear filters button
        Button clearFiltersButton = new Button("清除筛选", e -> {
            searchField.clear();
            difficultyFilter.clear();
            tagFilter.clear();
            refreshQuestionList();
        });
        
        filterLayout.add(searchField, difficultyFilter, tagFilter, clearFiltersButton);
        add(filterLayout);
    }
    
    private void createAddButton() {
        Button addButton = new Button("添加题目", e -> openAddQuestionDialog());
        addButton.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        add(addButton);
    }
    
    private void createQuestionList() {
        questionList = new VerticalLayout();
        questionList.setSpacing(true);
        questionList.setWidthFull();
        
        refreshQuestionList();
        add(questionList);
    }
    
    private void refreshQuestionList() {
        questionList.removeAll();
        
        List<Question> questions = questionService.searchQuestions(
            searchField.getValue(),
            difficultyFilter.getValue(),
            tagFilter.getValue()
        );
        
        if (questions.isEmpty()) {
            Div noQuestionsCard = new Div();
            noQuestionsCard.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Padding.LARGE,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.TextAlignment.CENTER
            );
            noQuestionsCard.add(new Span("没有找到匹配的题目"));
            questionList.add(noQuestionsCard);
        } else {
            for (Question question : questions) {
                questionList.add(createQuestionCard(question));
            }
        }
    }
    
    private Div createQuestionCard(Question question) {
        Div card = new Div();
        card.addClassNames(
            LumoUtility.Background.BASE,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Padding.LARGE,
            LumoUtility.BoxShadow.SMALL
        );
        
        // Question info
        Span title = new Span(question.getTitle());
        title.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.BOLD);
        
        Span difficulty = new Span("难度: " + question.getDifficulty().getDisplayName());
        difficulty.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);
        
        Span tags = new Span("标签: " + String.join(", ", question.getTags()));
        tags.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);
        
        // Check if user has started reviewing this question
        final UserQuestionReview userReview;
        if (currentUser != null) {
            userReview = reviewService.getUserReviews(currentUser).stream()
                .filter(review -> review.getQuestion().getId().equals(question.getId()))
                .findFirst()
                .orElse(null);
        } else {
            userReview = null;
        }
        
        Span status = new Span();
        if (userReview != null) {
            status.setText("状态: " + userReview.getStatus().getDisplayName());
            status.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);
        } else {
            status.setText("状态: 未开始");
            status.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);
        }
        
        // Action buttons
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        
        if (currentUser != null) {
            if (userReview == null) {
                Button startReviewButton = new Button("开始复习", e -> startReview(question));
                buttonLayout.add(startReviewButton);
            } else {
                Button viewProgressButton = new Button("查看进度", e -> viewProgress(userReview));
                buttonLayout.add(viewProgressButton);
            }
        }
        
        if (question.getLeetcodeUrl() != null && !question.getLeetcodeUrl().isEmpty()) {
            Button openLeetCodeButton = new Button("打开LeetCode", e -> {
                getUI().ifPresent(ui -> ui.getPage().open(question.getLeetcodeUrl(), "_blank"));
            });
            buttonLayout.add(openLeetCodeButton);
        }
        
        VerticalLayout cardContent = new VerticalLayout(title, difficulty, tags, status, buttonLayout);
        cardContent.setSpacing(false);
        cardContent.setPadding(false);
        
        card.add(cardContent);
        return card;
    }
    
    private void startReview(Question question) {
        if (currentUser == null) {
            Notification.show("请先登录", 3000, Notification.Position.MIDDLE);
            return;
        }
        
        try {
            reviewService.startReview(currentUser, question);
            Notification.show("已开始复习: " + question.getTitle(), 3000, Notification.Position.MIDDLE);
            refreshQuestionList();
        } catch (Exception e) {
            Notification.show("开始复习失败: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
    
    private void viewProgress(UserQuestionReview review) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
        
        H2 dialogTitle = new H2("复习进度: " + review.getQuestion().getTitle());
        
        Span status = new Span("状态: " + review.getStatus().getDisplayName());
        Span totalReviews = new Span("总复习次数: " + review.getTotalReviews());
        Span nextReview = new Span("下次复习: " + 
            (review.getNextReviewTime() != null ? 
                review.getNextReviewTime().toString() : "未设置"));
        
        Button markMasteredButton = new Button("标记为已掌握", e -> {
            reviewService.markAsMastered(currentUser, review.getQuestion());
            dialog.close();
            refreshQuestionList();
            Notification.show("已标记为掌握", 3000, Notification.Position.MIDDLE);
        });
        
        Button resetButton = new Button("重置进度", e -> {
            reviewService.resetReviewProgress(currentUser, review.getQuestion());
            dialog.close();
            refreshQuestionList();
            Notification.show("已重置进度", 3000, Notification.Position.MIDDLE);
        });
        
        HorizontalLayout buttonLayout = new HorizontalLayout(markMasteredButton, resetButton);
        buttonLayout.setSpacing(true);
        
        VerticalLayout dialogContent = new VerticalLayout(dialogTitle, status, totalReviews, nextReview, buttonLayout);
        dialogContent.setSpacing(true);
        dialogContent.setPadding(false);
        
        dialog.add(dialogContent);
        dialog.open();
    }
    
    private void openAddQuestionDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");
        
        H2 dialogTitle = new H2("添加新题目");
        
        TextField titleField = new TextField("题目名称");
        titleField.setRequired(true);
        
        Select<Question.Difficulty> difficultySelect = new Select<>();
        difficultySelect.setLabel("难度");
        difficultySelect.setItems(Question.Difficulty.values());
        difficultySelect.setItemLabelGenerator(Question.Difficulty::getDisplayName);
        
        Select<String> tagSelect = new Select<>();
        tagSelect.setLabel("标签");
        tagSelect.setItems(questionService.getAllTags());
        
        TextField urlField = new TextField("LeetCode链接");
        urlField.setPlaceholder("https://leetcode.cn/problems/...");
        
        TextField numberField = new TextField("题目编号");
        numberField.setPlaceholder("例如: 1");
        
        Button addButton = new Button("添加", e -> {
            if (titleField.getValue() == null || titleField.getValue().isEmpty()) {
                Notification.show("请输入题目名称", 3000, Notification.Position.MIDDLE);
                return;
            }
            
            if (difficultySelect.getValue() == null) {
                Notification.show("请选择难度", 3000, Notification.Position.MIDDLE);
                return;
            }
            
            if (tagSelect.getValue() == null) {
                Notification.show("请选择标签", 3000, Notification.Position.MIDDLE);
                return;
            }
            
            try {
                Integer leetcodeNumber = numberField.getValue() != null && !numberField.getValue().isEmpty() ? 
                    Integer.parseInt(numberField.getValue()) : null;
                
                questionService.createQuestion(
                    titleField.getValue(),
                    difficultySelect.getValue(),
                    List.of(tagSelect.getValue()),
                    urlField.getValue(),
                    leetcodeNumber,
                    null
                );
                
                dialog.close();
                refreshQuestionList();
                Notification.show("题目添加成功", 3000, Notification.Position.MIDDLE);
                
            } catch (NumberFormatException ex) {
                Notification.show("请输入有效的题目编号", 3000, Notification.Position.MIDDLE);
            } catch (Exception ex) {
                Notification.show("添加失败: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });
        
        Button cancelButton = new Button("取消", e -> dialog.close());
        
        HorizontalLayout buttonLayout = new HorizontalLayout(addButton, cancelButton);
        buttonLayout.setSpacing(true);
        
        FormLayout formLayout = new FormLayout(titleField, difficultySelect, tagSelect, urlField, numberField, buttonLayout);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        
        VerticalLayout dialogContent = new VerticalLayout(dialogTitle, formLayout);
        dialogContent.setSpacing(true);
        dialogContent.setPadding(false);
        
        dialog.add(dialogContent);
        dialog.open();
    }
}
