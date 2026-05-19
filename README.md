# FitTrack

A secure, containerized fitness tracking REST API built with Spring Boot 3, PostgreSQL, and JWT authentication.

## Features

- **User Authentication** - Register and login with JWT token-based auth
- **Workout Categories** - Create and manage workout categories (e.g., Push Day, Leg Day)
- **Workout Templates** - Add exercises with target sets, reps, and rest periods
- **Workout Logging** - Log completed workout sessions linked to categories
- **Meditation Tracking** - Log and retrieve meditation session history
- **Multi-tenant Security** - All data scoped to the authenticated user via JWT

## Tech Stack

| Layer | Technology |
|-------|------------|
| Framework | Spring Boot 3.5 (Java 21) |
| Security | Spring Security + JWT (jjwt) |
| Database | PostgreSQL 16 |
| ORM | Hibernate / Spring Data JPA |
| Build | Maven |
| Containerization | Docker + Docker Compose |

## Quick Start

### Prerequisites

- Docker & Docker Compose
- Java 21+ (optional, for local development without Docker)

### Run with Docker

```bash
# Clone the repository
git clone https://github.com/pranav244872/fittrack.git
cd fittrack

# Create environment file
cp .env.example .env

# Start the application
docker compose up --build -d
```

The API will be available at `http://localhost:8080`.

### Local Development

```bash
# Ensure PostgreSQL is running, then:
mvn spring-boot:run
```

## API Endpoints

### Authentication

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| `POST` | `/api/auth/register` | Public | Register a new user |
| `POST` | `/api/auth/login` | Public | Login and get JWT token |

### Categories

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| `POST` | `/api/categories` | Authenticated | Create a workout category |
| `GET` | `/api/categories` | Authenticated | List all categories |
| `GET` | `/api/categories/{id}` | Authenticated | Get category by ID |
| `DELETE` | `/api/categories/{id}` | Authenticated | Delete a category |

### Workouts

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| `POST` | `/api/categories/{categoryId}/workouts` | Authenticated | Add exercise to category |
| `GET` | `/api/categories/{categoryId}/workouts` | Authenticated | List exercises in category |
| `GET` | `/api/workouts/{id}` | Authenticated | Get exercise by ID |
| `DELETE` | `/api/workouts/{id}` | Authenticated | Delete an exercise |

### Activity Logs

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| `POST` | `/api/logs/workouts` | Authenticated | Log a completed workout |
| `GET` | `/api/logs/workouts` | Authenticated | Get all workout logs |
| `POST` | `/api/logs/meditation` | Authenticated | Log a meditation session |
| `GET` | `/api/logs/meditation` | Authenticated | Get all meditation logs |

## Usage Examples

### 1. Register & Login

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "john", "password": "securepass123"}'

# Login (save the token)
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "john", "password": "securepass123"}' \
  | python3 -c "import sys,json; print(json.load(sys.stdin)['token'])")
```

### 2. Create a Category & Add Exercises

```bash
# Create category
curl -X POST http://localhost:8080/api/categories \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name": "Push Day"}'

# Add exercise
curl -X POST http://localhost:8080/api/categories/1/workouts \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name": "Bench Press", "targetSets": 3, "targetReps": 10, "restBetweenSetsSeconds": 90}'
```

### 3. Log Activity

```bash
# Log meditation
curl -X POST http://localhost:8080/api/logs/meditation \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"durationMinutes": 15}'

# Log workout
curl -X POST http://localhost:8080/api/logs/workouts \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"categoryId": 1, "durationMinutes": 45}'
```

## Project Structure

```
src/main/java/com/pranav244872/fitness_tracker/
├── config/          # Security & application configuration
├── controller/      # REST API endpoints
├── dto/             # Request/Response data transfer objects
├── exception/       # Custom exception classes
├── model/           # JPA entity classes
├── repository/      # Spring Data JPA repositories
├── security/        # JWT filter & token service
└── service/         # Business logic layer
```

## Architecture

- **DTO Pattern** - All API responses use dedicated response DTOs. Entity classes are never exposed directly, preventing accidental leakage of sensitive data (password hashes, internal fields).
- **JWT Authentication** - Stateless token-based auth. Tokens are validated on every request via a custom `OncePerRequestFilter`.
- **Security Filter Chain** - `/api/auth/**` is public; all other endpoints require a valid `Authorization: Bearer <token>` header.
- **Docker Compose** - PostgreSQL and the Spring Boot app run in separate containers with health-check-based startup ordering.

## License

MIT
