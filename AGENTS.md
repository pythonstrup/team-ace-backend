# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview
Spring Boot 3.5.3 backend application implementing Clean Architecture with JWT authentication. This is the "Gamchi API" project.

## Development Commands

### Build and Run
```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Run a specific test class
./gradlew test --tests com.nexters.teamace.auth.presentation.AuthControllerTest

# Run a specific test method
./gradlew test --tests "com.nexters.teamace.auth.presentation.AuthControllerTest.login_success"

# Clean and build
./gradlew clean build

# Run the application
./gradlew bootRun
```

### Docker & Database
```bash
# Start MySQL database (required for application)
docker-compose up -d db

# Stop database
docker-compose down

# View database logs
docker-compose logs -f db
```

### Testing
```bash
# Run all tests (includes unit, integration, and E2E tests)
./gradlew test

# Run only integration tests
./gradlew test --tests "*UseCaseIntegrationTest"

# Run only E2E tests  
./gradlew test --tests "*E2ETest"

# Run tests with Testcontainers (automatically starts MySQL container)
./gradlew test --tests "*E2ETest"
```

### Code Quality
```bash
# Apply code formatting (automatically runs before compile)
./gradlew spotlessApply

# Check code formatting without applying
./gradlew spotlessCheck
```

### API Documentation
```bash
# Generate OpenAPI documentation
./gradlew openapi3

# Output: build/api-spec/openapi3.yaml
```

### Database Migration (Flyway)

#### 새 마이그레이션 만들기
1. `src/main/resources/db/migration/` 폴더에 새 파일 생성
2. 파일명 형식: `V{번호}__{설명}.sql`
3. 예시: `V2__Add_user_email_column.sql`, `V3__Create_chat_room_table.sql`

#### 주요 명령어
```bash
# 마이그레이션 검증하기 (PR 올리기 전에 실행)
./scripts/validate-migration-files.sh

# 마이그레이션 실행 (앱 시작할 때 자동으로 됨)
./gradlew flywayMigrate

# 마이그레이션 상태 확인
./gradlew flywayInfo

# SQL 문법 검증
./gradlew flywayValidate
```

## Architecture Overview

### Clean Architecture Layers
The project follows Clean Architecture with unidirectional dependencies:
```
presentation → application → domain ← infrastructure
                           ↑
                         config
```

### Key Architectural Decisions

1. **Dependency Inversion**: Application layer depends on interfaces, not implementations
   - Example: `TokenService` interface in application layer, `JwtTokenProvider` implementation in infrastructure

2. **Command/Result Pattern**: Application services use Command objects for input and Result objects for output
   - Prevents application layer from depending on presentation DTOs
   - Example: `LoginCommand` → `AuthService` → `LoginResult`

3. **Configuration Properties**: Using record types with constructor binding
   - Example: `JwtProperties` as a record with `@ConfigurationProperties`
   - Requires `@ConfigurationPropertiesScan` on main application class

4. **Security Architecture**:
   - Stateless authentication (JWT) with access/refresh token pattern
   - Custom error handling through `SecurityErrorHandler`
   - JWT filter processes tokens before Spring Security authentication

5. **AI Integration Architecture**:
   - Spring AI framework with custom Gemini adapter
   - Conversation domain manages AI interaction patterns
   - External prompt templates for maintainable AI responses
   - Context-aware conversation management

6. **Testing Architecture**:
   - Three-tier testing: Unit → Integration → E2E
   - Testcontainers for database integration testing
   - Fixture Monkey for realistic test data generation
   - REST Docs for automated API documentation

### Bounded Context Architecture
The project is organized into bounded contexts following Domain-Driven Design:

- **auth**: Authentication and authorization
- **user**: User management and domain logic
- **conversation**: AI-powered conversation system with Gemini integration
- **chat**: Chat room functionality
- **common**: Shared utilities and infrastructure

Each bounded context follows Clean Architecture layers with strict dependency rules.

