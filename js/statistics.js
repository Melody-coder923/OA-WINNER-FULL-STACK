/* ============================================
   STATISTICS SPECIFIC JAVASCRIPT
   Statistics page functionality
   ============================================ */

// Ebbinghaus Forgetting Curve LeetCode Review System
class StatisticsCalculator {
    constructor() {
        this.userData = this.loadUserData();
        // Ebbinghaus forgetting curve review intervals (days)
        this.reviewIntervals = Object.freeze([1, 3, 7, 15, 30, 60, 120]);
        this.cache = new Map(); // Performance cache
    }

    // Load user data from localStorage with error handling
    loadUserData() {
        try {
            const stored = localStorage.getItem('userData');
            if (stored) {
                return JSON.parse(stored);
            }
        } catch (error) {
            console.error('Error loading user data:', error);
        }
        
        return this.getDefaultData();
    }

    // Get default data structure
    getDefaultData() {
        return {
            questions: [
                { 
                    id: 1, 
                    title: "Two Sum", 
                    difficulty: "Easy", 
                    tags: ["Array", "Hash Table"], 
                    addedDate: "2024-01-01",
                    nextReviewDate: "2024-01-02",
                    reviewCount: 0,
                    masteredCount: 0,
                    forgotCount: 0
                },
                { 
                    id: 2, 
                    title: "Container With Most Water", 
                    difficulty: "Medium", 
                    tags: ["Array", "Two Pointers"], 
                    addedDate: "2024-01-02",
                    nextReviewDate: "2024-01-03",
                    reviewCount: 0,
                    masteredCount: 0,
                    forgotCount: 0
                },
                { 
                    id: 3, 
                    title: "Reverse Linked List", 
                    difficulty: "Easy", 
                    tags: ["Linked List", "Recursion"], 
                    addedDate: "2024-01-03",
                    nextReviewDate: "2024-01-04",
                    reviewCount: 0,
                    masteredCount: 0,
                    forgotCount: 0
                },
                { 
                    id: 4, 
                    title: "Maximum Subarray", 
                    difficulty: "Medium", 
                    tags: ["Array", "Dynamic Programming"], 
                    addedDate: "2024-01-04",
                    nextReviewDate: "2024-01-05",
                    reviewCount: 0,
                    masteredCount: 0,
                    forgotCount: 0
                },
                { 
                    id: 5, 
                    title: "Binary Tree Inorder Traversal", 
                    difficulty: "Medium", 
                    tags: ["Tree", "Stack"], 
                    addedDate: "2024-01-05",
                    nextReviewDate: "2024-01-06",
                    reviewCount: 0,
                    masteredCount: 0,
                    forgotCount: 0
                }
            ],
            reviews: [
                { questionId: 1, result: "Mastered", date: "2024-01-02", reviewNumber: 1 },
                { questionId: 1, result: "Mastered", date: "2024-01-05", reviewNumber: 2 },
                { questionId: 2, result: "Forgot", date: "2024-01-03", reviewNumber: 1 },
                { questionId: 2, result: "Mastered", date: "2024-01-06", reviewNumber: 2 },
                { questionId: 3, result: "Mastered", date: "2024-01-04", reviewNumber: 1 },
                { questionId: 4, result: "Forgot", date: "2024-01-05", reviewNumber: 1 },
                { questionId: 4, result: "Mastered", date: "2024-01-08", reviewNumber: 2 },
                { questionId: 5, result: "Mastered", date: "2024-01-06", reviewNumber: 1 }
            ]
        };
    }

    // 1. Total Questions - Counts all problems in the user's database
    calculateTotalQuestions() {
        const cacheKey = 'totalQuestions';
        if (this.cache.has(cacheKey)) {
            return this.cache.get(cacheKey);
        }
        
        const result = this.userData?.questions?.length || 0;
        this.cache.set(cacheKey, result);
        return result;
    }

    // 2. Total Reviews - Records all review operations completed by the user
    calculateTotalReviews() {
        const cacheKey = 'totalReviews';
        if (this.cache.has(cacheKey)) {
            return this.cache.get(cacheKey);
        }
        
        const result = this.userData?.reviews?.length || 0;
        this.cache.set(cacheKey, result);
        return result;
    }

