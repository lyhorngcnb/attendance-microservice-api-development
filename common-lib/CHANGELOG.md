# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.0] - 2024-01-15

### Added
- Initial release of common library
- **Exception Handling**
  - GlobalExceptionHandler for centralized exception handling
  - BusinessException for business logic errors
  - ErrorCode enum with comprehensive error codes (1000-6999 range)
  - Automatic error response formatting

- **Response Wrappers**
  - ApiResponse<T> for standardized API responses
  - PageResponse<T> for pagination support
  - ErrorResponse for error details
  - Factory methods for success/error responses

- **Utilities**
  - StringUtils: email/phone validation, masking, formatting
  - DateUtils: date/time parsing, formatting, calculations
  - JsonUtils: JSON serialization/deserialization
  - ValidationUtils: null checks, boolean validations, range checks
  - CollectionUtils: filter, map, groupBy, partition operations
  - CipherUtils: encryption/decryption, password hashing
  - RetryUtil: retry logic with configurable attempts
  - SecurityUtils: authentication helpers
  - RestClientUtil: REST client wrapper

- **Configuration**
  - CommonConfig for ObjectMapper configuration
  - RestTemplateConfig for HTTP client configuration
  - JpaAuditingConfig for automatic entity auditing
  - LoggingFilter for request/response logging

- **Entity Support**
  - BaseEntity with auditing fields
  - Automatic createdAt/updatedAt management

- **Constants**
  - API path constants
  - HTTP header constants
  - Date format constants
  - Status constants
  - Pagination constants
  - Validation constants

- **Documentation**
  - README.md with comprehensive overview
  - USAGE_EXAMPLES.md with detailed code examples
  - DEVELOPMENT_GUIDE.md for adding features
  - DEPLOYMENT.md for deployment instructions

### Features
- Auto-configuration support via META-INF/spring.factories
- Optional dependencies for Security and JPA
- Comprehensive error handling
- Standardized response format
- Helper utilities for common operations
- Request/response logging
- Entity auditing support

## [Versions]

### How to update this changelog

When you release a new version:

1. Create a new `[X.Y.Z]` section below `[Unreleased]`
2. Add the release date
3. List all changes under appropriate categories:
   - **Added** - New features
   - **Changed** - Changes in existing functionality
   - **Deprecated** - Soon-to-be removed features
   - **Removed** - Removed features
   - **Fixed** - Bug fixes
   - **Security** - Security improvements

### Example:

```markdown
## [1.1.0] - 2024-02-01

### Added
- New utility method for date validation
- Support for custom error messages

### Changed
- Improved error logging in GlobalExceptionHandler
- Updated DateUtils parsing methods

### Fixed
- Fixed null pointer exception in StringUtils

### Security
- Updated encryption algorithm in CipherUtils
```

---

[Unreleased]: https://github.com/your-org/common-lib/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/your-org/common-lib/releases/tag/v1.0.0

