# Sleep Management API

A backend service for managing and tracking users' sleep logs. The API allows users to record their sleep sessions, retrieve logs for specific dates, and get summaries of their sleep patterns over a date range.

## Functionalities

The API exposes the following main functionalities:

- **Create Sleep Log**  
  `POST /sleep-management/api/v1/sleep-logs`  
  Allows a user to record a new sleep log (bed time, wake up time, sleep quality, etc).

- **Get Sleep Log for a Specific Date**  
  `GET /sleep-management/api/v1/sleep-logs?date=yyyy-MM-dd`  
  Retrieves the sleep log for a user on a specific date.

- **Get Sleep Summary**  
  `GET /sleep-management/api/v1/sleep-logs/summary?startDate=yyyy-MM-dd&endDate=yyyy-MM-dd`  
  Returns a summary of the user's sleep logs between two dates (inclusive).

All endpoints require a `x-user-id` header to identify the user.

## Technologies Used

- **Spring Boot (Java 11)**: Main application framework.
- **PostgreSQL**: Relational database for persistent storage.
- **Spock Framework (Groovy)**: Testing framework for unit and integration tests.
- **Testcontainers**: For integration testing with real PostgreSQL instances.
- **Docker & Docker Compose**: Containerization and orchestration for local development.
- **Flyway**: Database migrations.
- **Lombok**: Reduces Java boilerplate code.



## Project Structure

This project follows a clear layered architecture, with each layer having a distinct responsibility:

- **domain**: Contains the core business entities and domain models that represent the fundamental concepts of the application.
- **application**: Implements the business logic and orchestrates use cases, coordinating interactions between domain entities and other layers.
- **infrastructure**: Handles all technology-specific concerns, such as database access, external integrations, and controller implementations.
- **api**: Defines the REST API, including API contracts, request/response models, and any API-specific logic.

## Running the Project Locally

1. **Prerequisites**
   - [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/) installed.

2. **Start the Application**

   From the project root, run:

   ```sh
   docker-compose up --build
   ```

   This will:
   - Start a PostgreSQL database container.
   - Build and run the Spring Boot application container.

   The API will be available at [http://localhost:8080](http://localhost:8080).

3. **Stopping the Application**

   Press `Ctrl+C` in the terminal, then run:

   ```sh
   docker-compose down
   ```

## Testing

Tests are written using the Spock Framework and run automatically with Gradle:

```sh
cd sleep
./gradlew test
```

This will execute all unit and integration tests.

---
