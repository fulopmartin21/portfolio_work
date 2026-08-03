# Portfolio Work

A Spring Boot REST API demonstrating secure user registration, login, validation, exception handling, PostgreSQL persistence, Flyway migrations, and containerized deployment.

## Features

- User registration
- User login
- BCrypt password hashing
- Request validation
- Global exception handling
- PostgreSQL persistence
- Flyway database migrations
- Docker and Docker Compose support
- Profile-based OpenAPI and Swagger UI
- Automated tests

## Technology Stack

- Java 17
- Spring Boot 4
- Spring Security
- Spring Data JPA
- PostgreSQL 17
- Flyway
- Springdoc OpenAPI
- Maven
- Docker
- Docker Compose
- JUnit 5
- Mockito

## Requirements

### Docker-based execution

- Docker
- Docker Compose

### Local execution

- Java 17
- Maven or Maven Wrapper
- PostgreSQL

## Running with Docker

Build and start the backend and PostgreSQL containers:

```bash
docker compose up --build
```

Start them in the background:

```bash
docker compose up --build -d
```

The application is available at:

```text
http://localhost:<BACKEND_PORT>
```

The default host port is `8080`, so without a custom configuration the application is available at:

```text
http://localhost:8080
```

The effective host port can be changed through the Docker Compose host-port environment variable, documented below as `BACKEND_PORT`.

Stop the containers:

```bash
docker compose down
```

Stop the containers and delete their volumes:

```bash
docker compose down -v
```

> Deleting the PostgreSQL volume permanently removes the locally stored development data.

## Running Locally

Start only PostgreSQL:

```bash
docker compose up -d postgres
```

Start Spring Boot on Linux or macOS:

```bash
./mvnw spring-boot:run
```

Start Spring Boot on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

When running outside Docker, the application uses `SERVER_PORT`. Its default value is `8080`.

## API Documentation

OpenAPI documentation and Swagger UI are enabled only when the `dev` Spring profile is active.

Swagger UI:

```text
http://localhost:<application-port>/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:<application-port>/v3/api-docs
```

With the default local configuration:

```text
http://localhost:8080/swagger-ui.html
http://localhost:8080/v3/api-docs
```

When running with Docker, replace `8080` with the effective host-side backend port.

Swagger UI and the OpenAPI endpoint are disabled when the `prod` profile is active.

## Spring Profiles

The active profile is controlled through:

```text
SPRING_PROFILES_ACTIVE
```

| Profile | Purpose | Swagger |
|---|---|---|
| `dev` | Local development and API exploration | Enabled |
| `prod` | Production-like execution | Disabled |

The default profile is `dev`.

Development example:

```dotenv
SPRING_PROFILES_ACTIVE=dev
```

Production example:

```dotenv
SPRING_PROFILES_ACTIVE=prod
```

## Environment Variables

All variables are optional because development-oriented default values are provided.

### Application

| Variable | Default | Description |
|---|---:|---|
| `SPRING_PROFILES_ACTIVE` | `dev` | Active Spring profile |
| `SERVER_PORT` | `8080` | Port used by Spring Boot inside its execution environment |
| `BACKEND_PORT` | `8080` | Host port mapped to the backend container |

### PostgreSQL Container

| Variable |     Default | Description |
|---|------------:|---|
| `POSTGRES_DB` | `portfolio` | PostgreSQL database name |
| `POSTGRES_USER` |  `postgres` | PostgreSQL username |
| `POSTGRES_PASSWORD` |  `postgres` | PostgreSQL password |
| `POSTGRES_PORT` |      `5432` | PostgreSQL port exposed on the host |

### Spring Datasource

| Variable | Default | Description |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/portfolio` | JDBC connection URL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Datasource username |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | Datasource password |

When the backend runs inside Docker Compose, the datasource URL must use the PostgreSQL service name rather than `localhost`:

```text
jdbc:postgresql://postgres:5432/portfolio
```

### JPA and Hibernate

| Variable | Default | Description |
|---|---|---|
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `validate` | Hibernate schema handling strategy |

Flyway manages database schema changes, so `validate` is recommended instead of allowing Hibernate to create or update the production schema.

### Flyway

| Variable | Default | Description |
|---|---|---|
| `SPRING_FLYWAY_ENABLED` | `true` | Enables or disables Flyway migrations |
| `SPRING_FLYWAY_LOCATIONS` | `classpath:db/migration` | Flyway migration locations |

## Example `.env` File

```dotenv
SPRING_PROFILES_ACTIVE=dev

POSTGRES_DB=portfolio
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_PORT=5432

BACKEND_PORT=8080
SERVER_PORT=8080

SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SPRING_FLYWAY_ENABLED=true
SPRING_FLYWAY_LOCATIONS=classpath:db/migration
```

The real `.env` file may contain credentials and must not be committed to version control.

## Tests

Run all tests on Linux or macOS:

```bash
./mvnw clean test
```

Run all tests on Windows:

```powershell
.\mvnw.cmd clean test
```

The Docker image build packages the application without running tests. Execute the tests before building or publishing the image.

## Building the Application

Linux or macOS:

```bash
./mvnw clean package
```

Windows:

```powershell
.\mvnw.cmd clean package
```

The generated JAR is placed in:

```text
target/
```

## Database Migrations

Flyway migration scripts are stored in:

```text
src/main/resources/db/migration
```

Migration files must follow the Flyway naming convention:

```text
V<version>__<description>.sql
```

Example:

```text
V1__create_users_table.sql
```

## Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── org/portfolio/java/portfolio_work/
│   │       ├── Configuration/
│   │       ├── Controller/
│   │       ├── DTO/
│   │       ├── Entity/
│   │       ├── Exception/
│   │       ├── Repository/
│   │       └── Service/
│   └── resources/
│       ├── db/migration/
│       └── application.properties
└── test/
    └── java/
```

## Security Notes

- Passwords are stored as BCrypt hashes.
- Error responses do not expose stack traces or internal exception messages.
- Swagger UI and OpenAPI endpoints are disabled in the `prod` profile.
- Development credentials are placeholders only.
- Production credentials should be supplied through environment variables or a secrets-management solution.
- Passwords, password hashes, and sensitive request payloads must never be logged.

## Planned Improvements

- Endpoint-level OpenAPI annotations
- Additional integration tests
- GitHub Actions continuous integration
- Deployment documentation
