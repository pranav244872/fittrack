# FitTrack API

A production-grade fitness tracking REST API with real-time meditation music streaming, an admin dashboard, and multi-layer security. Built with Spring Boot 3, PostgreSQL, and JWT authentication. Deployed via Docker Compose.

## Features

### Core
- **User Authentication** — Register/login with JWT tokens (24h expiry)
- **Workout Categories** — Create and manage workout groups (e.g., Push Day, Leg Day)
- **Workout Templates** — Define exercises with target sets, reps, and rest periods
- **Workout Logging** — Log completed sessions with duration, linked to categories
- **Meditation Tracking** — Log meditation sessions with optional music track reference
- **Monthly Pagination** — Fetch logs by `?year=YYYY&month=M` for efficient calendar views

### Meditation Music
- **Track Management** — Upload, list, and delete MP3 tracks via admin API
- **Streaming** — `GET /api/music/{id}/stream` serves audio with proper MIME headers
- **Download** — `GET /api/music/{id}/download` for offline caching in the mobile app
- **Auto-Seeding** — 4 initial tracks are seeded on first startup if present in the music directory

### Security
- **JWT Authentication** — Stateless token-based auth on all `/api/**` endpoints
- **API Secret Header** — Every request must include `X-App-Secret` or receive `403 Forbidden`
- **Rate Limiting** — 120 requests/minute per IP address, returns `429` when exceeded
- **Banned Email Registry** — Block specific emails from registering
- **Multi-tenant Isolation** — All data scoped to the authenticated user

### Admin Dashboard
- **Browser-based UI** at `/admin/` — dark-themed, single-page HTML/JS dashboard
- **HTTP Basic Auth** — separate from JWT, credentials in `.env`
- **User Management** — List all users, delete accounts (cascades all data), ban emails
- **System Health** — Disk space, JVM memory, database status, music storage metrics
- **Global Analytics** — DAU, workouts/meditations today & this week, most popular track
- **Music Management** — Upload new MP3s, list tracks, delete tracks
- **Live Log Viewer** — In-memory logback appender streams last 1000 log lines with auto-refresh

## Tech Stack

| Layer | Technology |
|-------|------------|
| Framework | Spring Boot 3.5 (Java 21) |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| Database | PostgreSQL 16 |
| ORM | Hibernate / Spring Data JPA |
| Build | Maven |
| Containerization | Docker + Docker Compose |
| Logging | Logback with in-memory appender |

## Quick Start

### Prerequisites
- Docker & Docker Compose
- Java 21+ (only for local development without Docker)

### Run with Docker

```bash
git clone https://github.com/pranav244872/fittrack.git
cd fittrack

# Create environment file from template
cp .env.example .env
# Edit .env with your credentials

# Start everything
docker compose up --build -d
```

The API will be available at `http://localhost:8080`.  
The admin dashboard will be at `http://localhost:8080/admin/`.

### Local Development

```bash
# Ensure PostgreSQL is running locally, then:
export JWT_SECRET=your-base64-secret
export APP_SECRET=your-app-secret
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=yourpassword
./mvnw spring-boot:run
```

## API Reference

### Public Endpoints (no auth required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/auth/register` | Register (body: `username`, `email`, `password`) |
| `POST` | `/api/auth/login` | Login (body: `username`, `password`) → returns JWT |
| `GET` | `/api/health` | Health check |

### Authenticated Endpoints (JWT + X-App-Secret)

#### User Profile
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/auth/me` | Get current user profile (username, email, createdAt) |

#### Categories & Workouts
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/categories` | Create a workout category |
| `GET` | `/api/categories` | List all categories with exercises |
| `GET` | `/api/categories/{id}` | Get category by ID |
| `DELETE` | `/api/categories/{id}` | Delete a category |
| `POST` | `/api/categories/{id}/workouts` | Add exercise to category |
| `GET` | `/api/categories/{id}/workouts` | List exercises in category |
| `DELETE` | `/api/workouts/{id}` | Delete an exercise |

