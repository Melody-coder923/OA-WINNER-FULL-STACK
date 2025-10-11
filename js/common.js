/* ============================================
   COMMON JAVASCRIPT FUNCTIONS
   通用JavaScript功能，所有页面共享
   ============================================ */

// Constants
const DEMO_MESSAGE = 'This is a demo version! Full functionality requires running the Spring Boot application.\n\nPlease follow the instructions in README.md to install Maven and MySQL, then run the project.';
const NAV_DEMO_MESSAGE = 'This is a demo version! Full functionality requires running the Spring Boot application.';

// Utility Functions
const showNotification = (message, type = 'info') => {
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
        animation: slideIn 0.3s ease;
    `;
    notification.textContent = message;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => {
            if (document.body.contains(notification)) {
                document.body.removeChild(notification);
            }
        }, 300);
    }, 3000);
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
    const link = e.target;
    if (link.getAttribute('href') === '#') {
        e.preventDefault();
        showNotification(NAV_DEMO_MESSAGE, 'error');
    }
};

// Common Event Handlers
const handleButtonClick = (event) => {
    const button = event.target;
    if (!button.onclick && !button.hasAttribute('data-handled')) {
        event.preventDefault();
        showNotification(DEMO_MESSAGE);
    }
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
