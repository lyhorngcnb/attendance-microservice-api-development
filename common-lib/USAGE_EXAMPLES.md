# Usage Examples

This document provides detailed examples of how to use the Common Library in your Spring Boot applications.

## Table of Contents

1. [Basic Setup](#basic-setup)
2. [Exception Handling](#exception-handling)
3. [API Controllers](#api-controllers)
4. [Service Layer](#service-layer)
5. [Repository Layer](#repository-layer)
6. [Utilities](#utilities)
7. [Complete Example](#complete-example)

## Basic Setup

### 1. Add Dependency

In your `pom.xml`:

```xml
<dependency>
    <groupId>com.lyhorng</groupId>
    <artifactId>common-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Enable Auto-Configuration

Your application will automatically configure:
- Global exception handlers
- REST template
- JPA auditing
- Logging filter

No additional configuration needed!

## Exception Handling

### Throw Business Exceptions

```java
@Service
public class UserService {
    
    public User findById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new BusinessException(
                ErrorCode.NOT_FOUND, 
                "User with id " + id + " not found"
            ));
        return user;
    }
    
    public void validateEmail(String email) {
        if (!StringUtils.isValidEmail(email)) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL, 
                "Invalid email format: " + email);
        }
    }
}
```

### Result
The exception is automatically caught and converted to a JSON response:

```json
{
  "success": false,
  "message": "Resource not found",
  "data": {
    "errorCode": "ERR_1004",
    "message": "Resource not found",
    "details": "User with id 123 not found",
    "path": "/api/users/123",
    "timestamp": "2024-01-15T10:30:00"
  }
}
```

## API Controllers

### Simple CRUD Controller

```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ApiResponse<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ApiResponse.success(user);
    }
    
    @PostMapping
    public ApiResponse<User> createUser(@RequestBody @Valid CreateUserRequest request) {
        User user = userService.create(request);
        return ApiResponse.success("User created successfully", user);
    }
    
    @PutMapping("/{id}")
    public ApiResponse<User> updateUser(@PathVariable Long id, 
                                       @RequestBody @Valid UpdateUserRequest request) {
        User user = userService.update(id, request);
        return ApiResponse.success("User updated successfully", user);
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ApiResponse.successNoData("User deleted successfully");
    }
}
```

### Pagination Example

```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    @GetMapping
    public ApiResponse<PageResponse<User>> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) String name
    ) {
        PageResponse<User> result = userService.findAll(page, size, name);
        return ApiResponse.success(result);
    }
}
```

## Service Layer

### Using BaseService

```java
@Service
@Transactional
public class UserService implements BaseService<User, Long> {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, 
                "User not found"));
    }
    
    @Override
    public User create(User user) {
        ValidationUtils.notNull(user, "User cannot be null");
        ValidationUtils.notEmpty(user.getEmail(), "Email is required");
        ValidationUtils.validEmail(user.getEmail(), "Invalid email format");
        
        return userRepository.save(user);
    }
    
    @Override
    public User update(Long id, User user) {
        User existingUser = findById(id);
        // Update logic
        return userRepository.save(existingUser);
    }
    
    @Override
    public void delete(Long id) {
        findById(id); // Verify exists
        userRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
    
    @Override
    public long count() {
        return userRepository.count();
    }
}
```

## Repository Layer

### Entity with Auditing

```java
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    private String phone;
    
    // Fields from BaseEntity are automatically managed:
    // - createdAt
    // - updatedAt
    // - createdBy
    // - updatedBy
    // - deleted
}
```

### Repository Interface

```java
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    List<User> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT u FROM User u WHERE u.deleted = false")
    Page<User> findAllActive(Pageable pageable);
}
```

## Utilities

### String Utils

```java
// Validation
if (StringUtils.isEmpty(str)) {
    // Handle empty string
}

if (StringUtils.isValidEmail(email)) {
    // Valid email
}

// Masking sensitive data
String masked = StringUtils.maskEmail("john@example.com");
// Result: "jo***n@example.com"

// Formatting
String camelCase = StringUtils.toCamelCase("hello_world");
// Result: "helloWorld"

String snakeCase = StringUtils.toSnakeCase("helloWorld");
// Result: "hello_world"
```

### Date Utils

```java
// Formatting
String dateStr = DateUtils.formatDate(LocalDate.now());
String datetimeStr = DateUtils.formatDateTime(LocalDateTime.now());

// Parsing
LocalDate date = DateUtils.parseDate("2024-01-15");
LocalDateTime datetime = DateUtils.parseDateTime("2024-01-15 10:30:00");

// Calculations
long days = DateUtils.daysBetween(startDate, endDate);
long hours = DateUtils.hoursBetween(startDateTime, endDateTime);