#### Activity Logs
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/logs/workouts` | Log a workout (body: `categoryId`, `durationMinutes`) |
| `GET` | `/api/logs/workouts` | Get all workout logs |
| `GET` | `/api/logs/workouts?year=2026&month=5` | Get logs for a specific month |
| `POST` | `/api/logs/meditation` | Log meditation (body: `durationMinutes`, `trackId?`) |
| `GET` | `/api/logs/meditation` | Get all meditation logs |
| `GET` | `/api/logs/meditation?year=2026&month=5` | Get logs for a specific month |

#### Music
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/music` | List all available tracks |
| `GET` | `/api/music/{id}/stream` | Stream MP3 audio |
| `GET` | `/api/music/{id}/download` | Download MP3 file |

### Admin Endpoints (HTTP Basic Auth)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/admin/users` | List all users |
| `GET` | `/api/admin/users/count` | Total user count |
| `DELETE` | `/api/admin/users/{id}` | Delete user + all their data |
| `GET` | `/api/admin/banned-emails` | List banned emails |
| `POST` | `/api/admin/banned-emails` | Ban an email (body: `email`, `reason?`) |
| `DELETE` | `/api/admin/banned-emails/{id}` | Unban an email |
| `GET` | `/api/admin/health` | System health metrics |
| `GET` | `/api/admin/analytics` | Global analytics |
| `GET` | `/api/admin/music` | List all tracks |
| `POST` | `/api/admin/music` | Upload track (multipart: `file`, `name`) |
| `DELETE` | `/api/admin/music/{id}` | Delete a track |
| `GET` | `/api/admin/logs?lines=200` | Get recent application logs |

## Project Structure

```
src/main/java/com/pranav244872/fitness_tracker/
├── config/              # SecurityConfig, ApplicationConfig, InMemoryLogAppender, MusicDataSeeder
├── controller/          # AuthController, AdminController, MusicController, TrackingLogController, etc.
├── dto/                 # Request/Response DTOs (AuthDTOs, WorkoutLog*, MeditationLog*, etc.)
├── exception/           # GlobalExceptionHandler, ResourceNotFoundException
├── filter/              # ApiSecretFilter, RateLimitFilter, RequestLoggingFilter
├── model/               # JPA entities (User, Category, Workout, WorkoutLog, MeditationLog, BannedEmail, MeditationTrack)
├── repository/          # Spring Data JPA repositories with custom queries
├── security/            # JwtAuthenticationFilter, JwtService
└── service/             # Business logic (AuthenticationService, WorkoutLogService, etc.)

src/main/resources/
├── application.yaml     # Spring Boot configuration
├── logback-spring.xml   # Logging config (console + in-memory appender)
└── static/admin/        # Admin dashboard (index.html, style.css)
```

## Architecture

```
Mobile App ──► [X-App-Secret Filter] ──► [Rate Limiter] ──► [JWT Filter] ──► Controllers
                                                                                  │
Browser    ──► [HTTP Basic Auth] ──────────────────────────► Admin Controller ─────┤
                                                                                  │
                                                                            PostgreSQL
                                                                            Music Files (volume)
```

- **Dual Security Chains** — `@Order(1)` for admin (HTTP Basic), `@Order(2)` for API (JWT)
- **DTO Pattern** — Entities never exposed directly, preventing password hash leakage
- **Docker Volumes** — PostgreSQL data in `postgres_data/`, music files in `music_data/`
- **Stateless** — No server-side sessions; JWT validated on every request

## Environment Variables

See `.env.example` for all required variables. Key ones:

| Variable | Description |
|----------|-------------|
| `DB_USER` | PostgreSQL username |
| `DB_PASSWORD` | PostgreSQL password |
| `DB_NAME` | Database name |
| `JWT_SECRET` | Base64-encoded HMAC-SHA256 key for JWT signing |
| `APP_SECRET` | Secret header value that the mobile app must send |
| `ADMIN_USERNAME` | HTTP Basic auth username for admin dashboard |
| `ADMIN_PASSWORD` | HTTP Basic auth password for admin dashboard |
