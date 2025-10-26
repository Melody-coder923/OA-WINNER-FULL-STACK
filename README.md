# 🏆 OA WINNER - LeetCode Memory Tracker

A full-stack application that helps you track and review LeetCode problems using the Ebbinghaus Forgetting Curve algorithm for optimal learning and retention.

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Detailed Setup Instructions](#detailed-setup-instructions)
- [Running the Application](#running-the-application)
- [Project Structure](#project-structure)
- [Usage Guide](#usage-guide)
- [Troubleshooting](#troubleshooting)
- [Documentation](#documentation)

## 🎯 Overview

OA WINNER is an intelligent problem-solving tracking system that helps you:
- **Manage** your LeetCode problem collection
- **Schedule** reviews based on the scientifically-proven Ebbinghaus Forgetting Curve
- **Track** your learning progress and statistics
- **Optimize** your study time with smart review intervals

## ✨ Features

- 📊 **Dashboard**: Overview of your learning statistics and progress
- ⏰ **Smart Review System**: Automatic scheduling based on Ebbinghaus algorithm
- 📝 **Question Management**: Add, search, and organize LeetCode problems
- 📈 **Statistics & Analytics**: Visual insights into your learning patterns
- 🎨 **Modern UI**: Clean, responsive interface with both static HTML and Vaadin components
- 📧 **Email Settings**: Configure notifications (feature in development)

## 🛠 Technology Stack

### Backend
- **Java 17+**: Core programming language
- **Spring Boot 3.2.0**: Application framework
- **Spring Data JPA**: Database interaction
- **Vaadin 24.3.0**: Modern web UI framework
- **Lombok**: Reduce boilerplate code

### Frontend
- **HTML5/CSS3**: Static pages
- **JavaScript ES6+**: Interactive features
- **Vaadin Components**: Rich UI widgets

### Database
- **MySQL 8.0+**: Primary database

### Build Tools
- **Maven 3.6+**: Dependency management and build automation

## 📦 Prerequisites

Before you begin, ensure you have the following installed on your system:

### Required Software

1. **Java Development Kit (JDK) 17 or higher**
   - Check version: `java -version`
   - Download: [OpenJDK](https://adoptium.net/) or [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)

2. **Apache Maven 3.6 or higher**
   - Check version: `mvn --version`
   - Download: [Apache Maven](https://maven.apache.org/download.cgi)

3. **MySQL 8.0 or higher**
   - Check version: `mysql --version`
   - Download: [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)

### Verify Installation

```bash
# Check Java version
java -version
# Should show: openjdk version "17.x.x" or higher

# Check Maven version
mvn --version
# Should show: Apache Maven 3.6.x or higher

# Check MySQL version
mysql --version
# Should show: mysql Ver 8.0.x or higher
```

## 🚀 Quick Start

For those who want to get started immediately:

```bash
# 1. Clone the repository
git clone https://github.com/Melody-coder923/OA-WINNER-FULL-STACK.git
cd OA-WINNER-FULL-STACK

# 2. Set up MySQL database
mysql -u root -p
# Then run these SQL commands:
CREATE DATABASE leetcode_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
exit;

# 3. Configure database credentials (if different from defaults)
# Edit src/main/resources/application.yml and update:
#   - spring.datasource.username (default: root)
#   - spring.datasource.password (default: password)

# 4. Run the application
# On Linux/Mac:
chmod +x run.sh
./run.sh

# On Windows:
run.bat

# 5. Access the application
# Open your browser and navigate to:
# http://localhost:8080
```

## 📚 Detailed Setup Instructions

### Step 1: Database Setup

#### 1.1 Start MySQL Server

Make sure MySQL is running:

```bash
# On Ubuntu/Debian
sudo systemctl start mysql
sudo systemctl status mysql

# On macOS (with Homebrew)
brew services start mysql

# On Windows
# MySQL should start automatically, or use Services panel
```

#### 1.2 Create Database

Connect to MySQL and create the database:

```bash
mysql -u root -p
```

Then execute the following SQL commands:

```sql
-- Create the database
CREATE DATABASE IF NOT EXISTS leetcode_tracker 
  CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci;

-- Verify database creation
SHOW DATABASES LIKE 'leetcode_tracker';

-- Exit MySQL
exit;
```

#### 1.3 Configure Database User (Optional but Recommended)

For better security, create a dedicated database user:

```sql
-- Create a dedicated user
CREATE USER 'tracker_user'@'localhost' IDENTIFIED BY 'your_secure_password';

-- Grant privileges
GRANT ALL PRIVILEGES ON leetcode_tracker.* TO 'tracker_user'@'localhost';

-- Apply changes
FLUSH PRIVILEGES;

-- Exit MySQL
exit;
```

If you create a dedicated user, update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    username: tracker_user
    password: your_secure_password
```

### Step 2: Application Configuration

The application configuration is located in `src/main/resources/application.yml`.

#### Default Configuration

The default configuration should work if you're using:
- **MySQL Host**: localhost
- **MySQL Port**: 3306
- **Database Name**: leetcode_tracker
- **Username**: root
- **Password**: password

#### Customize Configuration

If your setup is different, edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/leetcode_tracker?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root              # Change this to your MySQL username
    password: password          # Change this to your MySQL password
```

### Step 3: Build the Application

Navigate to the project directory and build the application:

```bash
cd OA-WINNER-FULL-STACK

# Clean and build the project
mvn clean install

# This will:
# - Download all dependencies
# - Compile the Java code
# - Run tests
# - Create the executable JAR file
```

**Note**: The first build may take several minutes as Maven downloads all dependencies.

### Step 4: Initialize Database Schema and Data

The application uses Hibernate's `ddl-auto: update` setting, which means:
- Database tables will be created automatically on first run
- Sample data from `src/main/resources/data.sql` will be inserted

No manual SQL script execution is required!

## 🏃 Running the Application

### Option 1: Using the Startup Scripts (Recommended)

#### On Linux/macOS:
```bash
chmod +x run.sh
./run.sh
```

#### On Windows:
```cmd
run.bat
```

### Option 2: Using Maven Directly

```bash
mvn spring-boot:run
```

### Option 3: Using the JAR File

```bash
# First, build the JAR
mvn clean package

# Then run it
java -jar target/memory-tracker-1.0.0.jar
```

### Verify Application is Running

1. Check the console output for:
   ```
   Started LeetCodeTrackerApplication in X.XXX seconds
   ```

2. Look for the startup message indicating the port:
   ```
   Tomcat started on port(s): 8080 (http)
   ```

3. Open your browser and navigate to:
   ```
   http://localhost:8080
   ```

4. You should see the Vaadin UI or can access the static HTML pages:
   - Dashboard: `http://localhost:8080/demo.html`
   - Today's Review: `http://localhost:8080/today-review.html`
   - Question List: `http://localhost:8080/question-list.html`
   - Statistics: `http://localhost:8080/statistics.html`

## 📁 Project Structure

```
OA-WINNER-FULL-STACK/
├── src/
│   └── main/
│       ├── java/com/leetcode/tracker/
│       │   ├── entity/              # JPA entities (User, Question, etc.)
│       │   ├── repository/          # Spring Data repositories
│       │   ├── service/             # Business logic services
│       │   ├── views/               # Vaadin UI views
│       │   ├── scheduler/           # Scheduled tasks
│       │   └── LeetCodeTrackerApplication.java
│       └── resources/
│           ├── application.yml      # Application configuration
│           ├── schema.sql           # Database schema (reference)
│           └── data.sql             # Sample data
├── styles/                          # CSS files for static pages
├── js/                              # JavaScript files for static pages
├── *.html                           # Static HTML pages
├── pom.xml                          # Maven configuration
├── run.sh                           # Linux/Mac startup script
├── run.bat                          # Windows startup script
├── README.md                        # This file
├── FILE_STRUCTURE.md                # Detailed file structure documentation
└── LeetCode_Memory_Tracker_Documentation.md  # Complete project documentation
```

## 📖 Usage Guide

### First Time Setup

1. **Start the application** following the instructions above
2. **Access the application** at `http://localhost:8080`
3. **Demo user** is automatically created with sample data:
   - Username: `demo`
   - Email: `demo@example.com`
   - Sample questions and reviews are pre-loaded

### Adding Questions

1. Navigate to the **Question List** page
2. Click **"Add Question"** button
3. Fill in the details:
   - Question title
   - Difficulty level (Easy/Medium/Hard)
   - LeetCode URL
   - Problem number
   - Tags
4. Click **"Save"**

### Starting Reviews

1. In the Question List, find a question
2. Click **"Start Review"**
3. The system will schedule it based on the Ebbinghaus algorithm

### Submitting Reviews

1. Go to **"Today's Review"** page
2. Select a question to review
3. Choose your rating:
   - **FORGOT**: Reset to day 1 interval
   - **HARD**: Keep current interval
   - **GOOD**: Move to next interval
   - **EASY**: Skip ahead in intervals
4. Add notes (optional)
5. Submit

### Review Intervals

The default Ebbinghaus intervals are:
- Day 1
- Day 2
- Day 4
- Day 7
- Day 15
- Day 30

These can be customized in `application.yml`:

```yaml
app:
  ebbinghaus:
    intervals:
      - 1
      - 2
      - 4
      - 7
      - 15
      - 30
```

## 🔧 Troubleshooting

### Problem: Application won't start

**Error**: `Error creating bean with name 'dataSource'`

**Solution**: 
- Verify MySQL is running: `sudo systemctl status mysql`
- Check database exists: `mysql -u root -p -e "SHOW DATABASES;"`
- Verify credentials in `application.yml`

### Problem: Database connection refused

**Error**: `Communications link failure`

**Solutions**:
1. Start MySQL server
2. Check MySQL is listening on port 3306: `netstat -an | grep 3306`
3. Verify firewall isn't blocking the connection
4. Try connecting manually: `mysql -u root -p -h localhost`

### Problem: Port 8080 already in use

**Error**: `Web server failed to start. Port 8080 was already in use`

**Solutions**:
1. Kill the process using port 8080:
   ```bash
   # Find the process
   lsof -i :8080
   # Kill it
   kill -9 <PID>
   ```
2. Or change the port in `application.yml`:
   ```yaml
   server:
     port: 8081
   ```

### Problem: Maven build fails

**Error**: Various Maven errors

**Solutions**:
1. Clear Maven cache:
   ```bash
   mvn clean
   rm -rf ~/.m2/repository
   mvn install
   ```
2. Verify Java version: `java -version` (must be 17+)
3. Update Maven: Download latest from [maven.apache.org](https://maven.apache.org/)

### Problem: Page not found (404)

**Solution**:
- For Vaadin pages: Navigate to `http://localhost:8080/`
- For static HTML: Navigate to `http://localhost:8080/demo.html`

### Problem: Database tables not created

**Solution**:
Check the `application.yml` setting:
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update  # Should be 'update' or 'create'
```

### Viewing Logs

Check the application logs for detailed error information:

```bash
# If running with startup script, logs appear in console

# If running as background process, check Spring Boot logs
tail -f logs/application.log

# Check MySQL logs
sudo tail -f /var/log/mysql/error.log
```

## 📚 Documentation

For more detailed information, please refer to:

- **[FILE_STRUCTURE.md](FILE_STRUCTURE.md)**: Detailed explanation of the project file structure
- **[LeetCode_Memory_Tracker_Documentation.md](LeetCode_Memory_Tracker_Documentation.md)**: Complete technical documentation including:
  - System architecture
  - Database design
  - API documentation
  - Ebbinghaus algorithm details
  - Development guide

## 🎓 Ebbinghaus Forgetting Curve

This application implements the Ebbinghaus Forgetting Curve algorithm to optimize your learning:

- **Day 1**: First review after learning
- **Day 2**: Second review
- **Day 4**: Third review
- **Day 7**: One week review
- **Day 15**: Two week review
- **Day 30**: One month review

The intervals adjust based on your performance:
- **FORGOT**: Reset to day 1
- **HARD**: Repeat current interval
- **GOOD**: Advance to next interval
- **EASY**: Skip ahead

## 🤝 Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.

## 📄 License

This project is licensed under the MIT License.

## 🆘 Getting Help

If you encounter any issues:

1. Check the [Troubleshooting](#troubleshooting) section
2. Review the detailed [documentation](LeetCode_Memory_Tracker_Documentation.md)
3. Check existing GitHub issues
4. Create a new issue with:
   - Your operating system
   - Java version (`java -version`)
   - Maven version (`mvn --version`)
   - MySQL version (`mysql --version`)
   - Complete error message
   - Steps to reproduce

## 🎉 Next Steps

Once your application is running:

1. Explore the demo data
2. Add your own LeetCode problems
3. Start reviewing and build your learning streak!
4. Check the Statistics page to track your progress

Happy coding and learning! 🚀
