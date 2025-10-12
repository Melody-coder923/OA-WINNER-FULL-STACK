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
@PageTitle("Question List | LeetCode Memory Tracker")
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
        H2 header = new H2("Question List");
        header.addClassNames(LumoUtility.Margin.Bottom.LARGE);
        add(header);
    }
    
    private void createFilters() {
        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setSpacing(true);
        filterLayout.setAlignItems(Alignment.END);
        
        // Search field
        searchField = new TextField("Search Questions");
        searchField.setPlaceholder("Enter question name...");
        searchField.addValueChangeListener(e -> refreshQuestionList());
        
        // Difficulty filter
        difficultyFilter = new Select<>();
        difficultyFilter.setLabel("Difficulty");
        difficultyFilter.setItems(Question.Difficulty.values());
        difficultyFilter.setItemLabelGenerator(Question.Difficulty::getDisplayName);
        difficultyFilter.setPlaceholder("Select difficulty");
        difficultyFilter.addValueChangeListener(e -> refreshQuestionList());
        
        // Tag filter
        tagFilter = new Select<>();
        tagFilter.setLabel("Tags");
        tagFilter.setItems(questionService.getAllTags());
        tagFilter.setPlaceholder("Select tags");
        tagFilter.addValueChangeListener(e -> refreshQuestionList());
        
        // Clear filters button
        Button clearFiltersButton = new Button("Clear Filters", e -> {
            searchField.clear();
            difficultyFilter.clear();
            tagFilter.clear();
            refreshQuestionList();
        });
        
        filterLayout.add(searchField, difficultyFilter, tagFilter, clearFiltersButton);
        add(filterLayout);
    }
    
    private void createAddButton() {
        Button addButton = new Button("Add Question", e -> openAddQuestionDialog());
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
            noQuestionsCard.add(new Span("No matching questions found"));
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
        
        Span difficulty = new Span("Difficulty: " + question.getDifficulty().getDisplayName());
        difficulty.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);
        
        Span tags = new Span("Tags: " + String.join(", ", question.getTags()));
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
            status.setText("Status: " + userReview.getStatus().getDisplayName());
            status.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);
        } else {
            status.setText("Status: Not Started");
            status.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);
        }
        
        // Action buttons
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        
        if (currentUser != null) {
            if (userReview == null) {
                Button startReviewButton = new Button("Start Review", e -> startReview(question));
                buttonLayout.add(startReviewButton);
            } else {
                Button viewProgressButton = new Button("View Progress", e -> viewProgress(userReview));
                buttonLayout.add(viewProgressButton);
            }
        }
        
        if (question.getLeetcodeUrl() != null && !question.getLeetcodeUrl().isEmpty()) {
            Button openLeetCodeButton = new Button("Open LeetCode", e -> {
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
            Notification.show("Please login first", 3000, Notification.Position.MIDDLE);
            return;
        }

        try {
            reviewService.startReview(currentUser, question);
            Notification.show("Started review: " + question.getTitle(), 3000, Notification.Position.MIDDLE);
            refreshQuestionList();
        } catch (Exception e) {
            Notification.show("Failed to start review: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
    
    private void viewProgress(UserQuestionReview review) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
        
        H2 dialogTitle = new H2("Review Progress: " + review.getQuestion().getTitle());
        
        Span status = new Span("Status: " + review.getStatus().getDisplayName());
        Span totalReviews = new Span("Total Reviews: " + review.getTotalReviews());
        Span nextReview = new Span("Next Review: " + 
            (review.getNextReviewTime() != null ? 
                review.getNextReviewTime().toString() : "Not set"));
        
        Button markMasteredButton = new Button("Mark as Mastered", e -> {
            reviewService.markAsMastered(currentUser, review.getQuestion());
            dialog.close();
            refreshQuestionList();
            Notification.show("Marked as mastered", 3000, Notification.Position.MIDDLE);
        });
        
        Button resetButton = new Button("Reset Progress", e -> {
            reviewService.resetReviewProgress(currentUser, review.getQuestion());
            dialog.close();
            refreshQuestionList();
            Notification.show("Progress reset", 3000, Notification.Position.MIDDLE);
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
        
        H2 dialogTitle = new H2("Add New Question");
        
        TextField titleField = new TextField("Question Title");
        titleField.setRequired(true);
        
        Select<Question.Difficulty> difficultySelect = new Select<>();
        difficultySelect.setLabel("Difficulty");
        difficultySelect.setItems(Question.Difficulty.values());
        difficultySelect.setItemLabelGenerator(Question.Difficulty::getDisplayName);
        
        Select<String> tagSelect = new Select<>();
        tagSelect.setLabel("Tags");
        tagSelect.setItems(questionService.getAllTags());
        
        TextField urlField = new TextField("LeetCode URL");
        urlField.setPlaceholder("https://leetcode.cn/problems/...");
        
        TextField numberField = new TextField("Question Number");
        numberField.setPlaceholder("e.g. 1");
        
        Button addButton = new Button("Add", e -> {
            if (titleField.getValue() == null || titleField.getValue().isEmpty()) {
                Notification.show("Please enter question title", 3000, Notification.Position.MIDDLE);
                return;
            }
            
            if (difficultySelect.getValue() == null) {
                Notification.show("Please select difficulty", 3000, Notification.Position.MIDDLE);
                return;
            }
            
            if (tagSelect.getValue() == null) {
                Notification.show("Please select tags", 3000, Notification.Position.MIDDLE);
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
                Notification.show("Question added successfully", 3000, Notification.Position.MIDDLE);
                
            } catch (NumberFormatException ex) {
                Notification.show("Please enter a valid question number", 3000, Notification.Position.MIDDLE);
            } catch (Exception ex) {
                Notification.show("Failed to add: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });
        
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        
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
