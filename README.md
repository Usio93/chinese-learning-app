# Chinese Learning App

A backend REST API for a Chinese language learning application built with **Java Spring Boot**.

The system is designed to support Chinese learners with lessons, vocabulary practice, quizzes, study plans, progress tracking, and user authentication.

---

## Features

### Authentication
- User registration
- User login
- JWT authentication
- Refresh token support
- Role-based authorization

### Lessons
- View Chinese learning lessons
- Lesson details and lesson content
- Track lesson completion
- Manage lesson progress

### Vocabulary Learning
- Daily vocabulary
- Vocabulary review queue
- Vocabulary learning history
- Vocabulary statistics
- Track learned and reviewing words

### Quiz System
- Quiz questions and options
- Submit quiz answers
- Calculate quiz results
- Save quiz attempts

### Study Plan
- Generate study plans
- Manage daily learning tasks
- Track task status
- Support different learning skills

### Progress Tracking
- Daily learning progress
- Skill progress
- Learning streak
- Progress history
- Skill analytics

### User Profile
- Manage user information
- HSK level
- Learning goals
- Study time
- Occupation
- Application language

---

## Technologies Used

### Backend
- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security

### Security
- JWT Authentication
- Spring Security
- Refresh Token

### Database
- MySQL
- Hibernate / JPA

### Build Tool
- Maven

### Development Tools
- IntelliJ IDEA
- Postman
- Git
- GitHub

---

## Project Structure

```text
src/main/java/com/chineselearning/chineselearningapi
│
├── auth
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── lesson
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── onboarding
│
├── progress
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── quiz
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── security
│   ├── config
│   ├── jwt
│   └── service
│
├── study
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── user
│   ├── controller
│   ├── entity
│   └── repository
│
└── vocabulary
    ├── controller
    ├── dto
    ├── entity
    ├── repository
    └── service
