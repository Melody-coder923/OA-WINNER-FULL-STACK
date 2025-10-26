# 🚀 OA WINNER - Complete Setup Guide

This guide will walk you through setting up the OA WINNER application step-by-step.

## ⚡ Quick Setup (Recommended)

If you want to get started quickly, we've created automated setup scripts:

### Linux/macOS

```bash
chmod +x setup.sh
./setup.sh
```

### Windows

```cmd
setup.bat
```

These scripts will:
1. Check prerequisites (Java, Maven, MySQL)
2. Start MySQL service
3. Create the database
4. Build the application
5. Start the server

## 📋 Manual Setup

If you prefer to set up manually or the automated script doesn't work for your system:

### Step 1: Install Prerequisites

#### Install Java 17+

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
java -version
```

**macOS (with Homebrew):**
```bash
brew install openjdk@17
java -version
```

**Windows:**
- Download from [Adoptium](https://adoptium.net/)
- Run installer and add to PATH

#### Install Maven

**Ubuntu/Debian:**
```bash
sudo apt install maven
mvn --version
```

**macOS (with Homebrew):**
```bash
brew install maven
mvn --version
```

**Windows:**
- Download from [Apache Maven](https://maven.apache.org/download.cgi)
- Extract and add to PATH

#### Install MySQL 8.0+

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
sudo systemctl status mysql
```

**macOS (with Homebrew):**
```bash
brew install mysql
brew services start mysql
```

