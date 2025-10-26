# 📖 How to Run the OA WINNER Application

## What You Asked For

You asked me to **read all the files in the repository and tell you what you need to do to make your application run**. Here's the complete answer!

## 🎯 What This Application Is

**OA WINNER** is a LeetCode Memory Tracker - a full-stack web application that helps you:
- Track LeetCode problems you're studying
- Schedule reviews using the Ebbinghaus Forgetting Curve algorithm
- Monitor your learning progress with statistics and analytics
- Build and maintain study streaks

## 🛠 Technology Stack

Your application uses:
- **Backend:** Spring Boot 3.2.0 (Java 17)
- **Frontend:** Vaadin 24.3.0 + Static HTML/CSS/JavaScript
- **Database:** MySQL 8.0+
- **Build Tool:** Maven 3.6+

## ✅ What You Need to Do

### Prerequisites to Install

1. **Java 17 or higher**
   - Download from: https://adoptium.net/
   - Verify: `java -version`

2. **Apache Maven 3.6 or higher**
   - Download from: https://maven.apache.org/download.cgi
   - Verify: `mvn --version`

3. **MySQL 8.0 or higher**
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Verify: `mysql --version`

### Quick Start (Choose One Method)

#### Method 1: Automated Setup (Easiest!)

**Linux/macOS:**
```bash
chmod +x setup.sh
./setup.sh
```

**Windows:**
```cmd
setup.bat
```

The script automatically:
- Checks prerequisites
- Starts MySQL
- Creates the database
- Builds the application

#### Method 2: Manual Setup (3 Simple Steps)

**Step 1: Setup MySQL Database**
```bash
# Start MySQL
sudo systemctl start mysql     # Linux
brew services start mysql      # macOS

# Create database
mysql -u root -p
```
```sql
CREATE DATABASE leetcode_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER USER 'root'@'localhost' IDENTIFIED BY 'password';
exit;
```

**Step 2: Build the Application**
```bash
mvn clean compile
```

**Step 3: Run the Application**
```bash
# Choose one:
./run.sh              # Linux/macOS
run.bat               # Windows
mvn spring-boot:run   # Direct Maven
```

### Access Your Application

Once running, open your browser:

**Main Application:**
```
http://localhost:8080
```

**Demo Pages (Static HTML):**
- Dashboard: http://localhost:8080/demo.html
- Today's Review: http://localhost:8080/today-review.html
- Question List: http://localhost:8080/question-list.html
- Statistics: http://localhost:8080/statistics.html

### Verify It's Working

Look for this in the console:
```
Started LeetCodeTrackerApplication in X.XXX seconds
Tomcat started on port 8080 (http)
```

## 📁 What's in Your Repository

Your repo contains:

### Backend Code (Spring Boot)
- `src/main/java/` - All Java source code
  - `entity/` - Database models (User, Question, Review, etc.)
  - `repository/` - Database access layer
  - `service/` - Business logic (Ebbinghaus algorithm, review scheduling)
  - `views/` - Vaadin UI pages
  - `scheduler/` - Background tasks

### Frontend Code
- `*.html` - Static HTML pages (demo.html, question-list.html, etc.)
- `styles/` - CSS stylesheets
- `js/` - JavaScript files for interactivity

### Configuration
- `pom.xml` - Maven dependencies and build configuration
- `src/main/resources/application.yml` - Application settings
- `src/main/resources/schema.sql` - Database schema
- `src/main/resources/data.sql` - Sample data (10 LeetCode problems)

### Setup Scripts
- `setup.sh` / `setup.bat` - Automated setup scripts
- `run.sh` / `run.bat` - Application startup scripts

### Documentation (All Created for You!)
- `README.md` - Comprehensive documentation
- `QUICKSTART.md` - Quick reference guide
- `SETUP.md` - Detailed setup instructions
- `FILE_STRUCTURE.md` - Code organization explanation
- `LeetCode_Memory_Tracker_Documentation.md` - Technical documentation

## 🎮 What You Can Do After Starting

1. **Explore Sample Data**
   - 10 LeetCode problems pre-loaded
   - Demo user account created
   - Sample review history

2. **Add Your Own Questions**
   - Navigate to Question List
   - Click "Add Question"
   - Enter LeetCode problem details

3. **Start Learning**
   - Click "Start Review" on any question
   - The Ebbinghaus algorithm schedules optimal review times
   - Build your study streak!

4. **Track Progress**
   - Dashboard shows overview statistics
   - Statistics page shows detailed analytics
   - Review history tracked automatically

## 🔧 Common Issues & Solutions

### Issue: Can't connect to MySQL
**Solution:** Make sure MySQL is running
```bash
sudo systemctl start mysql     # Linux
brew services start mysql      # macOS
```

### Issue: Port 8080 already in use
**Solution:** Change port in `src/main/resources/application.yml`:
```yaml
server:
  port: 8081
```

### Issue: Database doesn't exist
**Solution:**
```bash
mysql -u root -p
CREATE DATABASE leetcode_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
exit;
```

### Issue: Wrong MySQL credentials
**Solution:** Update `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    username: your_username
    password: your_password
```

## 📊 Application Features

### Ebbinghaus Forgetting Curve
The app uses scientifically-proven spaced repetition:
- **Day 1** - First review
- **Day 2** - Second review
- **Day 4** - Third review
- **Day 7** - One week review
- **Day 15** - Two week review
- **Day 30** - One month review

### Review Ratings
- **FORGOT** - Completely forgot, restart from day 1
- **HARD** - Difficult, repeat current interval
- **GOOD** - Understood, advance to next interval
- **EASY** - Very easy, skip ahead

### Statistics Tracking
- Total questions studied
- Total reviews completed
- Mastery rate percentage
- Current learning streak
- Longest streak achieved

## 📚 Where to Learn More

1. **README.md** - Main documentation with all details
2. **SETUP.md** - Step-by-step setup guide
3. **QUICKSTART.md** - Quick reference
4. **LeetCode_Memory_Tracker_Documentation.md** - Full technical docs

## 🎉 Summary

**To run your application:**

1. Install Java 17+, Maven 3.6+, MySQL 8.0+
2. Run `./setup.sh` (or `setup.bat` on Windows)
3. Run `./run.sh` (or `run.bat` on Windows)
4. Open `http://localhost:8080` in your browser
5. Start tracking your LeetCode progress!

**That's it!** 🚀

The application will:
- Create database tables automatically
- Load 10 sample LeetCode problems
- Create a demo user account
- Be ready for you to use immediately

---

## 🆘 Need Help?

If anything doesn't work:
1. Check the troubleshooting sections in README.md and SETUP.md
2. Verify all prerequisites are installed correctly
3. Check that MySQL is running
4. Make sure port 8080 is available
5. Review the console output for error messages

Happy coding and good luck with your LeetCode preparation! 🎯
