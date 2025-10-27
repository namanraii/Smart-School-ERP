@echo off
REM School Management System - Windows Build and Run Script

echo ==========================================
echo School Management System
echo ==========================================

REM Check if Maven is installed
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo Error: Maven is not installed or not in PATH
    echo Please install Maven first from https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

REM Check if Java is installed
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java 11 or higher
    pause
    exit /b 1
)

echo Java version:
java -version
echo.
echo Maven version:
mvn -version
echo.

REM Function to build the project
:build
echo Building the project...
call mvn clean compile
if %errorlevel% equ 0 (
    echo Build successful!
) else (
    echo Build failed!
    pause
    exit /b 1
)
goto :eof

REM Function to run the application
:run
echo Starting the application...
call mvn exec:java -Dexec.mainClass="com.schoolmanagement.SchoolManagementSystem"
goto :eof

REM Function to package the application
:package
echo Packaging the application...
call mvn clean package
if %errorlevel% equ 0 (
    echo Package created successfully!
    echo JAR file location: target\school-management-system-1.0.0.jar
) else (
    echo Packaging failed!
    pause
    exit /b 1
)
goto :eof

REM Function to run tests
:test
echo Running tests...
call mvn test
if %errorlevel% equ 0 (
    echo All tests passed!
) else (
    echo Some tests failed!
    pause
    exit /b 1
)
goto :eof

REM Function to setup database
:setup-db
echo Setting up database...
echo Please ensure MySQL is running and you have the necessary permissions.
echo You can run the schema.sql file to create the database structure.
echo.
echo To setup the database manually:
echo 1. Start MySQL server
echo 2. Run: mysql -u root -p ^< schema.sql
echo 3. Update database configuration in DatabaseConfig.java if needed
pause
goto :eof

REM Function to show help
:help
echo Usage: %0 [command]
echo.
echo Commands:
echo   build     - Build the project
echo   run       - Run the application
echo   package   - Package the application into JAR
echo   test      - Run tests
echo   setup-db  - Show database setup instructions
echo   help      - Show this help message
echo.
echo Examples:
echo   %0 build
echo   %0 run
echo   %0 package
pause
goto :eof

REM Main script logic
if "%1"=="build" (
    call :build
) else if "%1"=="run" (
    call :build
    call :run
) else if "%1"=="package" (
    call :package
) else if "%1"=="test" (
    call :test
) else if "%1"=="setup-db" (
    call :setup-db
) else if "%1"=="help" (
    call :help
) else if "%1"=="" (
    call :build
    call :run
) else (
    echo Unknown command: %1
    call :help
    exit /b 1
)

pause