**Windows:**
- Download from [MySQL Downloads](https://dev.mysql.com/downloads/mysql/)
- Run installer and start the service

### Step 2: Configure MySQL

#### Start MySQL Service

**Linux:**
```bash
sudo systemctl start mysql
sudo systemctl enable mysql  # Start on boot
```

**macOS:**
```bash
brew services start mysql
```

**Windows:**
- Use Services panel or:
```cmd
net start MySQL
```

#### Create Database and User

**Option A: Using MySQL CLI (Recommended)**

```bash
# Connect to MySQL
sudo mysql -u root

# Or if you have a root password:
mysql -u root -p
```

Then run these SQL commands:

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS leetcode_tracker 
  CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci;

-- Set root password (if not already set)
ALTER USER 'root'@'localhost' IDENTIFIED BY 'password';

-- Or create a dedicated user (recommended for production)
CREATE USER 'tracker_user'@'localhost' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON leetcode_tracker.* TO 'tracker_user'@'localhost';
FLUSH PRIVILEGES;

-- Verify database creation
SHOW DATABASES LIKE 'leetcode_tracker';

-- Exit
exit;
```

**Option B: Using the SQL Script**

```bash
# On Linux/macOS
mysql -u root -p < src/main/resources/schema.sql

# On Windows
mysql -u root -p < src\main\resources\schema.sql
```

### Step 3: Configure Application

Edit `src/main/resources/application.yml` to match your MySQL credentials:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/leetcode_tracker?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root              # Your MySQL username
    password: password          # Your MySQL password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

**If you created a dedicated user:**

```yaml
spring:
  datasource:
    username: tracker_user
    password: your_secure_password
```

### Step 4: Build the Application

```bash
# Navigate to project directory
cd OA-WINNER-FULL-STACK

# Clean and compile
mvn clean compile

# Or full build with tests
mvn clean install
```

**Expected output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX.XXX s
```

### Step 5: Run the Application

**Option A: Using Startup Scripts**

Linux/macOS:
```bash
chmod +x run.sh
./run.sh
```

Windows:
```cmd
run.bat
```

**Option B: Using Maven**

```bash
mvn spring-boot:run
```

**Option C: Using JAR File**

```bash
# Build JAR
mvn clean package -DskipTests

# Run JAR
java -jar target/memory-tracker-1.0.0.jar
```

### Step 6: Access the Application

Open your browser and navigate to:

**Main Application (Vaadin UI):**
```
http://localhost:8080
```

**Static Pages:**
- Dashboard: `http://localhost:8080/demo.html`
- Today's Review: `http://localhost:8080/today-review.html`
- Question List: `http://localhost:8080/question-list.html`
- Statistics: `http://localhost:8080/statistics.html`

**Expected startup logs:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

[INFO] Started LeetCodeTrackerApplication in X.XXX seconds
[INFO] Tomcat started on port(s): 8080 (http)
```

## ✅ Verification Checklist

After setup, verify everything is working:

- [ ] MySQL service is running
  ```bash
  # Linux
  sudo systemctl status mysql
  
  # macOS
  brew services list | grep mysql
  
  # Windows
  sc query MySQL
  ```

- [ ] Database exists
  ```bash
  mysql -u root -p -e "SHOW DATABASES LIKE 'leetcode_tracker';"
  ```

- [ ] Application compiles
  ```bash
  mvn clean compile
  ```

- [ ] Application starts
  ```bash
  mvn spring-boot:run
  ```

- [ ] Can access web interface
  - Open `http://localhost:8080` in browser
  - Should see the application UI

- [ ] Database tables are created
  ```bash
  mysql -u root -p leetcode_tracker -e "SHOW TABLES;"
  ```
  Should show: `question`, `question_tags`, `review_history`, `user`, `user_question_review`

- [ ] Sample data is loaded
  ```bash
  mysql -u root -p leetcode_tracker -e "SELECT COUNT(*) FROM question;"
  ```
  Should show 10 sample questions

## 🔧 Common Issues and Solutions

### Issue: "Port 8080 already in use"

**Solution 1:** Change the port in `application.yml`:
```yaml
server:
  port: 8081
```

**Solution 2:** Kill the process using port 8080:
```bash
# Linux/macOS
lsof -ti:8080 | xargs kill -9

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Issue: "Access denied for user 'root'@'localhost'"

**Solution:** Reset MySQL root password:
```bash
# Linux
sudo mysql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'password';
FLUSH PRIVILEGES;
exit;

# Then update application.yml with the password
```

### Issue: "Database 'leetcode_tracker' doesn't exist"

**Solution:**
```bash
mysql -u root -p
CREATE DATABASE leetcode_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
exit;
```

### Issue: Maven build fails

**Solutions:**

1. **Clear Maven cache:**
   ```bash
   mvn clean
   rm -rf ~/.m2/repository
   mvn install
   ```

2. **Update Maven:**
   - Download latest version from [maven.apache.org](https://maven.apache.org/)

3. **Check Java version:**
   ```bash
   java -version
   # Must be 17 or higher
   ```

### Issue: MySQL won't start

**Linux:**
```bash
# Check logs
sudo journalctl -u mysql -n 50

# Try reinstalling
sudo apt-get remove --purge mysql-server mysql-client mysql-common
sudo apt-get install mysql-server
```

**macOS:**
```bash
# Check logs
brew services list
tail -f /usr/local/var/mysql/*.err

# Restart
brew services restart mysql
```

### Issue: Vaadin frontend build fails

**Solution:**
```bash
# Clear Vaadin cache
rm -rf node_modules/
rm -rf target/
mvn clean install -DskipTests
```

## 🎯 Next Steps

Once setup is complete:

1. **Explore the Demo Data**
   - Log in with demo user (auto-created)
   - View sample questions
   - See example reviews

2. **Add Your Questions**
   - Navigate to Question List
   - Click "Add Question"
   - Enter LeetCode problem details

3. **Start Reviewing**
   - Go to "Today's Review"
   - Complete reviews
   - Build your streak!

4. **Check Statistics**
   - View your progress
   - Analyze learning patterns
   - Track mastery levels

## 📚 Additional Resources

- [README.md](README.md) - Main documentation
- [FILE_STRUCTURE.md](FILE_STRUCTURE.md) - Project structure
- [LeetCode_Memory_Tracker_Documentation.md](LeetCode_Memory_Tracker_Documentation.md) - Technical details

## 🆘 Getting Help

If you encounter issues not covered here:

1. Check the [Troubleshooting section in README.md](README.md#troubleshooting)
2. Review application logs
3. Check MySQL logs
4. Verify all prerequisites are correctly installed
5. Create a GitHub issue with:
   - Operating system
   - Java version
   - Maven version
   - MySQL version
   - Complete error message
   - Steps to reproduce

---

**Happy Learning! 🚀**
