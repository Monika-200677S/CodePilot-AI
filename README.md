# CodePilot AI — AI Coding Interview Preparation Platform

A full-stack platform for coding interview prep: Spring Boot + MySQL backend, HTML/CSS/JS frontend, JWT auth, and Gemini-powered AI features throughout.

## Features

- 🔐 **Auth**: Register, login (JWT + BCrypt), forgot/reset password (token-based)
- 📊 **Dashboard**: solved/attempted counts, difficulty breakdown (donut chart), daily streak, gamified contest rating, GitHub-style activity heatmap
- 🤖 **AI Interviewer**: ChatGPT-style chat for concept explanations and Q&A
- 💡 **AI Hints**: 3 progressive hint levels per question (nudge → approach → pseudocode) — never gives the full solution
- 📚 **44 practice questions** across Arrays, Strings, Linked List, Trees, Graphs, DP, Stack, Heap, Searching, Bit Manipulation
- 🏢 **Company-wise questions**: browse by Amazon, Google, Meta, Netflix, Apple, Microsoft, Uber, Adobe, Oracle, Bloomberg
- 📝 **Notes**: write and auto-save personal notes per question
- ⭐ **Bookmarks**: save favorite questions
- 📄 **Resume Analyzer**: upload a PDF, get an AI-generated ATS score, weak points, missing keywords, and detected skills
- ⏱ **Mock Interview**: 5 random questions, countdown timer per question, end-of-round score
- ⚙️ **Admin Panel**: add/edit/delete questions, view all registered users (protected by ROLE_ADMIN)
- Dark, glassmorphic UI with subtle animations
- Swagger/OpenAPI docs at `/swagger-ui.html`

## Tech Stack

**Backend:** Java 17, Spring Boot 3.5, Spring Data JPA, Spring Security, JWT (jjwt), MySQL, Apache PDFBox, springdoc-openapi
**Frontend:** HTML, CSS, vanilla JavaScript (served as static files by Spring Boot)
**AI:** Gemini API (default) or OpenAI API

## 1. Set up MySQL

```sql
CREATE DATABASE codepilot;
```

> **If you're upgrading from an earlier version of this project**, drop and recreate the database — several tables gained new columns (companies, notes, activity log, etc.):
> ```sql
> DROP DATABASE codepilot;
> CREATE DATABASE codepilot;
> ```

## 2. Configure `src/main/resources/application.properties`

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

jwt.secret=CHANGE_THIS_TO_A_LONG_RANDOM_STRING

gemini.api.key=YOUR_GEMINI_API_KEY_HERE   # free at https://aistudio.google.com/app/apikey
```

## 3. Run the backend

```bash
./mvnw spring-boot:run
```

Runs on **http://localhost:8080**. On first startup:
- 44 practice questions are seeded automatically (with company tags)
- A default admin account is created: **admin@codepilot.ai / Admin@123** — change this password in any real deployment, and use it to access `/admin.html`

## 4. Use the app

Open **http://localhost:8080** — you'll be routed to login/register automatically.

| Page | Path |
|---|---|
| Register | `/register.html` |
| Login | `/login.html` |
| Forgot password | `/forgot-password.html` |
| Dashboard | `/dashboard.html` |
| Practice Questions | `/questions.html` → click into `/question.html?id=N` |
| Companies | `/companies.html` |
| AI Interviewer | `/mentor.html` |
| Resume Analyzer | `/resume.html` |
| Mock Interview | `/mock-interview.html` |
| Admin Panel | `/admin.html` (ADMIN role only) |

## Design notes & honest limitations

- **Forgot password** works end-to-end (token generation, expiry, reset), but since there's no SMTP server configured, the reset token is returned directly in the API response and shown on-screen instead of emailed. Wire up a mail sender in `UserService` if you need real email delivery.
- **Contest rating** is an internal gamified score (+5/+12/+25 per easy/medium/hard solve), not a true competitive rating system — there's no real contest infrastructure behind it.
- **Company tags** are topic-based groupings for practice variety, not a verified "this exact question was asked at this company" dataset.
- **No real code execution/judging** — there's a code editor to draft your solution and a "Mark Solved/Attempted" flow that updates your dashboard, plus a direct link to the real LeetCode problem to verify. Building an actual sandboxed code judge (running arbitrary user code safely) is a separate, much larger project with real security considerations — happy to scope that separately if you want it.
- **Resume ATS scoring** is AI-generated (Gemini) based on resume text extracted via PDFBox — treat scores as directional feedback, not a certified ATS simulation.

## API Overview

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/auth/register` | No | Create account |
| POST | `/api/auth/login` | No | Log in |
| POST | `/api/auth/forgot-password` | No | Get a reset token |
| POST | `/api/auth/reset-password` | No | Reset password with token |
| GET | `/api/questions/all` | No | List all questions |
| GET | `/api/questions/{id}` | No | Get one question |
| GET | `/api/questions/topic/{topic}` | No | Filter by topic |
| GET | `/api/questions/difficulty/{difficulty}` | No | Filter by difficulty |
| POST/PUT/DELETE | `/api/questions/...` | ADMIN | Manage questions |
| POST | `/api/mentor/ask` | Yes | Chat with AI Interviewer |
| POST | `/api/mentor/hint` | Yes | Get a progressive hint (level 1-3) |
| GET/POST | `/api/progress`, `/api/progress/update` | Yes | Dashboard stats |
| GET | `/api/progress/heatmap` | Yes | Activity heatmap data |
| GET/POST/DELETE | `/api/bookmarks` | Yes | Manage bookmarks |
| GET/PUT | `/api/notes/{questionId}` | Yes | Per-question notes |
| POST | `/api/resume/analyze` | Yes | Upload + analyze resume PDF |
| GET | `/api/admin/users` | ADMIN | List all users |

Full interactive docs: **http://localhost:8080/swagger-ui.html**

## Project structure

```
codementor-ai/  (folder name kept for continuity; app is branded CodePilot AI)
├── pom.xml
└── src/main/
    ├── java/CodePilotAI/
    │   ├── config/     (Security, CORS, Swagger, WebClient, DataSeeder, AdminSeeder)
    │   ├── controller/ (Auth, Question, Mentor, Bookmark, Progress, Note, Resume, Admin)
    │   ├── dto/
    │   ├── entity/     (User, Question, Bookmark, StudentProgress, Note, ActivityLog, PasswordResetToken)
    │   ├── exception/
    │   ├── repository/
    │   ├── security/   (JWT service, filter, UserDetailsService)
    │   └── service/
    └── resources/
        ├── application.properties
        └── static/     (13 pages — see table above)
```
