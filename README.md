# Rewards API

A Spring Boot REST API that calculates customer reward points based on purchase transactions.

---

## Business Rules

A customer earns points per transaction as follows:

| Spend Range         | Points Earned              |
|---------------------|----------------------------|
| $0 – $50            | 0 points                   |
| $50.01 – $100       | 1 point per dollar spent above $50 |
| Over $100           | 1 point per dollar in the $50–$100 band + 2 points per dollar above $100 |

**Example:** A $120 purchase = (2 × $20) + (1 × $50) = **90 points**

Months are derived dynamically from transaction dates — no months are hardcoded.

---

## Tech Stack

| Layer        | Technology                  |
|--------------|-----------------------------|
| Language     | Java 17                     |
| Framework    | Spring Boot 3.2.5           |
| Build Tool   | Gradle (Kotlin DSL)         |
| Database     | H2 (in-memory)              |
| ORM          | Spring Data JPA / Hibernate |
| Testing      | JUnit 5, Mockito, MockMvc   |

---

## Prerequisites

- Java 17+
- Gradle 8+ (or use the included wrapper)

---

## Build & Run

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

The API starts on `http://localhost:8080`.

The H2 console is available at `http://localhost:8080/h2-console`
(JDBC URL: `jdbc:h2:mem:rewardsdb`, username: `sa`, no password).

---

## Run Tests

```bash
./gradlew test
```

Test reports are generated at `build/reports/tests/test/index.html`.

---

## API Endpoints

### Get rewards for all customers

```
GET /api/rewards
```

**Response 200 OK:**
```json
[
  {
    "customerId": 1,
    "customerName": "Alice Johnson",
    "customerEmail": "alice@example.com",
    "monthlyPoints": {
      "2024-01": 125,
      "2024-02": 250,
      "2024-03": 80
    },
    "totalPoints": 455
  }
]
```

---

### Get rewards for a specific customer

```
GET /api/rewards/{customerId}
```

| Parameter    | Type | Description              |
|--------------|------|--------------------------|
| `customerId` | Long | The customer's unique id |

**Response 200 OK:**
```json
{
  "customerId": 1,
  "customerName": "Alice Johnson",
  "customerEmail": "alice@example.com",
  "monthlyPoints": {
    "2024-01": 125,
    "2024-02": 250,
    "2024-03": 80
  },
  "totalPoints": 455
}
```

**Response 404 Not Found** (unknown customer):
```json
{
  "status": 404,
  "message": "Customer not found with id: 999",
  "timestamp": "2024-03-15T10:00:00"
}
```

**Response 400 Bad Request** (invalid id format):
```json
{
  "status": 400,
  "message": "Invalid value 'abc' for parameter 'customerId'. Expected type: Long",
  "timestamp": "2024-03-15T10:00:00"
}
```

---

## Project Structure

```
src/
├── main/
│   ├── java/com/retailer/rewards/
│   │   ├── RewardsApiApplication.java      # Application entry point
│   │   ├── config/
│   │   │   └── AppConfig.java              # Spring bean configuration
│   │   ├── controller/
│   │   │   └── RewardsController.java      # REST endpoints
│   │   ├── dto/
│   │   │   ├── RewardsSummaryDto.java       # Response payload
│   │   │   └── ErrorResponseDto.java       # Error response payload
│   │   ├── exception/
│   │   │   ├── CustomerNotFoundException.java
│   │   │   ├── InvalidTransactionException.java
│   │   │   └── GlobalExceptionHandler.java # @RestControllerAdvice
│   │   ├── model/
│   │   │   ├── Customer.java               # JPA entity
│   │   │   └── Transaction.java            # JPA entity
│   │   ├── repository/
│   │   │   ├── CustomerRepository.java
│   │   │   └── TransactionRepository.java
│   │   └── service/
│   │       ├── RewardsCalculatorService.java  # Pure calculation logic
│   │       └── RewardsService.java            # Orchestration + DB access
│   └── resources/
│       ├── application.yml
│       ├── schema.sql                      # DDL — creates tables
│       └── data.sql                        # DML — seeds test data
└── test/
    ├── java/com/retailer/rewards/
    │   ├── controller/
    │   │   └── RewardsControllerIntegrationTest.java
    │   └── service/
    │       ├── RewardsCalculatorServiceTest.java
    │       └── RewardsServiceTest.java
    └── resources/
        └── application.yml                 # Test-specific config
```

---

## Sample Data

The database is seeded with 4 customers and 6 transactions each across January–March 2024:

| Customer       | Jan Pts | Feb Pts | Mar Pts | Total |
|----------------|---------|---------|---------|-------|
| Alice Johnson  | 125     | 250     | 80      | 455   |
| Bob Martinez   | 25      | 150     | 210     | 385   |
| Carol Williams | 400     | 25      | 480     | 905   |
| David Lee      | 45      | 240     | 315     | 600   |

---

## Implementation Notes

- **No hardcoded months** — the `"YYYY-MM"` keys in `monthlyPoints` are derived entirely from `transaction.transaction_date` values using Java's `LocalDate` API.
- **No wildcard imports** — all imports are explicit throughout the codebase.
- **SQL-driven data** — `schema.sql` creates the schema; `data.sql` seeds rows. No Java entities contain hardcoded data.
- **Javadoc** — all classes and public methods are documented.
- **Global error handling** — `GlobalExceptionHandler` uses `@RestControllerAdvice` to provide consistent error JSON for 400, 404, and 500 responses.