    // 3. Mastery Rate - Measures the percentage of problems truly mastered
    calculateMasteryRate() {
        const cacheKey = 'masteryRate';
        if (this.cache.has(cacheKey)) {
            return this.cache.get(cacheKey);
        }
        
        const totalReviews = this.calculateTotalReviews();
        if (totalReviews === 0) {
            this.cache.set(cacheKey, 0);
            return 0;
        }
        
        const masteredCount = this.userData?.reviews?.filter(review => review.result === "Mastered").length || 0;
        const result = Math.round((masteredCount / totalReviews) * 100);
        this.cache.set(cacheKey, result);
        return result;
    }

    // 4. Forgetting Score - Measures the degree of knowledge forgetting
    calculateForgettingScore() {
        const cacheKey = 'forgettingScore';
        if (this.cache.has(cacheKey)) {
            return this.cache.get(cacheKey);
        }
        
        const totalReviews = this.calculateTotalReviews();
        if (totalReviews === 0) {
            this.cache.set(cacheKey, 0);
            return 0;
        }
        
        const forgotCount = this.userData?.reviews?.filter(review => review.result === "Forgot").length || 0;
        const result = Math.round((forgotCount / totalReviews) * 100);
        this.cache.set(cacheKey, result);
        return result;
    }

    // Validate relationship: Mastery Rate + Forgetting Score = 100%
    validateCalculationRelations() {
        const masteryRate = this.calculateMasteryRate();
        const forgettingScore = this.calculateForgettingScore();
        const total = masteryRate + forgettingScore;
        
        console.log(`Validate calculation relationship: Mastery Rate (${masteryRate}%) + Forgetting Score (${forgettingScore}%) = ${total}%`);
        return total === 100;
    }

    // Calculate next review date - Based on Ebbinghaus forgetting curve
    calculateNextReviewDate(currentReviewCount, result, currentDate = new Date()) {
        let nextInterval;
        
        if (result === "Mastered") {
            // If mastered, enter the next interval cycle
            if (currentReviewCount >= this.reviewIntervals.length) {
                // Beyond max interval, use the last interval (120 days)
                nextInterval = this.reviewIntervals[this.reviewIntervals.length - 1];
            } else {
                nextInterval = this.reviewIntervals[currentReviewCount];
            }
        } else if (result === "Forgot") {
            // If forgot, reset to 1st review (1 day later)
            nextInterval = this.reviewIntervals[0]; // 1 day
        }
        
        const nextDate = new Date(currentDate);
        nextDate.setDate(nextDate.getDate() + nextInterval);
        return nextDate.toISOString().split('T')[0];
    }

    // Get today's review questions
    getTodayReviewQuestions() {
        const today = new Date().toISOString().split('T')[0];
        
        return this.userData.questions
            .filter(question => question.nextReviewDate <= today)
            .sort((a, b) => {
                // Priority: prioritize questions with more "Forgot" counts, then by due date
                const forgotDiff = b.forgotCount - a.forgotCount;
                if (forgotDiff !== 0) return forgotDiff;
                return new Date(a.nextReviewDate) - new Date(b.nextReviewDate);
            });
    }

    // Update question statistics
    updateQuestionStats(questionId, result) {
        const question = this.userData.questions.find(q => q.id === questionId);
        if (!question) return;

        // Update question statistics
        question.reviewCount++;
        if (result === "Mastered") {
            question.masteredCount++;
        } else if (result === "Forgot") {
            question.forgotCount++;
        }

        // Calculate next review date
        question.nextReviewDate = this.calculateNextReviewDate(question.reviewCount, result);
    }

    // Get all statistics
    getAllStatistics() {
        return {
            totalQuestions: this.calculateTotalQuestions(),
            totalReviews: this.calculateTotalReviews(),
            masteryRate: this.calculateMasteryRate(),
            forgettingScore: this.calculateForgettingScore()
        };
    }

    // Clear cache when data changes
    clearCache() {
        this.cache.clear();
    }

    // Save updated data with error handling
    saveUserData() {
        try {
            localStorage.setItem('userData', JSON.stringify(this.userData));
            this.clearCache(); // Clear cache when data is saved
        } catch (error) {
            console.error('Error saving user data:', error);
            throw new Error('Failed to save data. Storage might be full.');
        }
    }

    // Add new question - Implement logic 1
    addQuestion(question) {
        const newQuestion = {
            id: Date.now(),
            ...question,
            addedDate: new Date().toISOString().split('T')[0],
            nextReviewDate: this.calculateNextReviewDate(0, "Mastered"), // First review after 1 day
            reviewCount: 0,
            masteredCount: 0,
            forgotCount: 0
        };
        
        this.userData.questions.push(newQuestion);
        this.saveUserData();
        
        console.log(`Added question: ${newQuestion.title}, Next review date: ${newQuestion.nextReviewDate}`);
        return newQuestion;
    }

