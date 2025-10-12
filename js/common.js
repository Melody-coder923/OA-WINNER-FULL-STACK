/* ============================================
   COMMON JAVASCRIPT FUNCTIONS
   Shared functionality across all pages
   ============================================ */

// Constants and Configuration
const CONFIG = {
    DEMO_MESSAGE: 'This is a demo version! Full functionality requires running the Spring Boot application.\n\nPlease follow the instructions in README.md to install Maven and MySQL, then run the project.',
    NAV_DEMO_MESSAGE: 'This is a demo version! Full functionality requires running the Spring Boot application.',
    NOTIFICATION_DURATION: 3000,
    ANIMATION_DURATION: 300
};

// Performance optimization: Cache DOM elements
const DOMCache = new Map();

// Utility Functions
const showNotification = (message, type = 'info') => {
    try {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            background: var(--primary-color);
            color: white;
            padding: var(--spacing-md);
            border-radius: var(--border-radius);
            box-shadow: var(--shadow-medium);
            z-index: 1000;
            max-width: 300px;
            animation: slideIn ${CONFIG.ANIMATION_DURATION}ms ease;
        `;
        notification.textContent = message;
        
        document.body.appendChild(notification);
        
        setTimeout(() => {
            notification.style.animation = `slideOut ${CONFIG.ANIMATION_DURATION}ms ease`;
            setTimeout(() => {
                if (document.body.contains(notification)) {
                    document.body.removeChild(notification);
                }
            }, CONFIG.ANIMATION_DURATION);
        }, CONFIG.NOTIFICATION_DURATION);
    } catch (error) {
        console.error('Error showing notification:', error);
    }
};

const setLoadingState = (button, isLoading) => {
    if (isLoading) {
        button.classList.add('loading');
        button.disabled = true;
        button.dataset.originalText = button.textContent;
        button.textContent = 'Loading...';
    } else {
        button.classList.remove('loading');
        button.disabled = false;
        button.textContent = button.dataset.originalText;
    }
};

const escapeHtml = (text) => {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
};

// Navigation Functions
const handleNavigation = (e) => {
    try {
        const link = e.target;
        if (link.getAttribute('href') === '#') {
            e.preventDefault();
            showNotification(CONFIG.NAV_DEMO_MESSAGE, 'error');
        }
    } catch (error) {
        console.error('Navigation error:', error);
    }
};

// Common Event Handlers
const handleButtonClick = (event) => {
    try {
        const button = event.target;
        if (!button.onclick && !button.hasAttribute('data-handled')) {
            event.preventDefault();
            showNotification(CONFIG.DEMO_MESSAGE);
        }
    } catch (error) {
        console.error('Button click error:', error);
    }
};

// Dashboard Statistics Functions
const initializeDashboardStats = () => {
    // Only run on dashboard page
    if (!document.querySelector('.stats-grid')) return;
    
    try {
        // Load statistics calculator if available
        if (typeof statsCalculator !== 'undefined') {
            updateDashboardStatistics();
        } else {
            // Fallback: load statistics.js dynamically
            const script = document.createElement('script');
            script.src = 'js/statistics.js';
            script.onload = () => {
                updateDashboardStatistics();
            };
            document.head.appendChild(script);
        }
    } catch (error) {
        console.error('Error initializing dashboard stats:', error);
    }
};

const updateDashboardStatistics = () => {
    if (typeof statsCalculator === 'undefined') return;
    
    const stats = statsCalculator.getAllStatistics();
    
    // Update dashboard stat cards
    const statCards = document.querySelectorAll('.stat-card .stat-number');
    if (statCards.length >= 4) {
        statCards[0].textContent = stats.totalQuestions;
        statCards[1].textContent = stats.totalReviews;
        statCards[2].textContent = stats.masteryRate + '%';
        statCards[3].textContent = stats.forgettingScore + '%';
        
        // Animate the numbers
        statCards.forEach((card, index) => {
            const values = [stats.totalQuestions, stats.totalReviews, stats.masteryRate, stats.forgettingScore];
            animateStatNumber(card, values[index], index === 2 || index === 3);
        });
    }
};

const animateStatNumber = (element, targetValue, isPercentage = false) => {
    const startValue = 0;
    const duration = 1500;
    const startTime = performance.now();
    
    const animate = (currentTime) => {
        const elapsed = currentTime - startTime;
        const progress = Math.min(elapsed / duration, 1);
        
        // Easing function for smooth animation
        const easeOutQuart = 1 - Math.pow(1 - progress, 4);
        const currentValue = Math.round(startValue + (targetValue - startValue) * easeOutQuart);
        
        element.textContent = currentValue + (isPercentage ? '%' : '');
        
        if (progress < 1) {
            requestAnimationFrame(animate);
        }
    };
    
    requestAnimationFrame(animate);
};

// Initialize Common Features
const initializeCommonFeatures = () => {
    try {
        // Add smooth scrolling for anchor links
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                e.preventDefault();
                const target = document.querySelector(this.getAttribute('href'));
                if (target) {
                    target.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            });
        });
        
        // Add navigation event listeners
        document.addEventListener('click', (e) => {
            if (e.target.classList.contains('nav') || e.target.closest('.nav a')) {
                handleNavigation(e);
            }
        });
        
        // Add button event delegation
        document.addEventListener('click', (e) => {
            if (e.target.classList.contains('btn') && !e.target.onclick) {
                handleButtonClick(e);
            }
        });
        
        // Initialize dashboard statistics
        initializeDashboardStats();
        
        // Performance monitoring
        if ('performance' in window) {
            window.addEventListener('load', () => {
                const loadTime = performance.timing.loadEventEnd - performance.timing.navigationStart;
                console.log(`Page loaded in ${loadTime}ms`);
            });
        }
        
    } catch (error) {
        console.error('Error initializing common features:', error);
    }
};

// Initialize when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeCommonFeatures);
} else {
    initializeCommonFeatures();
}
