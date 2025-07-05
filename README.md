# Backend Service

A Spring Boot backend service with JPA, PostgreSQL, and Swagger documentation.

## 🚀 Quick Start

### Prerequisites
- Java 17
- Maven 3.6+
- PostgreSQL 17.5
- IntelliJ IDEA (recommended)

### Running the Application

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd backend-service
   ```

2. **Start PostgreSQL database**
   ```bash
   # Using Docker (recommended)
   docker-compose up -d
   
   # Or start your local PostgreSQL instance
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - API Documentation: http://localhost:8080/swagger-ui.html
   - Health Check: http://localhost:8080/actuator/health
   - User API: http://localhost:8080/user/list

## 📁 Project Structure

```
backend-service/
├── src/
│   ├── main/
│   │   ├── java/thinh/springboot/
│   │   │   ├── BackendServiceApplication.java
│   │   │   ├── config/
│   │   │   │   └── OpenApiConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── UserController.java
│   │   │   │   ├── request/
│   │   │   │   └── response/
│   │   │   │       └── UserResponse.java
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-test.yml
│   │       └── application-prod.yml
│   └── test/
├── docker-compose.yml
├── Dockerfile
└── pom.xml
```

## ⚙️ Configuration

### Profiles
- **dev** (default): Development environment
- **test**: Testing environment  
- **prod**: Production environment

### Database Configuration
The application uses PostgreSQL with the following default settings:
- Host: localhost
- Port: 5432
- Database: backend_service
- Username: postgres
- Password: password

## 🔧 Important Notes

### 1. Lombok Configuration

**⚠️ CRITICAL: Maven Compiler Plugin Configuration**

The project uses Lombok for reducing boilerplate code. The Maven compiler plugin must be properly configured with the Lombok version:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <annotationProcessorPaths>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>  <!-- This line is REQUIRED -->
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

**Without the version specification, you'll get compilation errors like:**
```
symbol: method setId(long)
location: variable userResponse1 of type thinh.springboot.controller.response.UserResponse
```

### 2. IntelliJ IDEA Setup

To use Lombok in IntelliJ IDEA:

1. **Install Lombok Plugin**
   - Go to `File` → `Settings` → `Plugins`
   - Search for "Lombok" and install it
   - Restart IntelliJ

2. **Enable Annotation Processing**
   - Go to `File` → `Settings` → `Build, Execution, Deployment` → `Compiler` → `Annotation Processors`
   - Check "Enable annotation processing"
   - Apply and restart

3. **Invalidate Caches** (if needed)
   - Go to `File` → `Invalidate Caches and Restart`

### 3. Port Conflicts

If you get the error "Port 8080 was already in use":

**Option 1: Kill the process using port 8080**
```bash
# Find the process
netstat -ano | findstr :8080

# Kill the process (replace PID with the actual process ID)
taskkill /PID <PID> /F
```

**Option 2: Change the port in application.yml**
```yaml
server:
  port: 8081  # or any other available port
```

**Option 3: Use a different port when running**
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

## 🛠️ Development

### Available Endpoints

- `GET /user/list` - Get list of users with pagination
  - Query parameters:
    - `keyword` (optional): Search keyword
    - `page` (default: 0): Page number
    - `size` (default: 20): Page size

### Adding New Features

1. **Create Model Classes** in `src/main/java/thinh/springboot/model/`
2. **Create Repository Interfaces** in `src/main/java/thinh/springboot/repository/`
3. **Create Service Classes** in `src/main/java/thinh/springboot/service/`
4. **Create Controllers** in `src/main/java/thinh/springboot/controller/`
5. **Create Request/Response DTOs** in respective packages

### Using Lombok

Common Lombok annotations used in this project:

```java
@Getter @Setter  // Generate getters and setters
@Data            // Combines @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor
@NoArgsConstructor  // Generate default constructor
@AllArgsConstructor // Generate constructor with all fields
@Builder          // Generate builder pattern
```

## 🐳 Docker

### Build and Run with Docker

```bash
# Build the image
docker build -t backend-service .

# Run the container
docker run -p 8080:8080 backend-service
```

### Using Docker Compose

```bash
# Start all services (database + application)
docker-compose up -d

# Stop all services
docker-compose down
```

## 📝 API Documentation

The API documentation is automatically generated using Swagger/OpenAPI 3. Access it at:
- http://localhost:8080/swagger-ui.html

## 🔍 Troubleshooting

### Common Issues

1. **Lombok not working in IDE**
   - Ensure Lombok plugin is installed
   - Enable annotation processing
   - Invalidate caches and restart

2. **Database connection issues**
   - Check if PostgreSQL is running
   - Verify database credentials in `application-dev.yml`
   - Ensure database `backend_service` exists

3. **Port already in use**
   - Use the port killing commands above
   - Or change the port in configuration

4. **Compilation errors with Lombok**
   - Verify Maven compiler plugin configuration
   - Ensure `${lombok.version}` is specified in annotation processor path

## 📄 License

This project is licensed under the MIT License.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request 