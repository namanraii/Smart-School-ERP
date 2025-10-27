# School Management System

A comprehensive school management system built with Java Swing, JDBC, and MySQL. This system provides a complete solution for managing students, teachers, classes, grades, attendance, and more.

## Features

- **User Authentication**: Secure login system with role-based access
- **Role-based Interface**: Different interfaces for Admin, Teacher, Student, and Parent
- **Database Integration**: MySQL database with connection pooling
- **Modern UI**: Java Swing with system look and feel
- **Security**: Password hashing with BCrypt
- **Logging**: Comprehensive logging with SLF4J and Logback

## Prerequisites

- **Java 11 or higher**
- **Maven 3.6 or higher**
- **MySQL 8.0 or higher**

## Installation

### 1. Clone or Download the Project

```bash
git clone <repository-url>
cd school-management-system
```

### 2. Database Setup

1. Start your MySQL server
2. Create the database and tables by running the schema file:

```bash
mysql -u root -p < schema.sql
```

3. Update database configuration in `src/main/java/com/schoolmanagement/config/DatabaseConfig.java` if needed:
   - Database host (default: localhost)
   - Database port (default: 3306)
   - Database name (default: school_management_system)
   - Username (default: root)
   - Password (default: password)

### 3. Build and Run

#### Using the provided scripts:

**Linux/macOS:**
```bash
./run.sh
```

**Windows:**
```cmd
run.bat
```

#### Using Maven directly:

```bash
# Build the project
mvn clean compile

# Run the application
mvn exec:java -Dexec.mainClass="com.schoolmanagement.SchoolManagementSystem"

# Package the application
mvn clean package
```

## Default Login Credentials

- **Username**: admin
- **Password**: admin123

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/schoolmanagement/
│   │       ├── SchoolManagementSystem.java    # Main application class
│   │       ├── config/
│   │       │   ├── AppConfig.java             # Application configuration
│   │       │   └── DatabaseConfig.java        # Database configuration
│   │       ├── dao/
│   │       │   └── UserDAO.java               # User data access object
│   │       ├── model/
│   │       │   └── User.java                  # User model
│   │       ├── ui/
│   │       │   ├── LoginWindow.java          # Login interface
│   │       │   └── MainWindow.java           # Main application interface
│   │       └── util/
│   │           └── PasswordUtil.java         # Password utilities
│   └── resources/
│       └── application.properties            # Application properties
├── test/
│   └── java/                                 # Test classes
schema.sql                                     # Database schema
pom.xml                                        # Maven configuration
run.sh                                         # Linux/macOS run script
run.bat                                        # Windows run script
```

## Database Schema

The system includes a comprehensive database schema with the following main tables:

- **users**: User authentication and basic information
- **students**: Student-specific information
- **teachers**: Teacher-specific information
- **classes**: Class/grade information
- **subjects**: Subject information
- **grades**: Student grades and results
- **attendance**: Student attendance records
- **fees**: Fee management
- **notices**: System announcements

## User Roles

1. **Admin**: Full system access, can manage all users, classes, and system settings
2. **Teacher**: Can manage classes, grades, and attendance for assigned classes
3. **Student**: Can view their own profile, grades, and attendance
4. **Parent**: Can view their child's information, grades, and attendance

## Configuration

### Database Configuration

Update the database connection settings in `DatabaseConfig.java`:

```java
props.setProperty("dataSource.serverName", "localhost");
props.setProperty("dataSource.port", "3306");
props.setProperty("dataSource.databaseName", "school_management_system");
props.setProperty("dataSource.user", "root");
props.setProperty("dataSource.password", "your_password");
```

### Application Properties

Create `src/main/resources/application.properties` to override default settings:

```properties
app.name=School Management System
app.version=1.0.0
database.host=localhost
database.port=3306
database.name=school_management_system
database.username=root
database.password=your_password
```

## Development

### Adding New Features

1. Create model classes in `src/main/java/com/schoolmanagement/model/`
2. Create DAO classes in `src/main/java/com/schoolmanagement/dao/`
3. Create UI components in `src/main/java/com/schoolmanagement/ui/`
4. Update the database schema in `schema.sql`
5. Add appropriate tests in `src/test/java/`

### Database Updates

When adding new features that require database changes:

1. Update the `schema.sql` file with new tables or modifications
2. Create migration scripts if needed
3. Update the corresponding model and DAO classes
4. Test the changes thoroughly

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Ensure MySQL is running
   - Check database credentials in `DatabaseConfig.java`
   - Verify the database exists

2. **Build Failures**
   - Ensure Java 11+ is installed
   - Ensure Maven is installed and in PATH
   - Check for any compilation errors

3. **Login Issues**
   - Verify the default admin user exists in the database
   - Check password hashing implementation

### Logs

The application uses SLF4J with Logback for logging. Logs are written to the console by default. Check the console output for any error messages.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Update documentation
6. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions, please create an issue in the repository or contact the development team.
