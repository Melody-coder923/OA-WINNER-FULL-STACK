package com.leetcode.tracker.service;

import com.leetcode.tracker.entity.Question;
import com.leetcode.tracker.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for Question entity operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;

    /**
     * Create a new question
     */
    @Transactional
    public Question createQuestion(String title, Question.Difficulty difficulty,
            List<String> tags, String leetcodeUrl,
            Integer leetcodeNumber, String description) {

        Question question = Question.builder()
                .title(title)
                .difficulty(difficulty)
                .tags(tags)
                .leetcodeUrl(leetcodeUrl)
                .leetcodeNumber(leetcodeNumber)
                .description(description)
                .build();

        Question savedQuestion = questionRepository.save(question);
        log.info("Created new question: {}", title);
        return savedQuestion;
    }

    /**
     * Find question by ID
     */
    public Optional<Question> findById(Long id) {
        return questionRepository.findById(id);
    }

    /**
     * Find questions by difficulty
     */
    public List<Question> findByDifficulty(Question.Difficulty difficulty) {
        return questionRepository.findByDifficulty(difficulty);
    }

    /**
     * Find questions by tag
     */
    public List<Question> findByTag(String tag) {
        return questionRepository.findByTag(tag);
    }

    /**
     * Search questions by title
     */
    public List<Question> searchByTitle(String title) {
        return questionRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Find question by LeetCode number
     */
    public Optional<Question> findByLeetcodeNumber(Integer leetcodeNumber) {
        return questionRepository.findByLeetcodeNumber(leetcodeNumber);
    }

    /**
     * Search questions with filters
     */
    public List<Question> searchQuestions(String title, Question.Difficulty difficulty, String tag) {
        return questionRepository.searchQuestions(title, difficulty, tag);
    }

    /**
     * Get all questions
     */
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    /**
     * Update question
     */
    @Transactional
    public Question updateQuestion(Question question) {
        Question savedQuestion = questionRepository.save(question);
        log.info("Updated question: {}", question.getTitle());
        return savedQuestion;
    }

    /**
     * Delete question
     */
    @Transactional
    public void deleteQuestion(Long questionId) {
        questionRepository.deleteById(questionId);
        log.info("Deleted question with ID: {}", questionId);
    }

    /**
     * Get all available tags
     */
    public List<String> getAllTags() {
        return List.of(
                "Array", "String", "Linked List", "Tree", "Graph", "Dynamic Programming", "Backtracking",
                "Greedy", "Math", "Sorting", "Search", "Other");
    }

    /**
     * Get difficulty statistics
     */
    public long getQuestionCountByDifficulty(Question.Difficulty difficulty) {
        return questionRepository.findByDifficulty(difficulty).size();
    }
}
