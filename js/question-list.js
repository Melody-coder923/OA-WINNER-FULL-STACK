/* ============================================
   QUESTION LIST SPECIFIC JAVASCRIPT
   Question list page functionality
   ============================================ */

// Constants
const STORAGE_KEY = 'leetcode_questions';

// Form validation
const validateForm = (formData) => {
    const errors = [];
    
    if (!formData.get('problemNumber').trim()) {
        errors.push('Problem number is required');
    }
    
    if (!formData.get('title').trim()) {
        errors.push('Title is required');
    }
    
    if (!formData.get('difficulty')) {
        errors.push('Difficulty is required');
    }
    
    return errors;
};

// Local storage functions
const saveToStorage = (questions) => {
    try {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(questions));
    } catch (error) {
        console.error('Failed to save to localStorage:', error);
    }
};

const loadFromStorage = () => {
    try {
        const stored = localStorage.getItem(STORAGE_KEY);
        return stored ? JSON.parse(stored) : [];
    } catch (error) {
        console.error('Failed to load from localStorage:', error);
        return [];
    }
};

// Form submission handling
const handleFormSubmit = (e) => {
    e.preventDefault();
    
    const form = e.target;
    const formData = new FormData(form);
    
    // Validate form
    const errors = validateForm(formData);
    if (errors.length > 0) {
        showNotification(errors.join(', '), 'error');
        return;
    }
    
    const submitButton = form.querySelector('button[type="submit"]');
    setLoadingState(submitButton, true);
    
    // Simulate API call delay
    setTimeout(() => {
        const question = {
            id: Date.now(),
            number: formData.get('problemNumber').trim(),
            title: formData.get('title').trim(),
            difficulty: formData.get('difficulty'),
            tags: formData.get('tags').split(',').map(tag => tag.trim()).filter(tag => tag),
            createdAt: new Date().toISOString()
        };
        
        // Add to question list
        addQuestionToList(question);
        
        // Reset form
        form.reset();
        
        // Remove loading state
        setLoadingState(submitButton, false);
        
        // Show success message
        showNotification('Question added successfully!');
    }, 500);
};

// Add question to list
const addQuestionToList = (question) => {
    const questionList = document.getElementById('questionList');
    
    // Remove "no questions" message if it exists
    const noQuestionsMsg = questionList.querySelector('p');
    if (noQuestionsMsg && noQuestionsMsg.textContent.includes('No questions added yet')) {
        noQuestionsMsg.remove();
    }
    
    // Create question card
    const questionCard = document.createElement('div');
    questionCard.className = 'card';
    questionCard.dataset.questionId = question.id;
    
    const difficultyColors = {
        'easy': { bg: '#e8f5e8', color: '#2e7d32' },
        'medium': { bg: '#fff3e0', color: '#f57c00' },
        'hard': { bg: '#ffebee', color: '#d32f2f' }
    };
    
    const difficulty = difficultyColors[question.difficulty] || difficultyColors.easy;
    const tagsDisplay = question.tags.length > 0 ? 
        `Tags: ${question.tags.join(', ')}` : 
        'No tags specified';
    
    questionCard.innerHTML = `
        <div style="font-weight: 600; margin-bottom: var(--spacing-sm); color: var(--text-color);">
            ${escapeHtml(question.number)} - ${escapeHtml(question.title)}
        </div>
        <div style="color: var(--text-secondary); font-size: 0.9rem; margin-bottom: var(--spacing-md);">
            <span style="
                display: inline-block;
                padding: var(--spacing-xs) var(--spacing-sm);
                border-radius: var(--border-radius);
                font-size: 0.8rem;
                font-weight: 500;
                background: ${difficulty.bg};
                color: ${difficulty.color};
                margin-right: var(--spacing-md);
            ">${question.difficulty.charAt(0).toUpperCase() + question.difficulty.slice(1)}</span>
            ${escapeHtml(tagsDisplay)}
        </div>
        <div style="display: flex; gap: var(--spacing-sm);">
            <button class="btn btn-primary start-review-btn" style="flex: 1; padding: var(--spacing-sm); font-size: 0.9rem;" data-question-id="${question.id}">
                Start Review
            </button>
            <button class="btn btn-secondary remove-btn" style="flex: 1; padding: var(--spacing-sm); font-size: 0.9rem;" data-question-id="${question.id}">
                Remove
            </button>
        </div>
    `;
    
    // Add event listeners
    const startReviewBtn = questionCard.querySelector('.start-review-btn');
    const removeBtn = questionCard.querySelector('.remove-btn');
    
    startReviewBtn.addEventListener('click', () => startReview(question));
    removeBtn.addEventListener('click', (e) => removeQuestion(e.target, question.id));
    
    questionList.appendChild(questionCard);
    
    // Save to localStorage
    const questions = loadFromStorage();
    questions.push(question);
    saveToStorage(questions);
};

// Question management functions
const startReview = (question) => {
    showNotification(`Starting review for ${question.number} - ${question.title}...\n\nThis is a demo version! In the full application, this would open the review interface.`);
};

const removeQuestion = (button, questionId) => {
    if (confirm('Are you sure you want to remove this question?')) {
        const questionCard = button.closest('.card');
        questionCard.style.animation = 'fadeOut 0.3s ease';
        
        setTimeout(() => {
            questionCard.remove();
            
            // Remove from localStorage
            const questions = loadFromStorage();
            const updatedQuestions = questions.filter(q => q.id !== questionId);
            saveToStorage(updatedQuestions);
            
            // Show "no questions" message if list is empty
            const questionList = document.getElementById('questionList');
            if (questionList.children.length === 0) {
                questionList.innerHTML = `
                    <p style="color: var(--text-secondary); text-align: center; padding: var(--spacing-xl);">
                        No questions added yet. Use the form above to add your first question!
                    </p>
                `;
            }
        }, 300);
    }
};

const cancelForm = () => {
    if (confirm('Are you sure you want to cancel? All unsaved changes will be lost.')) {
        document.getElementById('questionForm').reset();
    }
};

// Load saved questions on page load
const loadSavedQuestions = () => {
    const questions = loadFromStorage();
    questions.forEach(question => {
        addQuestionToList(question);
    });
};

// Initialize question list page
const initializeQuestionList = () => {
    try {
        // Initialize form
        const form = document.getElementById('questionForm');
        if (form) {
            form.addEventListener('submit', handleFormSubmit);
        }
        
        // Load saved questions
        loadSavedQuestions();
        
        // Add form validation on input
        const inputs = document.querySelectorAll('.form-input, .form-select');
        inputs.forEach(input => {
            input.addEventListener('blur', () => {
                if (input.checkValidity()) {
                    input.classList.remove('invalid');
                    input.classList.add('valid');
                } else {
                    input.classList.remove('valid');
                    input.classList.add('invalid');
                }
            });
        });
        
    } catch (error) {
        console.error('Error initializing question list:', error);
    }
};

// Initialize when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeQuestionList);
} else {
    initializeQuestionList();
}
