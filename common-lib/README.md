# Common Library for Spring Boot

A comprehensive common library for Spring Boot applications providing reusable components, utilities, and configurations for faster development.

## Features

- **Exception Handling**: Global exception handler with standardized error responses
- **Response Wrappers**: Consistent API response format
- **Utility Classes**: String, Date, JSON, Validation utilities
- **HTTP Client**: REST template wrapper with error handling
- **JPA Auditing**: Automatic entity auditing support
- **Logging**: Request/Response logging filter
- **Security Utils**: Helper methods for authentication
- **Common Constants**: Shared constants across applications

## Installation

Add this library to your project's `pom.xml`:

```xml
<dependency>
    <groupId>com.lyhorng</groupId>
    <artifactId>common-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

### 1. Enable Auto-Configuration

The library uses Spring Boot auto-configuration. Just ensure the package is scanned:

```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.lyhorng.common", "your.package"})
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
```

### 2. Exception Handling

The library includes a global exception handler that automatically handles common exceptions:

```java
// Throws BusinessException
throw new BusinessException(ErrorCode.NOT_FOUND, "User not found");

// Automatically converted to JSON response:
{
  "success": false,
  "message": "Resource not found",
  "data": {
    "errorCode": "ERR_1004",
    "message": "Resource not found",
    "path": "/api/users/123",
    "timestamp": "2024-01-15T10:30:00"
  }
}
```

### 3. Response Wrappers

Use standardized response format:

```java
@RestController
public class UserController {
    
    @GetMapping("/users/{id}")
    public ApiResponse<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ApiResponse.success(user);
    }
    
    @PostMapping("/users")
    public ApiResponse<User> createUser(@RequestBody @Valid UserRequest request) {
        User user = userService.create(request);
        return ApiResponse.success("User created successfully", user);
    }
}
```

### 4. Pagination

Use PageResponse for paginated results:

```java
@GetMapping("/users")
public ApiResponse<PageResponse<User>> getUsers(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
) {
    PageResponse<User> pageData = userService.findAll(page, size);
    return ApiResponse.success(pageData);
}
```

### 5. Entity Auditing

Extend BaseEntity for automatic auditing:

```java
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    
    // createdAt, updatedAt, createdBy, updatedBy are automatically managed
}
```

### 6. Utility Classes

```java
// String utilities
if (StringUtils.isEmpty(str)) { }
if (StringUtils.isValidEmail(email)) { }
String masked = StringUtils.maskEmail(email);

// Date utilities
String formatted = DateUtils.formatDateTime(LocalDateTime.now());
long days = DateUtils.daysBetween(startDate, endDate);

// JSON utilities
String json = JsonUtils.toJson(object);
User user = JsonUtils.fromJson(json, User.class);

// Validation
ValidationUtils.notNull(obj, "Object cannot be null");
ValidationUtils.notEmpty(list, "List cannot be empty");
ValidationUtils.isTrue(condition, "Condition must be true");
```

### 7. REST Client

Use RestClientUtil for HTTP calls:

```java
@Service
public class ExternalService {
    
    @Autowired
    private RestClientUtil restClient;
    
    public User getUser(Long id) {
        Map<String, String> headers = Map.of("Authorization", "Bearer token");
        return restClient.get("https://api.example.com/users/" + id, headers, User.class);
    }
    
    public User createUser(User user) {
        return restClient.post("https://api.example.com/users", user, User.class);
    }
}
```

### 8. Security Utils

Get current user information:

```java
// Get current username
String username = SecurityUtils.getCurrentUsername();

// Check authentication
if (SecurityUtils.isAuthenticated()) {
    // User is logged in
}

// Check authority
if (SecurityUtils.hasAuthority("ROLE_ADMIN")) {
    // User has admin role
}
```

### 9. Constants

Use predefined constants:

```java
import static com.lyhorng.common.constant.Constants.*;

// API Paths
String path = API_VERSION_V1 + "/users";

// Headers
request.addHeader(HEADER_AUTHORIZATION, token);

// Status
if (status.equals(STATUS_ACTIVE)) { }

// Pagination
int pageSize = DEFAULT_PAGE_SIZE;
```

## Error Codes

The library provides a comprehensive set of error codes:

- **1000-1999**: General Errors
- **2000-2999**: Validation Errors
- **3000-3999**: Business Logic Errors
- **4000-4999**: Data Errors
- **5000-5999**: Authentication & Authorization Errors
- **6000-6999**: External Service Errors

## Configuration

### Disable Auto-Configuration

To disable certain auto-configurations:

```java
@SpringBootApplication(exclude = {
    CommonConfig.class,
    RestTemplateConfig.class
})
public class YourApplication {
    // ...
}
```

### Custom Configuration

Override any bean by defining your own:

```java
@Configuration
public class CustomConfig {
    
    @Bean
    public RestTemplate customRestTemplate() {
        // Your custom configuration
    }
    
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("USER123"); // Get from SecurityContext
    }
}
```

## Features Overview

### Exception Handling
- ✅ Global exception handler
- ✅ Validation errors
- ✅ Business exceptions
- ✅ Standardized error responses

### Response Wrappers
- ✅ ApiResponse for all endpoints
- ✅ PageResponse for pagination
- ✅ ErrorResponse for errors

### Utilities
- ✅ StringUtils (validation, masking, formatting)
- ✅ DateUtils (parsing, formatting, calculations)
- ✅ JsonUtils (conversion, validation)
- ✅ ValidationUtils (null checks, validations)
- ✅ SecurityUtils (authentication helpers)

### Infrastructure
- ✅ REST client wrapper
- ✅ Request/Response logging
- ✅ JPA auditing
- ✅ Common configuration

## Deployment & Version Management

### Deploying New Versions

See [DEPLOYMENT.md](DEPLOYMENT.md) for detailed deployment instructions.

**Quick deploy to local:**
```bash
mvn clean install
```

**Deploy to Maven repository:**
```bash
mvn clean deploy
```

### Version Management

This library uses Semantic Versioning (MAJOR.MINOR.PATCH):
- **1.0.0** → **1.0.1** (bug fixes)
- **1.0.0** → **1.1.0** (new features)
- **1.0.0** → **2.0.0** (breaking changes)

Update version in `pom.xml` before deploying:
```xml
<version>1.0.1</version>
```

### Using Private Git Repository

**Can others use your private Git library?**

✅ **YES!** They need to:
1. Clone the repository
2. Run `mvn clean install`
3. Use the dependency in their projects

See [DEPLOYMENT.md](DEPLOYMENT.md) for complete instructions.

### Adding New Features

See [DEVELOPMENT_GUIDE.md](DEVELOPMENT_GUIDE.md) for:
- How to add new utilities
- How to add new configurations
- Git workflow
- Best practices

## Building the Library

Build the library:

```bash
mvn clean install
```

Install with sources and javadoc:

```bash
mvn clean install -DskipTests
```

## Contributing

Contributions are welcome! Please ensure:
- All tests pass
- Code follows existing style
- Documentation is updated
- See DEVELOPMENT_GUIDE.md for workflow

## Related Documentation

- [README.md](README.md) - This file, overview
- [USAGE_EXAMPLES.md](USAGE_EXAMPLES.md) - Detailed code examples
- [DEVELOPMENT_GUIDE.md](DEVELOPMENT_GUIDE.md) - Adding features and development workflow
- [DEPLOYMENT.md](DEPLOYMENT.md) - Deployment and version management

## License

Copyright © 2024 Lyhorng

