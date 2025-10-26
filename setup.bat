@echo off
REM OA WINNER - Automated Setup Script for Windows
REM This script automates the setup process for the LeetCode Memory Tracker

setlocal enabledelayedexpansion

echo ================================================================
echo          OA WINNER - Automated Setup Script
echo               LeetCode Memory Tracker Setup
echo ================================================================
echo.

REM Step 1: Check Prerequisites
echo ================================================================
echo Step 1: Checking Prerequisites
echo ================================================================

REM Check Java
echo [INFO] Checking Java installation...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java is not installed
    echo Please install Java 17+ from: https://adoptium.net/
    pause
    exit /b 1
) else (
    echo [OK] Java is installed
)

REM Check Maven
echo [INFO] Checking Maven installation...
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Maven is not installed
    echo Please install Maven from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
) else (
    echo [OK] Maven is installed
)

REM Check MySQL
echo [INFO] Checking MySQL installation...
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] MySQL is not installed
    echo Please install MySQL 8.0+ from: https://dev.mysql.com/downloads/mysql/
    pause
    exit /b 1
) else (
    echo [OK] MySQL is installed
)

echo.

REM Step 2: Start MySQL Service
echo ================================================================
echo Step 2: Starting MySQL Service
echo ================================================================

echo [INFO] Starting MySQL service...
net start MySQL >nul 2>&1
if %errorlevel% neq 0 (
    echo [WARNING] Could not start MySQL service automatically
    echo Please start MySQL service manually via Services panel
) else (
    echo [OK] MySQL service is running
)

echo.

REM Step 3: Configure Database
echo ================================================================
echo Step 3: Configuring Database
echo ================================================================

echo [INFO] Setting up database...
echo Please enter your MySQL root password (or press Enter for no password):
set /p MYSQL_PASS=

if "%MYSQL_PASS%"=="" (
    mysql -u root -e "CREATE DATABASE IF NOT EXISTS leetcode_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password'; FLUSH PRIVILEGES;" 2>nul
) else (
    mysql -u root -p%MYSQL_PASS% -e "CREATE DATABASE IF NOT EXISTS leetcode_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>nul
)

if %errorlevel% neq 0 (
    echo [ERROR] Failed to create database
    echo.
    echo Please create the database manually:
    echo   1. Run: mysql -u root -p
    echo   2. Execute: CREATE DATABASE leetcode_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    echo   3. Execute: ALTER USER 'root'@'localhost' IDENTIFIED BY 'password';
    echo   4. Exit MySQL and re-run this script
    pause
    exit /b 1
) else (
    echo [OK] Database created successfully
)

REM Update application.yml if password is not default
if not "%MYSQL_PASS%"=="" (
    if not "%MYSQL_PASS%"=="password" (
        echo [INFO] Updating application.yml with your MySQL password...
        powershell -Command "(gc src\main\resources\application.yml) -replace 'password: password', 'password: %MYSQL_PASS%' | Out-File -encoding ASCII src\main\resources\application.yml"
    )
)

echo.

REM Step 4: Build Application
echo ================================================================
echo Step 4: Building Application
echo ================================================================

echo [INFO] Compiling application with Maven...
echo [WARNING] This may take several minutes on first run...
echo.

call mvn clean compile -DskipTests
if %errorlevel% neq 0 (
    echo [ERROR] Build failed
    echo Please check the error messages above and fix any issues.
    pause
    exit /b 1
) else (
    echo [OK] Application compiled successfully
)

echo.

REM Step 5: Summary
echo ================================================================
echo Setup Complete!
echo ================================================================
echo.
echo [OK] All setup steps completed successfully!
echo.
echo Configuration Summary:
echo    - Database: leetcode_tracker
echo    - Username: root
echo    - Password: password (or your provided password)
echo    - Port: 8080
echo.
echo To start the application, run:
echo    run.bat
echo.
echo    Or use Maven directly:
echo    mvn spring-boot:run
echo.
echo Once started, access the application at:
echo    http://localhost:8080
echo.
echo For more information, see:
echo    - README.md - Main documentation
echo    - SETUP.md - Detailed setup guide
echo.
echo Happy coding!
echo.
pause