### Authentication Flow
1. **Login**: `POST /api/v1/auth/login` with username (password-less for current implementation)
2. **Token Generation**: `AuthService` creates access and refresh tokens via `TokenService`
3. **Response**: Returns both tokens in `LoginResponse`
4. **API Access**: Subsequent requests include `Authorization: Bearer {accessToken}` header
5. **Token Validation**: `JwtAuthenticationFilter` validates tokens and sets `SecurityContext`
6. **Token Refresh**: Use refresh token to get new access token when expired

### Conversation System Architecture
1. **Domain Models**: `ConversationScript` defines conversation structure and types
2. **Service Layer**: `ConversationService` orchestrates conversation flow
3. **AI Integration**: `ConversationClient` interface with Gemini implementation
4. **Context Management**: `ConversationContext` maintains conversation state
5. **Prompt Management**: External prompt files loaded from resources
6. **Response Processing**: Structured responses based on conversation type

### Testing Strategy

#### Controller Tests
- **Base Class**: `ControllerTest` with `@WebMvcTest` and security configurations
- **Authentication**: `@WithMockCustomUser` annotation for authenticated endpoints
- **Mocking**: `@MockitoBean` for services to isolate controller logic
- **Documentation**: REST Docs integration with `@AutoConfigureRestDocs`
- **JSON Handling**: ObjectMapper and JsonPath utilities for response parsing

#### Integration Tests
- **Base Class**: `UseCaseIntegrationTest` for service layer testing
- **Configuration**: `@SpringBootTest(webEnvironment = NONE)` for non-web tests
- **Real Beans**: No mocking - tests actual Spring bean interactions
- **Assertions**: BDD-style using `BDDAssertions.then()` with `extracting()`

#### E2E Tests
- **Base Class**: `E2ETest` with full Spring Boot context
- **Database**: Testcontainers for MySQL integration
- **HTTP Client**: REST Assured for API testing
- **Test Data**: Fixture Monkey for generating realistic test data

#### Test Data Generation
- **Fixture Monkey**: Generates realistic test objects with minimal configuration
- **Custom Factories**: Domain-specific object creation patterns
- **Database Population**: Automated test data setup for integration tests

##### Describe-Context-It Pattern
This pattern focuses on describing the behavior of code through test cases. While it shares a similar philosophy with the well-known BDD pattern Given-When-Then, it has subtle differences. Describe-Context-It is more suitable for describing behaviors in detail with the test subject as the protagonist, rather than merely describing situations.

| Keyword | Description |
|---------|-------------|
| **Describe** | Specifies the test subject. Names the class or method being tested. |
| **Context** | Describes the circumstances in which the test subject is placed. Explains the parameters to be input to the method being tested. |
| **It** | Describes the behavior of the test subject. Explains what the test subject method returns. |

Writing Guidelines:
- Context statements must start with **with** or **when**
- It statements should be simple, like "It returns true" or "It responses 404"
- Use Fixture Monkey for generating test data: `fixture.giveMeOne(User.class)`
- Use BDD assertions with `extracting()` for multiple field validation
- Mock external dependencies like AI services in unit tests

Example structure:
```java
@Nested
@DisplayName("createUser")
class Describe_createUser {
    @Nested
    @DisplayName("when valid username and nickname are provided")
    class Context_with_valid_username_and_nickname {
        @Test
        @DisplayName("it returns user with generated id")
        void it_returns_user_with_generated_id() {
            // Given
            CreateUserCommand command = fixture.giveMeOne(CreateUserCommand.class);
            
            // When
            CreateUserResult result = userService.createUser(command);
            
            // Then - BDD assertions with extracting()
            then(result)
                .extracting("id", "username", "nickname")
                .containsExactly(result.id(), command.username(), command.nickname());
        }
    }
}
```

## Important Configuration

### Environment Variables
```yaml
# Database
DB_HOST: localhost
DB_USERNAME: root
DB_PASSWORD: 1234

# JWT Authentication
JWT_SECRET: base64-encoded-secret-key
JWT_ACCESS_TOKEN_VALIDITY: 3600000     # 1 hour in milliseconds
JWT_REFRESH_TOKEN_VALIDITY: 604800000  # 7 days in milliseconds

# AI Integration
GEMINI_API_KEY: your-gemini-api-key
OPENAI_API_KEY: fake-key  # Required but unused (Spring AI constraint)
```

