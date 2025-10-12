# LeetCode Memory Tracker - Complete Project Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Technology Stack](#technology-stack)
3. [System Architecture](#system-architecture)
4. [Core Business Logic](#core-business-logic)
5. [Ebbinghaus Forgetting Curve Algorithm](#ebbinghaus-forgetting-curve-algorithm)
6. [Database Design](#database-design)
7. [API Interface Design](#api-interface-design)
8. [Frontend Page Design](#frontend-page-design)
9. [Deployment Guide](#deployment-guide)
10. [User Guide](#user-guide)

---

## Project Overview

LeetCode Memory Tracker is an intelligent problem-solving system based on the Ebbinghaus forgetting curve, designed to help users scientifically review LeetCode problems and improve learning efficiency.

### Core Features
- **Problem Management**: Add, search, and filter LeetCode problems
- **Smart Review**: Automatically schedule review plans based on forgetting curve algorithm
- **Progress Tracking**: Real-time statistics of learning progress and mastery level
- **Data Visualization**: Intuitive display of learning data and trends

### Project Highlights
- Scientific review interval algorithm
- Beautiful modern UI interface
- Comprehensive statistical data analysis
- Responsive design supporting multiple devices

---

## Technology Stack

### Backend Technologies
- **Java 17**: Primary programming language
- **Spring Boot 3.2.0**: Application framework
- **Spring Data JPA**: Data access layer
- **MySQL 8.x**: Database
- **Vaadin 24.3.0**: UI framework
- **Lombok**: Code simplification tool

### Frontend Technologies
- **HTML5**: Page structure
- **CSS3**: Styling design
- **JavaScript ES6+**: Interactive logic
- **Vaadin Components**: UI component library

### Development Tools
- **Maven**: Project management
- **Git**: Version control
- **IDE**: IntelliJ IDEA / VS Code

---

## System Architecture

### Overall Architecture Diagram
```
┌─────────────────────────────────────────────────────────────────┐
│                    LeetCode Memory Tracker                     │
│                     System Architecture                        │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Backend       │    │   Database      │
│   (Vaadin UI)   │    │   (Spring Boot) │    │   (MySQL)       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│ • Dashboard     │    │ • View Layer    │    │ • User Table    │
│ • Question List │    │ • Service Layer │    │ • Question Table│
│ • Today Review  │    │ • Repository    │    │ • Review Tables │
│ • Statistics    │    │ • Entity Layer  │    │ • History Table │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Layered Architecture
```
┌─────────────────────────────────────────────────────────────────┐
│                    Layered Architecture Design                  │
└─────────────────────────────────────────────────────────────────┘

Presentation Layer
├── Vaadin Views
│   ├── DashboardView
│   ├── QuestionListView
│   ├── TodayReviewView
│   └── StatisticsView
└── MainLayout

Business Layer
├── Services
│   ├── UserService
│   ├── QuestionService
│   ├── ReviewService
│   └── EbbinghausSchedulerService
└── Schedulers
    └── DailyReviewScheduler

Data Access Layer
├── Repositories
│   ├── UserRepository
│   ├── QuestionRepository
│   ├── UserQuestionReviewRepository
│   └── ReviewHistoryRepository
└── Entities
    ├── User
    ├── Question
    ├── UserQuestionReview
    └── ReviewHistory

Database Layer
└── MySQL Database
    ├── user table
    ├── question table
    ├── user_question_review table
    └── review_history table
```

---

## Core Business Logic

### User Management Flow
```
┌─────────────────────────────────────────────────────────────────┐
│                      User Management Flow                       │
└─────────────────────────────────────────────────────────────────┘

User Registration/Login
    │
    ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│ Validate    │───▶│ Create/Get  │───▶│ Initialize  │
│ User        │    │ User        │    │ User        │
│ Credentials │    │ Session     │    │ Data        │
└─────────────┘    └─────────────┘    └─────────────┘
    │                       │                   │
    ▼                       ▼                   ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│ Password    │    │ Session     │    │ Load Review │
│ Verification│    │ Management  │    │ Progress    │
│ Permission  │    │             │    │             │
│ Check       │    │             │    │             │
└─────────────┘    └─────────────┘    └─────────────┘
```

### Problem Management Flow
```
┌─────────────────────────────────────────────────────────────────┐
│                    Problem Management Flow                      │
└─────────────────────────────────────────────────────────────────┘

Problem List Display
    │
    ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│ Load All    │───▶│ Apply       │───▶│ Paginate    │
│ Problems    │    │ Filter      │    │ Display     │
│             │    │ Conditions  │    │ Results     │
└─────────────┘    └─────────────┘    └─────────────┘
    │                       │                   │
    ▼                       ▼                   ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│ Database    │    │ Difficulty  │    │ User        │
│ Query       │    │ Filter      │    │ Operation   │
│             │    │ Tag Filter  │    │ Interface   │
└─────────────┘    └─────────────┘    └─────────────┘

Add New Problem
    │
    ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│ Input       │───▶│ Validate    │───▶│ Save to     │
│ Problem     │    │ Data        │    │ Database    │
│ Information │    │ Integrity   │    │             │
└─────────────┘    └─────────────┘    └─────────────┘
    │                       │                   │
    ▼                       ▼                   ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│ Form Input  │    │ Required    │    │ Create      │
│ Interface   │    │ Field       │    │ Problem     │
│             │    │ Format      │    │ Record      │
│             │    │ Validation  │    │             │
└─────────────┘    └─────────────┘    └─────────────┘
```

### Review Management Flow
```
┌─────────────────────────────────────────────────────────────────┐
│                    Review Management Flow                       │
└─────────────────────────────────────────────────────────────────┘

Start Review
    │
    ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│ Select      │───▶│ Create      │───▶│ Initialize  │
│ Problem &   │    │ Review      │    │ Ebbinghaus  │
│ Start       │    │ Record      │    │ Schedule    │
│ Review      │    │             │    │             │
└─────────────┘    └─────────────┘    └─────────────┘
    │                       │
    ▼                       ▼
┌─────────────┐    ┌─────────────┐
│ Problem     │    │ User-Problem│
│ List        │    │ Association │
│ Interface   │    │ Record      │
└─────────────┘    └─────────────┘

Submit Review
    │
    ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│ Input       │───▶│ Record      │───▶│ Update Next │
│ Review      │    │ Review      │    │ Review      │
│ Rating      │    │ History     │    │ Time        │
└─────────────┘    └─────────────┘    └─────────────┘
    │                       │                   │
    ▼                       ▼                   ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│ Rating      │    │ Save to     │    │ Calculate   │
│ Selection   │    │ History     │    │ New         │
│ Time        │    │ Table       │    │ Interval    │
│ Recording   │    │             │    │             │
│ Notes       │    │             │    │             │
│ Input       │    │             │    │             │
└─────────────┘    └─────────────┘    └─────────────┘
```

---

## Ebbinghaus Forgetting Curve Algorithm

### Algorithm Principle
The Ebbinghaus forgetting curve is a memory forgetting pattern proposed by German psychologist Hermann Ebbinghaus, showing that forgetting begins immediately after learning, with rapid initial forgetting that gradually slows down.

### Algorithm Implementation
```
┌─────────────────────────────────────────────────────────────────┐
│                Ebbinghaus Forgetting Curve Logic               │
└─────────────────────────────────────────────────────────────────┘

┌─────────────┐
│ User Review │
│ Submission  │
└─────┬───────┘
      │
      ▼
┌─────────────┐
│ Get Rating  │
│ (Forgot/    │
│ Hard/Good/  │
│ Easy)       │
└─────┬───────┘
      │
      ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│ Calculate   │───▶│ Update      │───▶│ Schedule    │
│ Next        │    │ Interval    │    │ Next        │
│ Interval    │    │ Index       │    │ Review      │
└─────────────┘    └─────────────┘    └─────────────┘
      │
      ▼
┌─────────────┐
│ Review      │
│ Intervals   │
│ [1,2,4,7,   │
│ 15,30] days │
└─────────────┘
```

### Rating Logic
```
Rating Logic:
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│ FORGOT      │───▶│ Reset to    │    │ Index = 0   │
│ (0 points)  │    │ Interval 1  │    │ Next: 1 day │
└─────────────┘    └─────────────┘    └─────────────┘

┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│ HARD        │───▶│ Stay at     │    │ Index = 0   │
│ (1 point)   │    │ Same        │    │ Next: 1 day │
└─────────────┘    └─────────────┘    └─────────────┘

┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│ GOOD        │───▶│ Move to     │    │ Index += 1  │
│ (2 points)  │    │ Next        │    │ Next: 2 days│
└─────────────┘    └─────────────┘    └─────────────┘

┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│ EASY        │───▶│ Move to     │    │ Index += 2  │
│ (3 points)  │    │ Next + 1    │    │ Next: 4 days│
└─────────────┘    └─────────────┘    └─────────────┘
```

### Interval Configuration
- **Default Intervals**: [1, 2, 4, 7, 15, 30] days
- **Configurable**: Customize intervals through application.properties
- **Dynamic Adjustment**: Automatically adjust review frequency based on user performance

---

## Database Design

### Table Structure Design

#### 1. User Table
```sql
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    display_name VARCHAR(100),
    total_solved INT DEFAULT 0,
    current_streak INT DEFAULT 0,
    longest_streak INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 2. Question Table
```sql
CREATE TABLE question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    difficulty ENUM('EASY', 'MEDIUM', 'HARD') NOT NULL,
    leetcode_url VARCHAR(500),
    leetcode_number INT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 3. Question Tags Table
```sql
CREATE TABLE question_tags (
    question_id BIGINT,
    tag VARCHAR(50),
    PRIMARY KEY (question_id, tag),
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE
);
```

#### 4. User Question Review Table
```sql
CREATE TABLE user_question_review (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    status ENUM('NOT_STARTED', 'IN_PROGRESS', 'MASTERED', 'ARCHIVED') DEFAULT 'NOT_STARTED',
    current_interval_index INT DEFAULT 0,
    total_reviews INT DEFAULT 0,
    mastered_count INT DEFAULT 0,
    forgot_count INT DEFAULT 0,
    next_review_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_question (user_id, question_id)
);
```

#### 5. Review History Table
```sql
CREATE TABLE review_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    review_rating ENUM('FORGOT', 'HARD', 'GOOD', 'EASY') NOT NULL,
    review_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    time_spent_seconds INT,
    notes TEXT,
    interval_before INT,
    interval_after INT,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE
);
```

### Entity Relationship Diagram
```
┌─────────────────────────────────────────────────────────────────┐
│                   Entity Relationship Diagram                   │
└─────────────────────────────────────────────────────────────────┘

User
    │
    │ 1:N
    ▼
UserQuestionReview
    │
    │ N:1
    ▼
Question
    │
    │ 1:N
    ▼
QuestionTags

User
    │
    │ 1:N
    ▼
ReviewHistory
    │
    │ N:1
    ▼
Question
```

---

## Frontend Page Design

### Page Structure
```
┌─────────────────────────────────────────────────────────────────┐
│                      Page Structure Design                      │
└─────────────────────────────────────────────────────────────────┘

MainLayout
├── Header
│   └── Navigation
├── Sidebar
│   ├── Dashboard
│   ├── Today's Review
│   ├── Question List
│   └── Statistics
└── Content Area
    ├── DashboardView
    ├── TodayReviewView
    ├── QuestionListView
    └── StatisticsView
```

### Page Functions

#### 1. Dashboard
- **Function**: Display user learning overview
- **Components**: 
  - Statistics cards (Total problems, Solved, Due reviews, Streak days)
  - Recent activity list
  - Quick action buttons

#### 2. Today's Review
- **Function**: Display today's problems to review
- **Components**:
  - Review list
  - Review rating interface
  - Progress tracking

#### 3. Question List
- **Function**: Manage LeetCode problems
- **Components**:
  - Problem search and filtering
  - Problem list display
  - Add new problem form

#### 4. Statistics
- **Function**: Display detailed learning statistics
- **Components**:
  - Overview statistics
  - Difficulty distribution
  - Tag distribution
  - Review statistics
  - Recent activity

---

## Deployment Guide

### Environment Requirements
- **Java**: 17+
- **Maven**: 3.6+
- **MySQL**: 8.0+
- **Memory**: Minimum 2GB RAM

### Deployment Steps

#### 1. Database Configuration
```sql
-- Create database
CREATE DATABASE leetcode_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create user
CREATE USER 'tracker_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON leetcode_tracker.* TO 'tracker_user'@'localhost';
FLUSH PRIVILEGES;
```

#### 2. Application Configuration
```properties
# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/leetcode_tracker
spring.datasource.username=tracker_user
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Ebbinghaus intervals (days)
app.ebbinghaus.intervals=1,2,4,7,15,30

# Server configuration
server.port=8080
```

#### 3. Start Application
```bash
# Start with Maven
mvn spring-boot:run

# Or use provided scripts
# Windows
run.bat

# Linux/Mac
./run.sh
```

### Docker Deployment (Optional)
```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/memory-tracker-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## User Guide

### Quick Start

#### 1. First Time Use
1. Start the application and visit `http://localhost:8080`
2. The system will automatically create a demo user
3. Start adding LeetCode problems

#### 2. Add Problems
1. Go to "Question List" page
2. Click "Add Question" button
3. Fill in problem information:
   - Problem title
   - Difficulty level
   - Tag classification
   - LeetCode URL
   - Problem number

#### 3. Start Review
1. Find the problem to review in the problem list
2. Click "Start Review" to begin review
3. The system will schedule review time based on Ebbinghaus algorithm

#### 4. Submit Review
1. Go to "Today's Review" page
2. Select the problem to review
3. Choose rating based on mastery level:
   - **Forgot**: Completely forgotten, reset to first day review
   - **Hard**: Difficult, maintain current interval
   - **Good**: Good, move to next interval
   - **Easy**: Easy, skip to next interval
4. Fill in review notes and time
5. Submit review record

#### 5. View Statistics
1. Go to "Statistics" page
2. View detailed learning statistics:
   - Overall progress
   - Difficulty distribution
   - Review frequency
   - Mastery rate analysis

### Best Practices

#### 1. Review Strategy
- **Daily Review**: Maintain learning continuity
- **Honest Rating**: Accurately reflect mastery level
- **Timely Recording**: Record solution approaches and difficulties

#### 2. Problem Management
- **Categorization**: Use tags for proper classification
- **Progressive Learning**: Start with easy problems
- **Regular Review**: Regularly check statistical reports

#### 3. System Optimization
- **Regular Backup**: Backup user data
- **Performance Monitoring**: Monitor system performance
- **Timely Updates**: Keep system version updated

---

## Development Guide

### Project Structure
```
OA-WINNER-FULL-STACK/
├── src/main/java/com/leetcode/tracker/
│   ├── entity/                 # Entity classes
│   ├── repository/            # Data access layer
│   ├── service/               # Business logic layer
│   ├── views/                 # View layer
│   ├── scheduler/             # Scheduled tasks
│   └── LeetcodeTrackerApplication.java
├── src/main/resources/
│   ├── application.properties # Configuration file
│   └── data.sql              # Initial data
├── pom.xml                   # Maven configuration
├── run.bat                   # Windows startup script
├── run.sh                    # Linux/Mac startup script
└── README.md                 # Project documentation
```

### Development Environment Setup
1. Install Java 17 and Maven
2. Install MySQL 8.0
3. Clone project code
4. Configure database connection
5. Run `mvn spring-boot:run`

### Code Standards
- Use Lombok to reduce boilerplate code
- Follow Spring Boot best practices
- Use Vaadin component library for UI
- Keep code clean and readable

---

## Performance Optimization

### Database Optimization
- Add indexes for frequently queried fields
- Use connection pool to manage database connections
- Regularly clean up historical data

### Application Optimization
- Use caching to reduce database queries
- Asynchronous processing for time-consuming operations
- Optimize query statements

### Frontend Optimization
- Use CDN to accelerate static resources
- Implement lazy loading to reduce initial loading time
- Compress CSS and JavaScript files

---

## Troubleshooting

### Common Issues

#### 1. Database Connection Failed
- Check if MySQL service is running
- Verify database connection configuration
- Confirm user permission settings

#### 2. Application Startup Failed
- Check if Java version is 17+
- Verify Maven dependencies are correct
- Check application log files

#### 3. Page Display Issues
- Clear browser cache
- Check network connection
- View browser console errors

### Log Viewing
```bash
# View application logs
tail -f logs/application.log

# View error logs
grep ERROR logs/application.log
```

---

## Future Plans

### Feature Extensions
- [ ] Mobile app development
- [ ] Social features (friend system)
- [ ] Competition mode
- [ ] Intelligent recommendation algorithm
- [ ] Data export functionality

### Technical Upgrades
- [ ] Microservices architecture transformation
- [ ] Containerized deployment
- [ ] Cloud-native support
- [ ] Real-time notification system
- [ ] Machine learning integration

### User Experience
- [ ] Theme switching functionality
- [ ] Personalized settings
- [ ] Keyboard shortcuts support
- [ ] Offline mode
- [ ] Multi-language support

---

## Technical Support

### Contact Information
- **Project Repository**: [GitHub Repository]
- **Documentation Site**: [Documentation Site]
- **Issue Feedback**: [Issues Page]

### Contribution Guidelines
1. Fork project code
2. Create feature branch
3. Submit code changes
4. Create Pull Request

---

## License

This project is licensed under the MIT License. See LICENSE file for details.

--
--