    // Add review record - Implement logic 2
    addReview(questionId, result) {
        const question = this.userData.questions.find(q => q.id === questionId);
        if (!question) {
            console.error(`Question not found: ${questionId}`);
            return;
        }

        // Update question statistics
        this.updateQuestionStats(questionId, result);

        // Add review record
        const review = {
            questionId: questionId,
            result: result,
            date: new Date().toISOString().split('T')[0],
            reviewNumber: question.reviewCount
        };
        
        this.userData.reviews.push(review);
        this.saveUserData();
        
        // Validate calculation relationships
        this.validateCalculationRelations();
        
        console.log(`Review record: Question ${questionId} - ${result}, Next review: ${question.nextReviewDate}`);
        return review;
    }

    // Get Ebbinghaus review interval table
    getReviewIntervalTable() {
        return this.reviewIntervals.map((interval, index) => ({
            reviewNumber: index + 1,
            intervalDays: interval,
            description: index === 0 ? "1st Review" : `${index + 1}th Review`
        }));
    }

    // Get question details (including review history)
    getQuestionDetails(questionId) {
        const question = this.userData.questions.find(q => q.id === questionId);
        if (!question) return null;

        const reviews = this.userData.reviews.filter(r => r.questionId === questionId);
        
        return {
            ...question,
            reviews: reviews,
            isDueToday: question.nextReviewDate <= new Date().toISOString().split('T')[0],
            masteryRate: question.reviewCount > 0 ? Math.round((question.masteredCount / question.reviewCount) * 100) : 0
        };
    }
}

// Global statistics calculator instance
const statsCalculator = new StatisticsCalculator();

// Initialize statistics page
const initializeStatistics = () => {
    try {
        // Load and display statistics
        updateStatisticsDisplay();
        
        // Animate progress bars
        animateProgressBars();
        
        // Add hover effects to stat cards
        addCardHoverEffects();
        
        // Initialize interactive charts if present
        initializeCharts();
        
    } catch (error) {
        console.error('Error initializing statistics:', error);
    }
};

// Update statistics display with calculated values
const updateStatisticsDisplay = () => {
    const stats = statsCalculator.getAllStatistics();
    
    // Update dashboard stats
    updateStatCard('.stat-card:nth-child(1) .stat-number', stats.totalQuestions);
    updateStatCard('.stat-card:nth-child(2) .stat-number', stats.totalReviews);
    updateStatCard('.stat-card:nth-child(3) .stat-number', stats.masteryRate + '%');
    updateStatCard('.stat-card:nth-child(4) .stat-number', stats.forgettingScore + '%');
    
    // Update detailed statistics if on statistics page
    if (document.querySelector('.detailed-stats')) {
        updateDetailedStatistics(stats);
    }
};

// Update individual stat card
const updateStatCard = (selector, value) => {
    const element = document.querySelector(selector);
    if (element) {
        // Animate number change
        const currentValue = parseInt(element.textContent) || 0;
        const targetValue = parseInt(value) || 0;
        
        if (currentValue !== targetValue) {
            animateNumber(element, currentValue, targetValue, 1000);
        }
    }
};

// Animate number changes
const animateNumber = (element, start, end, duration) => {
    const startTime = performance.now();
    const isPercentage = element.textContent.includes('%');
    
    const animate = (currentTime) => {
        const elapsed = currentTime - startTime;
        const progress = Math.min(elapsed / duration, 1);
        
        const current = Math.round(start + (end - start) * progress);
        element.textContent = current + (isPercentage ? '%' : '');
        
        if (progress < 1) {
            requestAnimationFrame(animate);
        }
    };
    
    requestAnimationFrame(animate);
};

