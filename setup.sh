#!/bin/bash

# OA WINNER - Automated Setup Script for Linux/macOS
# This script automates the setup process for the LeetCode Memory Tracker

set -e  # Exit on error

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║         🏆 OA WINNER - Automated Setup Script 🏆             ║"
echo "║              LeetCode Memory Tracker Setup                     ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

# Step 1: Check Prerequisites
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Step 1: Checking Prerequisites"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Check Java
print_info "Checking Java installation..."
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d. -f1)
    if [ "$JAVA_VERSION" -ge 17 ]; then
        print_success "Java $JAVA_VERSION is installed"
    else
        print_error "Java 17 or higher is required. Found Java $JAVA_VERSION"
        echo "Please install Java 17+ from: https://adoptium.net/"
        exit 1
    fi
else
    print_error "Java is not installed"
    echo "Please install Java 17+ from: https://adoptium.net/"
    exit 1
fi

# Check Maven
print_info "Checking Maven installation..."
if command -v mvn &> /dev/null; then
    MVN_VERSION=$(mvn --version | head -n 1 | awk '{print $3}')
    print_success "Maven $MVN_VERSION is installed"
else
    print_error "Maven is not installed"
    echo "Please install Maven from: https://maven.apache.org/download.cgi"
    exit 1
fi

# Check MySQL
print_info "Checking MySQL installation..."
if command -v mysql &> /dev/null; then
    MYSQL_VERSION=$(mysql --version | awk '{print $5}' | cut -d, -f1)
    print_success "MySQL $MYSQL_VERSION is installed"
else
    print_error "MySQL is not installed"
    echo "Please install MySQL 8.0+ from: https://dev.mysql.com/downloads/mysql/"
    exit 1
fi

echo ""

# Step 2: Start MySQL Service
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Step 2: Starting MySQL Service"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Detect OS and start MySQL accordingly
if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    print_info "Detected Linux system"
    if command -v systemctl &> /dev/null; then
        print_info "Starting MySQL service..."
        sudo systemctl start mysql 2>/dev/null || sudo service mysql start 2>/dev/null || true
        sleep 2
        if sudo systemctl is-active --quiet mysql 2>/dev/null || sudo service mysql status &>/dev/null; then
            print_success "MySQL service is running"
        else
            print_warning "Could not verify MySQL service status. Continuing anyway..."
        fi
    fi
elif [[ "$OSTYPE" == "darwin"* ]]; then
    print_info "Detected macOS system"
    if command -v brew &> /dev/null; then
        print_info "Starting MySQL service..."
        brew services start mysql 2>/dev/null || true
        sleep 2
        print_success "MySQL service start command issued"
    fi
else
    print_warning "Unknown OS type. Please start MySQL manually."
fi

echo ""

# Step 3: Configure Database
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Step 3: Configuring Database"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

print_info "Setting up database and user..."

# Try to create database with different authentication methods
DB_CREATED=false

# Method 1: Try with sudo mysql (works on Ubuntu with auth_socket)
if ! $DB_CREATED; then
    if sudo mysql -e "CREATE DATABASE IF NOT EXISTS leetcode_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password'; FLUSH PRIVILEGES;" 2>/dev/null; then
        DB_CREATED=true
        print_success "Database created successfully (using sudo mysql)"
    fi
fi

# Method 2: Try with debian-sys-maint
if ! $DB_CREATED && [ -f /etc/mysql/debian.cnf ]; then
    if sudo mysql --defaults-file=/etc/mysql/debian.cnf -e "CREATE DATABASE IF NOT EXISTS leetcode_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password'; FLUSH PRIVILEGES;" 2>/dev/null; then
        DB_CREATED=true
        print_success "Database created successfully (using debian-sys-maint)"
    fi
fi

# Method 3: Try with root password
if ! $DB_CREATED; then
    print_info "Please enter your MySQL root password (or press Enter if none):"
    read -s MYSQL_ROOT_PASS
    echo ""
    
    if [ -z "$MYSQL_ROOT_PASS" ]; then
        if mysql -u root -e "CREATE DATABASE IF NOT EXISTS leetcode_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null; then
            DB_CREATED=true
            print_success "Database created successfully (no password)"
            
            # Set password for future use
            mysql -u root -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password'; FLUSH PRIVILEGES;" 2>/dev/null || true
        fi
    else
        if mysql -u root -p"$MYSQL_ROOT_PASS" -e "CREATE DATABASE IF NOT EXISTS leetcode_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null; then
            DB_CREATED=true
            print_success "Database created successfully"
            
            # Update application.yml with provided password
            if [ "$MYSQL_ROOT_PASS" != "password" ]; then
                print_info "Updating application.yml with your MySQL password..."
                sed -i.bak "s/password: password/password: $MYSQL_ROOT_PASS/" src/main/resources/application.yml
            fi
        fi
    fi
fi

if ! $DB_CREATED; then
    print_error "Failed to create database automatically"
    echo ""
    echo "Please create the database manually:"
    echo "  1. Run: mysql -u root -p"
    echo "  2. Execute: CREATE DATABASE leetcode_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
    echo "  3. Execute: ALTER USER 'root'@'localhost' IDENTIFIED BY 'password';"
    echo "  4. Exit MySQL and re-run this script"
    exit 1
fi

# Verify database creation
print_info "Verifying database..."
if mysql -u root -ppassword -e "USE leetcode_tracker;" 2>/dev/null; then
    print_success "Database 'leetcode_tracker' is accessible"
else
    print_warning "Could not verify database access. Please check manually."
fi

echo ""

# Step 4: Build Application
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Step 4: Building Application"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

print_info "Compiling application with Maven..."
print_warning "This may take several minutes on first run..."

if mvn clean compile -DskipTests; then
    print_success "Application compiled successfully"
else
    print_error "Build failed"
    echo "Please check the error messages above and fix any issues."
    exit 1
fi

echo ""

# Step 5: Summary
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "✅ Setup Complete!"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

print_success "All setup steps completed successfully!"
echo ""
echo "📝 Configuration Summary:"
echo "   • Database: leetcode_tracker"
echo "   • Username: root"
echo "   • Password: password (or your provided password)"
echo "   • Port: 8080"
echo ""
echo "🚀 To start the application, run:"
echo "   ./run.sh"
echo ""
echo "   Or use Maven directly:"
echo "   mvn spring-boot:run"
echo ""
echo "🌐 Once started, access the application at:"
echo "   http://localhost:8080"
echo ""
echo "📚 For more information, see:"
echo "   • README.md - Main documentation"
echo "   • SETUP.md - Detailed setup guide"
echo ""
print_success "Happy coding! 🎉"
echo ""
