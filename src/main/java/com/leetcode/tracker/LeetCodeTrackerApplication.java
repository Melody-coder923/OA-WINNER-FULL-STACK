package com.leetcode.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * LeetCode Memory Tracker Application
 * Main Spring Boot application class
 */
@SpringBootApplication
@EnableScheduling
public class LeetCodeTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeetCodeTrackerApplication.class, args);
    }
}
