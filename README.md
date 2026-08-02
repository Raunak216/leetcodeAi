# unSheet

> **AI-powered interview preparation platform that analyzes *how* you solve LeetCode problems and generates personalized, high-ROI practice roadmaps.**

unSheet goes beyond counting solved questions. Instead of tracking only accepted submissions, it captures your entire problem-solving journey—from failed attempts to the final accepted solution—to understand your strengths, weaknesses, and solving patterns.

Using this information, unSheet builds a personalized skill profile and recommends the most impactful questions to maximize interview preparation.

---

## ✨ Features

### 🧠 AI-Powered Solution Analysis
- Tracks the complete solving journey instead of just the final accepted code.
- Analyzes retries, mistakes, code evolution, and submission patterns.
- Uses Gemini AI to identify learning signals from each attempt.

### 🎯 Personalized Interview Roadmaps
Generate recommendations based on:
- Your current skill profile.
- Recently practiced topics.
- Weak areas identified through AI analysis.
- Company-specific interview patterns.

### 🏢 Company-Specific Preparation
Choose a target company and receive a personalized roadmap built from:
- Frequently asked interview questions.
- Your existing strengths.
- Topics you need to improve first.

Instead of solving hundreds of random problems, unSheet prioritizes the highest ROI questions for your profile.

### 📊 Adaptive Skill 

Builds a fine-grained skill graph covering Data Structures & Algorithms subtopics rather than tracking only solved questions.

Every analyzed attempt produces AI-derived learning signals that update topic mastery using a modified EMA-based scoring model with diminishing returns, creating a continuously evolving representation of interview readiness.

### 🔄 Automatic LeetCode Tracking
The Chrome Extension automatically captures:
- Run attempts
- Wrong Answers
- Time Limit Exceeded
- Runtime Errors
- Accepted submissions
- Code evolution between runs

No manual logging required.

---

# How It Works

```
LeetCode
      │
      ▼
Chrome Extension
      │
      ▼
Journey Recorder
(Runs, Verdicts, Code Changes)
      │
      ▼
Spring Boot Backend
      │
      ▼
Gemini AI Analysis
      │
      ▼
Skill Profile Update
      │
      ▼
Recommendation Engine
      │
      ▼
Personalized Interview Roadmap
```

---

# Architecture

## Frontend
- Next.js
- TypeScript
- Tailwind CSS

Responsible for:
- Authentication
- Dashboard
- Recommendation Interface
- Company Selection

---

## Chrome Extension

Built using Manifest V3.

Responsibilities:
- Detect LeetCode submissions.
- Capture code evolution.
- Build solving journey.
- Upload accepted attempts securely using JWT authentication.

---

## Backend

Built with Spring Boot.

Responsible for:
- Authentication
- OAuth Login
- Recommendation Engine
- AI Analysis Pipeline
- Skill Profile Management
- Company Question APIs

---

## Database

PostgreSQL stores:

- Users
- Question Attempts
- Skill Profiles
- Company Questions
- LeetCode Profiles

---

## AI Layer

Gemini analyzes every accepted attempt and extracts:

- Solving efficiency
- Mistake patterns
- Topic understanding
- Skill signals
- Learning insights

These signals update the user's skill profile automatically.

---

# Recommendation Strategy

unSheet recommends questions using multiple signals including:

- Current topic mastery
- Weak topics
- Previously solved questions
- Company interview frequency
- Interview urgency (days remaining)

The objective is to maximize interview preparation by recommending the highest ROI questions instead of simply increasing question count.

---

# Authentication

- Google OAuth 2.0
- JWT Authentication
- Secure HttpOnly Cookies
- Chrome Extension Authentication

---

# Tech Stack

### Frontend
- Next.js
- React
- TypeScript
- Tailwind CSS
- Axios

### Backend
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- OAuth2
- JWT

### Database
- PostgreSQL

### AI
- Google Gemini API

### Extension
- Chrome Extension (Manifest V3)
- TypeScript

### Deployment
- Vercel
- Railway
- Neon PostgreSQL

---


# Disclaimer

unSheet is designed to support interview preparation by analyzing solving behavior and recommending practice questions. It is **not** intended to provide solutions or assist users in cheating during interviews or online assessments.

---

## Author

**Raunak Kumar**

GitHub: https://github.com/Raunak216
