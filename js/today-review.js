/* ============================================
   TODAY REVIEW SPECIFIC JAVASCRIPT
   Today review page functionality
   ============================================ */

// Variables
let completedTasks = 0;
const totalTasks = 3;

// Save review function
function saveReview(taskId) {
    const taskCard = document.querySelector(`[data-task-id="${taskId}"]`);
    const statusSelect = taskCard.querySelector('select[name="reviewStatus"]');
    const notesTextarea = taskCard.querySelector('textarea[name="notes"]');
    const saveButton = taskCard.querySelector('.btn');
    
    if (!statusSelect.value) {
        showNotification('Please select a review status before saving.', 'error');
        return;
    }
    
    // Mark task as completed
    taskCard.style.opacity = '0.7';
    taskCard.style.borderColor = 'var(--primary-color)';
    saveButton.textContent = 'Saved ✓';
    saveButton.disabled = true;
    saveButton.style.background = 'var(--primary-dark)';
    
    completedTasks++;
    updateProgress();
    
    // Show completion message if all tasks are done
    if (completedTasks === totalTasks) {
        setTimeout(() => {
            document.getElementById('completionSection').style.display = 'block';
        }, 1000);
    }
    
    // Show success message
    const statusText = statusSelect.value === 'forgotten' ? 'Forgotten' : 'Mastered';
    showNotification(`Review saved successfully!\nStatus: ${statusText}\nNotes: ${notesTextarea.value || 'No notes added'}`);
}

// Update progress function
function updateProgress() {
    const progressFill = document.getElementById('progressFill');
    const progressText = document.getElementById('progressText');
    
    const percentage = (completedTasks / totalTasks) * 100;
    progressFill.style.width = `${percentage}%`;
    progressText.textContent = `${completedTasks}/${totalTasks}`;
}

// Initialize today review page
const initializeTodayReview = () => {
    try {
        // Initialize progress
        updateProgress();
        
        // Add animation to progress bars
        const progressBars = document.querySelectorAll('.progress-fill');
        progressBars.forEach(bar => {
            const width = bar.style.width;
            bar.style.width = '0%';
            setTimeout(() => {
                bar.style.width = width;
            }, 300);
        });
        
    } catch (error) {
        console.error('Error initializing today review:', error);
    }
};

// Initialize when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeTodayReview);
} else {
    initializeTodayReview();
}