// Range checks
boolean inRange = DateUtils.isDateInRange(checkDate, startDate, endDate);
```

### JSON Utils

```java
// Convert object to JSON
User user = new User();
String json = JsonUtils.toJson(user);

// Pretty print
String prettyJson = JsonUtils.toJsonPretty(user);

// Parse JSON to object
User parsedUser = JsonUtils.fromJson(json, User.class);

// Convert between types
Map<String, Object> map = JsonUtils.convertValue(user, new TypeReference<Map<String, Object>>() {});

// Validate JSON
boolean valid = JsonUtils.isValidJson(jsonString);
```

### Validation Utils

```java
// Null checks
ValidationUtils.notNull(obj, "Object cannot be null");
ValidationUtils.notEmpty(list, "List cannot be empty");

// Boolean checks
ValidationUtils.isTrue(condition, "Condition must be true");
ValidationUtils.isFalse(condition, "Condition must be false");

// Comparison
ValidationUtils.equals(obj1, obj2, "Objects must be equal");
ValidationUtils.notEquals(obj1, obj2, "Objects must not be equal");

// Range checks
ValidationUtils.inRange(value, 0, 100, "Value must be between 0 and 100");
ValidationUtils.positive(value, "Value must be positive");

// Email/Phone validation
ValidationUtils.validEmail(email, "Invalid email");
ValidationUtils.validPhone(phone, "Invalid phone number");
```

### REST Client

```java
@Service
public class ExternalApiService {
    
    @Autowired
    private RestClientUtil restClient;
    
    public UserDto getUserFromExternal(Long id) {
        String url = "https://api.example.com/users/" + id;
        
        Map<String, String> headers = Map.of(
            "Authorization", "Bearer token",
            "X-Request-ID", UUID.randomUUID().toString()
        );
        
        return restClient.get(url, headers, UserDto.class);
    }
    
    public UserDto createUserExternal(CreateUserDto dto) {
        String url = "https://api.example.com/users";
        return restClient.post(url, dto, UserDto.class);
    }
}
```

### Retry Utils

```java
// Retry on failure
String result = RetryUtil.executeWithRetry(
    () -> callExternalService(),
    3, // max retries
    1000 // delay in milliseconds
);

// Retry on specific exception
User user = RetryUtil.executeWithRetry(
    () -> fetchUser(id),
    5,
    500,
    IOException.class // only retry on IOException
);

// Retry void operations
RetryUtil.executeWithRetry(
    () -> saveData(),
    3,
    1000
);
```

### Collection Utils

```java
// Check if empty
if (CollectionUtils.isEmpty(list)) {
    // Handle empty list
}

// Filter
List<User> activeUsers = CollectionUtils.filter(users, u -> u.isActive());

// Map
List<String> emails = CollectionUtils.map(users, User::getEmail);

// Group by
Map<String, List<User>> usersByCountry = CollectionUtils.groupBy(users, User::getCountry);

// Partition
List<List<User>> batches = CollectionUtils.partition(users, 100);

// Flatten
List<String> allValues = CollectionUtils.flatten(listOfLists);
```

## Complete Example

Here's a complete working example:

### Entity

```java
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    @Column(nullable = false)
    private Integer stock;
    
    // Getters and setters
}
```

### Repository

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContaining(String name, Pageable pageable);
    List<Product> findByStockLessThan(Integer threshold);
}
```

### Service

```java
@Service
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public Product create(Product product) {
        ValidationUtils.notNull(product, "Product cannot be null");
        ValidationUtils.notEmpty(product.getName(), "Product name is required");
        ValidationUtils.positive(product.getPrice().doubleValue(), "Price must be positive");
        
        return productRepository.save(product);
    }
    
    public Product findById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, 
                "Product not found"));
    }
    
    public PageResponse<Product> search(int page, int size, String name) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> pageResult = productRepository.findByNameContaining(name, pageRequest);
        
        return PageResponse.of(
            pageResult.getContent(),
            page,
            size,
            pageResult.getTotalElements()
        );
    }
}
```

### Controller

```java
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping("/{id}")
    public ApiResponse<Product> getProduct(@PathVariable Long id) {
        Product product = productService.findById(id);
        return ApiResponse.success(product);
    }
    
    @PostMapping
    public ApiResponse<Product> createProduct(@RequestBody @Valid Product product) {
        Product created = productService.create(product);
        return ApiResponse.success("Product created successfully", created);
    }
    
    @GetMapping
    public ApiResponse<PageResponse<Product>> searchProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) String name
    ) {
        PageResponse<Product> result = productService.search(page, size, name);
        return ApiResponse.success(result);
    }
}
```

This complete example demonstrates:
- Entity auditing
- Validation
- Error handling
- Pagination
- CRUD operations

All automatically handled by the common library!