### Database Configuration
- **Database**: MySQL 8.x with utf8mb4 charset
- **Migration**: Flyway with validation enabled
- **Connection Pool**: HikariCP (Spring Boot default)
- **SQL Logging**: P6Spy with formatted output for development

### AI Configuration
- **Primary Model**: Google Gemini 2.0 Flash
- **Framework**: Spring AI with custom Gemini integration
- **Conversation Types**: Emotion analysis, message generation
- **Prompt Management**: External prompt files in resources/prompts/

### Monitoring Configuration
- **Actuator**: Health, metrics, and Prometheus endpoints
- **Metrics**: Micrometer with Prometheus registry
- **Endpoints**: `/actuator/health`, `/actuator/metrics`, `/actuator/prometheus`

### Security Configuration
- **Authentication**: JWT-based with access/refresh token pattern
- **Session Management**: STATELESS (no server-side sessions)
- **CSRF**: Disabled for REST API
- **CORS**: Configured for cross-origin requests
- **Public Endpoints**: `/api/v1/auth/**`, `/docs/**`, `/swagger-ui/**`, `/actuator/**`
- **Protected Endpoints**: All other endpoints require valid JWT
- **Error Handling**: `SecurityErrorHandler` for authentication/authorization errors
- **Filter Chain**: Custom `JwtAuthenticationFilter` validates tokens before Spring Security
- **Test Configuration**: `@ConditionalOnWebApplication` prevents security loading in non-web tests

## Common Development Patterns

### Adding a New Domain
1. Create package structure: `domain/application/infrastructure/presentation`
2. Define domain interfaces in domain layer
3. Implement interfaces in infrastructure layer
4. Use dependency injection with constructor parameters
5. Follow Command/Result pattern for application services

### Adding New Endpoints
1. Create Request/Response DTOs in presentation layer
2. Create Command/Result objects in application layer
3. Implement business logic in application service
4. Add controller method with proper validation
5. Write controller tests extending `ControllerTest`
6. Add integration tests extending `UseCaseIntegrationTest`
7. Consider E2E tests extending `E2ETest` for critical flows

### Working with AI Features
1. Define conversation types in `ConversationType` enum
2. Create prompt templates in `src/main/resources/prompts/`
3. Implement conversation logic in `ConversationService`
4. Use `ConversationClient` for AI model interactions
5. Test with mocked AI responses using `@MockitoBean`

### Database Development
1. Create new migration files following `V{number}__{description}.sql` pattern
2. Run `./scripts/validate-migration-files.sh` before committing
3. Use `./gradlew flywayInfo` to check migration status
4. Domain entities go in domain layer, JPA entities in infrastructure
5. Repository interfaces in domain, implementations in infrastructure

### Error Handling
- Use `CustomException` with appropriate `ErrorType`
- Global exception handling via `GlobalExceptionHandler`
- Security exceptions handled by `SecurityErrorHandler`

## Database Migration Guidelines

### Migration File Naming Convention
- Format: `V{version}__{description}.sql`
- Version: Sequential numbers (V1, V2, V3, etc.)
- Description: Use underscores for spaces, be descriptive
- Examples:
  - `V1__Create_user_schema.sql`
  - `V2__Add_user_email_column.sql`
  - `V3__Create_chat_room_participants_table.sql`

### Migration Best Practices
1. **Never modify existing migrations** - Create new migrations instead
2. **Use descriptive names** - Make it clear what the migration does
3. **Test migrations** - Always test against a copy of production data
4. **Backward compatibility** - Consider rollback scenarios
5. **Small incremental changes** - Avoid large, complex migrations
6. **Sequential versioning** - Use V1, V2, V3... for simple and clear ordering
7. **자동 검증** - PR 올릴 때 GitHub에서 자동으로 파일 이름과 SQL 문법을 확인해줍니다

### Migration Location
- All migration files must be placed in: `src/main/resources/db/migration/`
- Flyway automatically runs migrations on application startup
- Migrations are executed in version order
