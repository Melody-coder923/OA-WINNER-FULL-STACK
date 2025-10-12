/* ============================================
   EMAIL REMINDER SYSTEM
   Daily email reminder for LeetCode practice
   ============================================ */

class EmailReminderSystem {
    constructor() {
        this.reminderSettings = this.loadReminderSettings();
        this.isEnabled = this.reminderSettings.enabled;
        this.reminderTime = this.reminderSettings.time;
        this.emailAddress = this.reminderSettings.email;
        this.lastReminderDate = this.reminderSettings.lastReminderDate;
        
        this.initReminderSystem();
    }

    // Load reminder settings from localStorage
    loadReminderSettings() {
        const defaultSettings = {
            enabled: false,
            time: "09:00",
            email: "",
            frequency: "daily", // daily, weekly
            lastReminderDate: null,
            timezone: Intl.DateTimeFormat().resolvedOptions().timeZone
        };
        
        return JSON.parse(localStorage.getItem('emailReminderSettings')) || defaultSettings;
    }

    // Save reminder settings to localStorage
    saveReminderSettings() {
        localStorage.setItem('emailReminderSettings', JSON.stringify(this.reminderSettings));
    }

    // Initialize the reminder system
    initReminderSystem() {
        if (this.isEnabled && this.emailAddress) {
            this.scheduleNextReminder();
            this.checkForDailyReminder();
        }
    }

    // Enable email reminders
    enableReminders(emailAddress, time = "09:00") {
        this.reminderSettings.enabled = true;
        this.reminderSettings.email = emailAddress;
        this.reminderSettings.time = time;
        this.reminderSettings.lastReminderDate = new Date().toISOString().split('T')[0];
        
        this.emailAddress = emailAddress;
        this.reminderTime = time;
        this.isEnabled = true;
        
        this.saveReminderSettings();
        this.scheduleNextReminder();
        
        console.log(`Email reminders enabled for ${emailAddress} at ${time}`);
    }

    // Disable email reminders
    disableReminders() {
        this.reminderSettings.enabled = false;
        this.isEnabled = false;
        
        this.saveReminderSettings();
        this.clearScheduledReminder();
        
        console.log('Email reminders disabled');
    }

    // Schedule next reminder
    scheduleNextReminder() {
        if (!this.isEnabled || !this.emailAddress) return;

        const [hours, minutes] = this.reminderTime.split(':');
        const now = new Date();
        const reminderTime = new Date();
        reminderTime.setHours(parseInt(hours), parseInt(minutes), 0, 0);

        // If the time has passed today, schedule for tomorrow
        if (reminderTime <= now) {
            reminderTime.setDate(reminderTime.getDate() + 1);
        }

        const timeUntilReminder = reminderTime.getTime() - now.getTime();
        
        // Clear any existing timeout
        this.clearScheduledReminder();
        
        // Schedule the reminder
        this.reminderTimeout = setTimeout(() => {
            this.sendDailyReminder();
            this.scheduleNextReminder(); // Schedule the next day
        }, timeUntilReminder);

        console.log(`Next reminder scheduled for ${reminderTime.toLocaleString()}`);
    }

    // Clear scheduled reminder
    clearScheduledReminder() {
        if (this.reminderTimeout) {
            clearTimeout(this.reminderTimeout);
            this.reminderTimeout = null;
        }
    }

    // Check if we should send a daily reminder
    checkForDailyReminder() {
        const today = new Date().toISOString().split('T')[0];
        
        if (this.lastReminderDate !== today) {
            const now = new Date();
            const [hours, minutes] = this.reminderTime.split(':');
            const reminderTime = new Date();
            reminderTime.setHours(parseInt(hours), parseInt(minutes), 0, 0);

            // If it's past reminder time today and we haven't sent one
            if (now >= reminderTime) {
                this.sendDailyReminder();
            }
        }
    }

    // Send daily reminder email
    async sendDailyReminder() {
        if (!this.isEnabled || !this.emailAddress) return;

        const today = new Date().toISOString().split('T')[0];
        this.reminderSettings.lastReminderDate = today;
        this.saveReminderSettings();

        try {
            // Get today's review questions
            const todayQuestions = this.getTodayReviewQuestions();
            const userStats = this.getUserStatistics();

            // Create email content
            const emailContent = this.createEmailContent(todayQuestions, userStats);

            // In a real application, this would send an actual email
            // For demo purposes, we'll simulate the email sending
            await this.simulateEmailSending(emailContent);

            console.log(`Daily reminder sent to ${this.emailAddress}`);
            this.showReminderNotification(todayQuestions.length);

        } catch (error) {
            console.error('Error sending daily reminder:', error);
        }
    }

