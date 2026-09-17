# StudySync Backend

Backend service for StudySync, a collaborative platform designed to help students study together through interactive study rooms.

## Features

- User registration
- User authentication
- Live study rooms
- Real-time chat
- Collaborative whiteboard
- Group voice calls

## Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security
- MySQL
- WebSocket
- WebRTC
- Docker
- Maven

## Architecture

StudySync follows a layered backend architecture:

```text
Client
   │
   ▼
Controller
   │
   ▼
Service
   │
   ▼
Repository
   │
   ▼
MySQL