#!/bin/bash

# School Management System - Build and Run Script

echo "=========================================="
echo "School Management System"
echo "=========================================="

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed or not in PATH"
    echo "Please install Maven first:"
    echo "  - macOS: brew install maven"
    echo "  - Ubuntu/Debian: sudo apt-get install maven"
    echo "  - Windows: Download from https://maven.apache.org/download.cgi"
    exit 1
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 11 or higher"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 11 ]; then
    echo "Error: Java 11 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "Java version: $(java -version 2>&1 | head -n 1)"
echo "Maven version: $(mvn -version | head -n 1)"
echo ""

# Function to build the project
build_project() {
    echo "Building the project..."
    mvn clean compile
    if [ $? -eq 0 ]; then
        echo "✅ Build successful!"
    else
        echo "❌ Build failed!"
        exit 1
    fi
}

# Function to run the application
run_application() {
    echo "Starting the application..."
    mvn exec:java -Dexec.mainClass="com.schoolmanagement.SchoolManagementSystem"
}

# Function to package the application
package_application() {
    echo "Packaging the application..."
    mvn clean package
    if [ $? -eq 0 ]; then
        echo "✅ Package created successfully!"
        echo "JAR file location: target/school-management-system-1.0.0.jar"
    else
        echo "❌ Packaging failed!"
        exit 1
    fi
}

# Function to run tests
run_tests() {
    echo "Running tests..."
    mvn test
    if [ $? -eq 0 ]; then
        echo "✅ All tests passed!"
    else
        echo "❌ Some tests failed!"
        exit 1
    fi
}

# Function to setup database
setup_database() {
    echo "Setting up database..."
    echo "Please ensure MySQL is running and you have the necessary permissions."
    echo "You can run the schema.sql file to create the database structure."
    echo ""
    echo "To setup the database manually:"
    echo "1. Start MySQL server"
    echo "2. Run: mysql -u root -p < schema.sql"
    echo "3. Update database configuration in DatabaseConfig.java if needed"
}

# Function to show help
show_help() {
    echo "Usage: $0 [command]"
    echo ""
    echo "Commands:"
    echo "  build     - Build the project"
    echo "  run       - Run the application"
    echo "  package   - Package the application into JAR"
    echo "  test      - Run tests"
    echo "  setup-db  - Show database setup instructions"
    echo "  help      - Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 build"
    echo "  $0 run"
    echo "  $0 package"
}

# Main script logic
case "${1:-run}" in
    "build")
        build_project
        ;;
    "run")
        build_project
        run_application
        ;;
    "package")
        package_application
        ;;
    "test")
        run_tests
        ;;
    "setup-db")
        setup_database
        ;;
    "help"|"-h"|"--help")
        show_help
        ;;
    *)
        echo "Unknown command: $1"
        show_help
        exit 1
        ;;
esac
