# Spring Boot API Gateway - Microservices Management

## 🚀 Project Overview

This is a comprehensive **Spring Boot API Gateway** that serves as the central entry point for your microservices architecture. It provides authentication, authorization, routing, rate limiting, and monitoring capabilities.

## ✨ Features

### 🔐 **Authentication & Authorization**
- **JWT Token-based Authentication** with secure token validation
- **Role-based Access Control** (RBAC) with ADMIN, MANAGER, and USER roles
- **Public and Protected Route Management**
- **Automatic Token Validation** for all protected endpoints

### 🛣️ **Smart Routing**
- **Service Discovery** with configurable service URLs
- **Load Balancing** capabilities
- **Request/Response Transformation**
- **Header Injection** for downstream services

### 🛡️ **Security Features**
- **Rate Limiting** per client IP
- **CORS Configuration** for cross-origin requests
- **Request Logging** and monitoring
- **Error Handling** with custom error responses

### 📊 **Monitoring & Observability**
- **Actuator Endpoints** for health checks and metrics
- **Request/Response Logging** with timing information
- **Prometheus Metrics** integration
- **Gateway Route Information** exposure

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   API Gateway   │    │  Microservices  │
│   (React/Vue)   │───▶│   (Port 8080)   │───▶│                 │
│                 │    │                 │    │ • User Service  │
│                 │    │ • Authentication│    │   (Port 8081)   │
│                 │    │ • Authorization │    │ • Product Svc   │
│                 │    │ • Rate Limiting │    │   (Port 8083)   │
│                 │    │ • Routing       │    │ • Other Services│
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🔧 Configuration

### Service URLs
```properties
# User Service
user-service.url=http://localhost:8081

# Product Service  
product-service.url=http://localhost:8083
```

### JWT Configuration
```properties
# JWT Secret (256-bit key)
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970

# Token Expiration (24 hours)
jwt.expiration=86400000

# Refresh Token Expiration (7 days)
jwt.refresh-expiration=604800000
```

## 🛣️ Route Configuration

### Public Routes (No Authentication Required)
- `/api/auth/login` - User login
- `/api/auth/register` - User registration  
- `/api/auth/refresh` - Token refresh
- `/api/auth/forgot-password` - Password reset
- `/actuator/**` - Health checks and metrics

### Protected Routes (Authentication Required)
- `/api/users/**` - User management
- `/api/admin/**` - Admin operations (ADMIN role required)
- `/api/manager/**` - Manager operations (MANAGER role required)
- `/api/products/**` - Product management

## 🔑 JWT Token Usage

### How to Use JWT Tokens

#### 1. **Login to Get Token**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "your_username",
    "password": "your_password"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "roles": ["USER"]
  }
}
```

#### 2. **Use Token for Protected Endpoints**
```bash
curl -X GET http://localhost:8080/api/users/profile \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### 3. **Frontend Integration (JavaScript)**
```javascript
// Store token after login
localStorage.setItem('token', response.data.token);

// Use token in API calls
const token = localStorage.getItem('token');
fetch('/api/users/profile', {
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
});
```

#### 4. **Token Information**
The JWT token contains:
- **Subject**: Username
- **User ID**: Unique user identifier
- **Roles**: User roles (USER, MANAGER, ADMIN)
- **Expiration**: Token expiry time
- **Issued At**: Token creation time

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- Running microservices (User Service on 8081, Product Service on 8083)

### 1. **Clone and Build**
```bash
git clone <your-repo>
cd springboot-gateway-services
mvn clean install
```

### 2. **Configure Services**
Update `application.properties` with your service URLs:
```properties
user-service.url=http://localhost:8081
product-service.url=http://localhost:8083
```

### 3. **Run the Gateway**
```bash
mvn spring-boot:run
```

The gateway will start on `http://localhost:8080`

### 4. **Test the Gateway**
```bash
# Health check
curl http://localhost:8080/actuator/health

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

## 📊 Monitoring

### Actuator Endpoints
- **Health**: `http://localhost:8080/actuator/health`
- **Info**: `http://localhost:8080/actuator/info`
- **Metrics**: `http://localhost:8080/actuator/metrics`
- **Gateway Routes**: `http://localhost:8080/actuator/gateway/routes`

### Logs
The gateway provides detailed logging:
- Request/Response logging with timing
- Authentication success/failure logs
- Rate limiting notifications
- Error tracking

## 🔒 Security Best Practices

### 1. **JWT Token Security**
- Tokens are signed with HMAC SHA-256
- Tokens expire after 24 hours
- Refresh tokens available for seamless renewal
- Secure secret key configuration

### 2. **Rate Limiting**
- Configurable per endpoint
- IP-based limiting
- Prevents abuse and DoS attacks

### 3. **CORS Configuration**
- Configurable allowed origins
- Credential support
- Preflight request handling

## 🛠️ Development

### Project Structure
```
src/main/java/com/rbc/demo/apigateway/
├── ApiGatewayApplication.java          # Main application class
├── client/
│   └── UserServiceClient.java         # Service communication
├── config/
│   ├── GatewayConfig.java             # Route configuration
│   ├── GatewayProperties.java         # Custom properties
│   ├── SecurityConfig.java            # Security configuration
│   └── WebClientConfig.java           # HTTP client setup
├── exception/
│   └── GlobalExceptionHandler.java    # Error handling
├── filter/
│   ├── AuthenticationFilter.java      # JWT validation
│   ├── LoggingFilter.java             # Request logging
│   ├── RateLimitFilter.java           # Rate limiting
│   └── RoleCheckFilter.java           # Role-based access
└── security/
    └── JwtUtil.java                   # JWT utilities
```

### Adding New Services
1. **Update `application.properties`**:
```properties
new-service.url=http://localhost:8084
```

2. **Add route in `GatewayConfig.java`**:
```java
.route("new-service", r -> r
    .path("/api/new-service/**")
    .filters(f -> f
        .filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
        .addRequestHeader("X-Route-Type", "New-Service"))
    .uri("http://localhost:8084"))
```

## 🐛 Troubleshooting

### Common Issues

#### 1. **Authentication Failures**
- Check JWT secret configuration
- Verify token format (Bearer token)
- Ensure token is not expired

#### 2. **Service Connection Issues**
- Verify service URLs in configuration
- Check if downstream services are running
- Review network connectivity

#### 3. **CORS Issues**
- Update CORS configuration for your frontend domain
- Check preflight request handling

### Debug Mode
Enable debug logging:
```properties
logging.level.com.rbc.demo.apigateway=DEBUG
logging.level.org.springframework.cloud.gateway=DEBUG
```

## 📈 Performance

### Optimizations
- **Connection Pooling**: Configured HTTP client pools
- **Rate Limiting**: Prevents service overload
- **Async Processing**: Non-blocking request handling
- **Caching**: JWT validation caching (future enhancement)

### Metrics
Monitor key metrics:
- Request latency
- Error rates
- Rate limit hits
- Authentication success/failure rates

## 🔄 Future Enhancements

- [ ] **Redis Integration** for distributed rate limiting
- [ ] **Circuit Breaker** pattern implementation
- [ ] **Request/Response Caching**
- [ ] **API Versioning** support
- [ ] **GraphQL** endpoint support
- [ ] **WebSocket** routing
- [ ] **Distributed Tracing** with Zipkin/Jaeger

## 📝 License

This project is licensed under the MIT License.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

---

**Happy Coding! 🚀**