// Update detailed statistics
const updateDetailedStatistics = (stats) => {
    const detailedStats = document.querySelector('.detailed-stats');
    if (!detailedStats) return;
    
    // Get today's review questions
    const todayReviewQuestions = statsCalculator.getTodayReviewQuestions();
    
    // Get Ebbinghaus review intervals
    const intervalTable = statsCalculator.getReviewIntervalTable();
    
    // Validate calculation relations
    const isValidRelation = statsCalculator.validateCalculationRelations();
    
    // Create detailed breakdown
    const breakdown = `
        <div class="stat-breakdown">
            <h3>📋 Ebbinghaus Forgetting Curve Analysis</h3>
            
            <!-- Core Statistics -->
            <div class="analysis-card">
                <h4>📊 Core Statistics</h4>
                <div class="calculation-detail">
                    <strong>Total Questions:</strong> ${stats.totalQuestions} problems
                </div>
                <div class="calculation-detail">
                    <strong>Total Reviews:</strong> ${stats.totalReviews} review sessions
                </div>
                <div class="calculation-detail">
                    <strong>Mastery Rate:</strong> ${stats.masteryRate}% (mastery rate)
                </div>
                <div class="calculation-detail">
                    <strong>Forgetting Score:</strong> ${stats.forgettingScore}% (forgetting rate)
                </div>
                <div class="calculation-detail ${isValidRelation ? 'valid' : 'invalid'}">
                    <strong>Validation:</strong> ${stats.masteryRate}% + ${stats.forgettingScore}% = ${stats.masteryRate + stats.forgettingScore}% 
                    ${isValidRelation ? '✅' : '❌'}
                </div>
            </div>
            
            <!-- Today's Review -->
            <div class="analysis-card">
                <h4>📅 Today's Review (${todayReviewQuestions.length} questions)</h4>
                ${todayReviewQuestions.length > 0 ? 
                    todayReviewQuestions.slice(0, 5).map(q => `
                        <div class="calculation-detail">
                            <strong>${q.title}</strong> - Reviewed ${q.reviewCount} times, Forgot ${q.forgotCount} times
                        </div>
                    `).join('') :
                    '<div class="calculation-detail">🎉 No questions to review today!</div>'
                }
                ${todayReviewQuestions.length > 5 ? `<div class="calculation-detail">...and ${todayReviewQuestions.length - 5} more questions</div>` : ''}
            </div>
            
            <!-- Ebbinghaus Interval Table -->
            <div class="analysis-card">
                <h4>🔄 Ebbinghaus Review Intervals</h4>
                <div class="interval-table">
                    ${intervalTable.map(item => `
                        <div class="interval-row">
                            <span class="interval-number">${item.reviewNumber}</span>
                            <span class="interval-days">${item.intervalDays} days</span>
                            <span class="interval-desc">${item.description}</span>
                        </div>
                    `).join('')}
                </div>
            </div>
            
            <!-- Calculation Logic -->
            <div class="analysis-card">
                <h4>🧮 Calculation Logic</h4>
                <div class="calculation-detail">
                    <strong>Total Questions:</strong> Counts all problems added to user's database
                </div>
                <div class="calculation-detail">
                    <strong>Total Reviews:</strong> Each "Mastered" or "Forgot" mark increments counter +1
                </div>
                <div class="calculation-detail">
                    <strong>Mastery Rate:</strong> (Total "Mastered" marks / Total Reviews) × 100%
                </div>
                <div class="calculation-detail">
                    <strong>Forgetting Score:</strong> (Total "Forgot" marks / Total Reviews) × 100%
                </div>
            </div>
        </div>
    `;
    
    detailedStats.innerHTML = breakdown;
};

// Animate progress bars
const animateProgressBars = () => {
    const progressBars = document.querySelectorAll('.progress-fill');
    progressBars.forEach(bar => {
        const width = bar.style.width || bar.dataset.width;
        bar.style.width = '0%';
        setTimeout(() => {
            bar.style.width = width;
        }, 500);
    });
};

// Add hover effects to stat cards
const addCardHoverEffects = () => {
    const statCards = document.querySelectorAll('.stat-card');
    statCards.forEach(card => {
        card.addEventListener('mouseenter', () => {
            card.style.transform = 'translateY(-5px) scale(1.02)';
            card.style.transition = 'transform 0.3s ease';
        });
        
        card.addEventListener('mouseleave', () => {
            card.style.transform = 'translateY(0) scale(1)';
        });
    });
};

// Initialize charts (placeholder for future chart implementation)
const initializeCharts = () => {
    // This would integrate with chart libraries like Chart.js
    console.log('Charts initialized');
};

// Export for use in other files
window.StatisticsCalculator = StatisticsCalculator;
window.statsCalculator = statsCalculator;
window.updateStatisticsDisplay = updateStatisticsDisplay;

// Initialize when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeStatistics);
} else {
    initializeStatistics();
}
