package com.leetcode.tracker.repository;

import com.leetcode.tracker.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Question entity
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    /**
     * Find questions by difficulty
     */
    List<Question> findByDifficulty(Question.Difficulty difficulty);
    
    /**
     * Find questions by tag
     */
    @Query("SELECT DISTINCT q FROM Question q JOIN q.tags t WHERE t = :tag")
    List<Question> findByTag(@Param("tag") String tag);
    
    /**
     * Find questions by title containing text
     */
    List<Question> findByTitleContainingIgnoreCase(String title);
    
    /**
     * Find questions by LeetCode number
     */
    Optional<Question> findByLeetcodeNumber(Integer leetcodeNumber);
    
    /**
     * Find questions by multiple tags
     */
    @Query("SELECT DISTINCT q FROM Question q JOIN q.tags t WHERE t IN :tags")
    List<Question> findByTagsIn(@Param("tags") List<String> tags);
    
    /**
     * Find questions by difficulty and tags
     */
    @Query("SELECT DISTINCT q FROM Question q JOIN q.tags t WHERE q.difficulty = :difficulty AND t IN :tags")
    List<Question> findByDifficultyAndTagsIn(@Param("difficulty") Question.Difficulty difficulty, 
                                           @Param("tags") List<String> tags);
    
    /**
     * Search questions by title, difficulty, and tags
     */
    @Query("SELECT DISTINCT q FROM Question q LEFT JOIN q.tags t WHERE " +
           "(:title IS NULL OR LOWER(q.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:difficulty IS NULL OR q.difficulty = :difficulty) AND " +
           "(:tag IS NULL OR t = :tag)")
    List<Question> searchQuestions(@Param("title") String title, 
                                 @Param("difficulty") Question.Difficulty difficulty, 
                                 @Param("tag") String tag);
}
