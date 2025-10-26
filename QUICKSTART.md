# ⚡ Quick Start Guide - OA WINNER

Get your LeetCode Memory Tracker up and running in 5 minutes!

## 🎯 Prerequisites Checklist

Before starting, ensure you have:
- [ ] Java 17+ installed (`java -version`)
- [ ] Maven 3.6+ installed (`mvn --version`)
- [ ] MySQL 8.0+ installed (`mysql --version`)

## 🚀 5-Minute Setup

### Option 1: Automated Setup (Recommended)

**On Linux/macOS:**
```bash
chmod +x setup.sh
./setup.sh
```

**On Windows:**
```cmd
setup.bat
```

The script will:
1. ✓ Verify prerequisites
2. ✓ Start MySQL service
3. ✓ Create database
4. ✓ Build the application

### Option 2: Manual Setup

#### Step 1: Start MySQL
```bash
# Linux
sudo systemctl start mysql

# macOS
brew services start mysql

# Windows - MySQL should auto-start
```

#### Step 2: Create Database
```bash
mysql -u root -p
```
```sql
CREATE DATABASE leetcode_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER USER 'root'@'localhost' IDENTIFIED BY 'password';
exit;
```

#### Step 3: Build & Run
```bash
# Build
mvn clean compile

# Run
./run.sh          # Linux/macOS
# or
run.bat           # Windows
# or
mvn spring-boot:run
```

## 🌐 Access the Application

Open your browser and go to:

**Main Application (Vaadin UI):**
```
http://localhost:8080
```

**Static HTML Pages:**
- Dashboard: http://localhost:8080/demo.html
- Today's Review: http://localhost:8080/today-review.html
- Question List: http://localhost:8080/question-list.html
- Statistics: http://localhost:8080/statistics.html

## ✅ Verify It's Working

1. **Check the console output** - Look for:
   ```
   Started LeetCodeTrackerApplication in X.XXX seconds
   Tomcat started on port 8080 (http)
   ```

2. **Access the URL** - You should see the OA WINNER interface

3. **Check sample data**:
   ```bash
   mysql -u root -ppassword leetcode_tracker -e "SELECT COUNT(*) FROM question;"
   ```
   Should show: 10 sample questions

## 🎮 First Steps

1. **Explore Demo Data**
   - The app comes with 10 sample LeetCode problems
   - A demo user is pre-created

2. **Add Your First Question**
   - Go to "Question List"
   - Click "Add Question"
   - Fill in details (title, difficulty, URL)

3. **Start Your First Review**
   - Select a question
   - Click "Start Review"
   - The Ebbinghaus algorithm will schedule it

4. **Track Your Progress**
   - Visit "Dashboard" for overview
   - Check "Statistics" for detailed analytics

## 🛑 Troubleshooting

### Problem: Port 8080 in use
**Quick Fix:** Change port in `src/main/resources/application.yml`:
```yaml
server:
  port: 8081
```

### Problem: Can't connect to MySQL
**Quick Fix:**
```bash
# Check MySQL is running
sudo systemctl status mysql    # Linux
brew services list             # macOS

# Restart MySQL
sudo systemctl restart mysql   # Linux
brew services restart mysql    # macOS
```

### Problem: Database error
**Quick Fix:**
```bash
mysql -u root -p
CREATE DATABASE leetcode_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
exit;
```

## 📚 Next Steps

- Read [README.md](README.md) for complete documentation
- Check [SETUP.md](SETUP.md) for detailed setup guide
- Review [LeetCode_Memory_Tracker_Documentation.md](LeetCode_Memory_Tracker_Documentation.md) for technical details

## 💡 Pro Tips

1. **Keep MySQL Running**
   ```bash
   sudo systemctl enable mysql  # Auto-start on Linux
   ```

2. **Quick Restart**
   ```bash
   # Stop: Ctrl+C in terminal
   # Start: ./run.sh or run.bat
   ```

3. **View Logs**
   - Application logs appear in the terminal
   - MySQL logs: `/var/log/mysql/error.log` (Linux)

4. **Backup Your Data**
   ```bash
   mysqldump -u root -ppassword leetcode_tracker > backup.sql
   ```

## 🎉 You're Ready!

The application is now running and you can start tracking your LeetCode progress!

**Default Configuration:**
- Database: `leetcode_tracker`
- Username: `root`
- Password: `password`
- Port: `8080`

**Demo User:**
- Username: `demo`
- Email: `demo@example.com`

Happy coding! 🚀

---

**Need Help?** Check the [troubleshooting section](README.md#troubleshooting) or create a GitHub issue.
