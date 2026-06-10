# Library Management System

A modern Spring Boot 3.x / Java 25 application designed to manage a library management system.

Available Features:

User Roles

- Client: Can view available books and borrow them.
- Owner: Can add, remove, or update books in the inventory.

Core Functionalities

- View Books: Both roles can view and search the list of books.
- Borrow Book: Clients can borrow a book if it is available.
- Return Book: Clients can return a book.
- Manage Books: Owners can add, update, or remove books.
- Book History: Track who borrowed a book, when it was returned, and whether it was returned late.
- Authentication: Distinguish between client and owner roles
- Use of Testcontainers with JUnit for integration testing
- Natural language search

Endpoints


---

## 🚀 Quick Start (Docker Compose)

The entire environment—including the Java application database structures, local Ollama instance, and automated model
provisioning—is fully containerized. You do not need Java, Maven, or Ollama installed on your host machine.

### Prerequisites

* [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running.

### Spin Up the Application

1. Open your terminal in the project root directory.
2. Run the following command:
   ```bash
   docker compose up --build