    // Get today's review questions
    getTodayReviewQuestions() {
        if (typeof statsCalculator !== 'undefined') {
            return statsCalculator.getTodayReviewQuestions();
        }
        
        // Fallback data
        return [
            { id: 1, title: "Two Sum", difficulty: "Easy", reviewCount: 2, forgotCount: 1 },
            { id: 2, title: "Container With Most Water", difficulty: "Medium", reviewCount: 1, forgotCount: 0 },
            { id: 3, title: "Reverse Linked List", difficulty: "Easy", reviewCount: 3, forgotCount: 2 }
        ];
    }

    // Get user statistics
    getUserStatistics() {
        if (typeof statsCalculator !== 'undefined') {
            return statsCalculator.getAllStatistics();
        }
        
        // Fallback data
        return {
            totalQuestions: 15,
            totalReviews: 12,
            masteryRate: 80,
            forgettingScore: 20
        };
    }

    // Create email content
    createEmailContent(todayQuestions, userStats) {
        const currentDate = new Date().toLocaleDateString('en-US', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });

        return {
            to: this.emailAddress,
            subject: `🏆 Daily LeetCode Reminder - ${currentDate}`,
            html: this.generateEmailHTML(currentDate, todayQuestions, userStats),
            text: this.generateEmailText(currentDate, todayQuestions, userStats)
        };
    }

    // Generate HTML email content
    generateEmailHTML(date, todayQuestions, userStats) {
        const questionList = todayQuestions.length > 0 
            ? todayQuestions.map(q => `
                <li style="margin-bottom: 10px; padding: 10px; background-color: #f8f9fa; border-radius: 5px;">
                    <strong>${q.title}</strong> (${q.difficulty})
                    <br><small>Reviewed ${q.reviewCount} times, Forgot ${q.forgotCount} times</small>
                </li>
            `).join('')
            : '<li style="color: #28a745;">🎉 No questions to review today! Great job!</li>';

        return `
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Daily LeetCode Reminder</title>
        </head>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;">
            <div style="background: linear-gradient(135deg, #6F9C6E 0%, #4A6B4A 100%); color: white; padding: 20px; border-radius: 10px; text-align: center; margin-bottom: 20px;">
                <h1 style="margin: 0; font-size: 24px;">🏆 OA WINNER</h1>
                <p style="margin: 10px 0 0 0; font-size: 16px;">Daily LeetCode Practice Reminder</p>
            </div>
            
            <div style="background-color: #f8f9fa; padding: 20px; border-radius: 10px; margin-bottom: 20px;">
                <h2 style="color: #6F9C6E; margin-top: 0;">📅 ${date}</h2>
                <p>Time to practice your LeetCode problems! Here's what's on your schedule today:</p>
            </div>
            
            <div style="margin-bottom: 20px;">
                <h3 style="color: #6F9C6E;">📚 Today's Review Questions (${todayQuestions.length})</h3>
                <ul style="list-style: none; padding: 0;">
                    ${questionList}
                </ul>
            </div>
            
            <div style="background-color: #e8f5e8; padding: 15px; border-radius: 10px; margin-bottom: 20px;">
                <h3 style="color: #6F9C6E; margin-top: 0;">📊 Your Progress</h3>
                <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(120px, 1fr)); gap: 15px;">
                    <div style="text-align: center;">
                        <div style="font-size: 24px; font-weight: bold; color: #6F9C6E;">${userStats.totalQuestions}</div>
                        <div style="font-size: 12px; color: #666;">Total Questions</div>
                    </div>
                    <div style="text-align: center;">
                        <div style="font-size: 24px; font-weight: bold; color: #6F9C6E;">${userStats.totalReviews}</div>
                        <div style="font-size: 12px; color: #666;">Total Reviews</div>
                    </div>
                    <div style="text-align: center;">
                        <div style="font-size: 24px; font-weight: bold; color: #6F9C6E;">${userStats.masteryRate}%</div>
                        <div style="font-size: 12px; color: #666;">Mastery Rate</div>
                    </div>
                    <div style="text-align: center;">
                        <div style="font-size: 24px; font-weight: bold; color: #6F9C6E;">${userStats.forgettingScore}%</div>
                        <div style="font-size: 12px; color: #666;">Forgetting Score</div>
                    </div>
                </div>
            </div>
            
            <div style="background-color: #fff3cd; padding: 15px; border-radius: 10px; margin-bottom: 20px; border-left: 4px solid #ffc107;">
                <h3 style="color: #856404; margin-top: 0;">💡 Ebbinghaus Forgetting Curve Tips</h3>
                <ul style="color: #856404;">
                    <li>Review questions you marked as "Forgot" more frequently</li>
                    <li>Mastered questions will have longer intervals between reviews</li>
                    <li>Consistent daily practice improves long-term retention</li>
                </ul>
            </div>
            
            <div style="text-align: center; margin-top: 30px;">
                <a href="http://localhost:8080" style="background-color: #6F9C6E; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; font-weight: bold;">Start Practicing Now</a>
            </div>
            
            <div style="text-align: center; margin-top: 20px; font-size: 12px; color: #666;">
                <p>This is an automated reminder from OA WINNER - Your LeetCode Memory Tracker</p>
                <p>To change reminder settings, visit your dashboard.</p>
            </div>
        </body>
        </html>
        `;
    }

    // Generate text email content
    generateEmailText(date, todayQuestions, userStats) {
        const questionList = todayQuestions.length > 0 
            ? todayQuestions.map(q => `- ${q.title} (${q.difficulty}) - Reviewed ${q.reviewCount} times, Forgot ${q.forgotCount} times`).join('\n')
            : '🎉 No questions to review today! Great job!';

        return `
🏆 OA WINNER - Daily LeetCode Practice Reminder
${date}

Time to practice your LeetCode problems! Here's what's on your schedule today:

📚 Today's Review Questions (${todayQuestions.length}):
${questionList}

📊 Your Progress:
- Total Questions: ${userStats.totalQuestions}
- Total Reviews: ${userStats.totalReviews}
- Mastery Rate: ${userStats.masteryRate}%
- Forgetting Score: ${userStats.forgettingScore}%

💡 Ebbinghaus Forgetting Curve Tips:
- Review questions you marked as "Forgot" more frequently
- Mastered questions will have longer intervals between reviews
- Consistent daily practice improves long-term retention

Start Practicing: http://localhost:8080

This is an automated reminder from OA WINNER - Your LeetCode Memory Tracker
To change reminder settings, visit your dashboard.
        `;
    }

    // Simulate email sending (in real app, this would use an email service)
    async simulateEmailSending(emailContent) {
        // Simulate API call delay
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        // In a real application, you would:
        // 1. Send to your backend API
        // 2. Use services like SendGrid, AWS SES, etc.
        // 3. Store email logs in database
        
        console.log('Email sent:', {
            to: emailContent.to,
            subject: emailContent.subject,
            timestamp: new Date().toISOString()
        });
        
        // Store email log locally for demo
        this.logEmailSent(emailContent);
    }

    // Log email sent
    logEmailSent(emailContent) {
        const emailLog = JSON.parse(localStorage.getItem('emailLogs')) || [];
        emailLog.push({
            to: emailContent.to,
            subject: emailContent.subject,
            timestamp: new Date().toISOString(),
            type: 'daily_reminder'
        });
        
        // Keep only last 30 email logs
        if (emailLog.length > 30) {
            emailLog.splice(0, emailLog.length - 30);
        }
        
        localStorage.setItem('emailLogs', JSON.stringify(emailLog));
    }

    // Show reminder notification
    showReminderNotification(questionCount) {
        if (typeof showNotification !== 'undefined') {
            showNotification(
                `📧 Daily reminder sent! ${questionCount} questions to review today.`,
                'info'
            );
        }
    }

    // Get email logs
    getEmailLogs() {
        return JSON.parse(localStorage.getItem('emailLogs')) || [];
    }

    // Test email sending
    async testEmailSending() {
        if (!this.emailAddress) {
            throw new Error('No email address configured');
        }
        
        const todayQuestions = this.getTodayReviewQuestions();
        const userStats = this.getUserStatistics();
        const emailContent = this.createEmailContent(todayQuestions, userStats);
        
        await this.simulateEmailSending(emailContent);
        this.showReminderNotification(todayQuestions.length);
    }
}

// Global email reminder system instance
const emailReminderSystem = new EmailReminderSystem();

// Export for use in other files
window.EmailReminderSystem = EmailReminderSystem;
window.emailReminderSystem = emailReminderSystem;
