# BTG Funds API - Java + Spring Boot + Hexagonal Architecture

Banking API for investment funds with clean architecture, JWT security, PostgreSQL persistence (pgAdmin compatible), and AWS deployment artifacts.

## Tech Stack

- Java 21
- Spring Boot 3.4.5
- Spring Security + JWT
- Spring Data JPA + PostgreSQL
- JUnit 5 + Mockito
- OpenAPI/Swagger
- Terraform (AWS)

## Architecture (Hexagonal)

```text
                 +-----------------------------+
                 |       REST Controllers      |
                 |  (Auth, Funds, Subs, Tx)    |
                 +-------------+---------------+
                               |
                               v
                 +-----------------------------+
                 |      Application Layer      |
                 | (Use Cases / Services)      |
                 +-------------+---------------+
                               |
               +---------------+----------------+
               |                                |
               v                                v
  +---------------------------+    +----------------------------+
  |       Domain Layer        |    | Infrastructure Adapters    |
  | Entities, Rules, Ports    |    | PostgreSQL, JWT, Notify    |
  +---------------------------+    +----------------------------+
```

## Project Structure

```text
src/main/java/com/btg/funds
|-- domain
|   |-- model
|   |-- port/in
|   |-- port/out
|   `-- exception
|-- application/service
`-- infrastructure
    |-- adapter/in/rest
    |-- adapter/out/persistence/postgres
    |-- adapter/out/notification
    |-- security
    |-- config
    `-- exception
```

## Business Rules Implemented

- Initial customer balance: `500000 COP`
- Minimum amount validation by fund
- Exact insufficient balance message:
  `No tiene saldo disponible para vincularse al fondo <Nombre>`
- Cancel subscription returns money and records transaction
- Unique transaction ID using `UUID`

## Seed User

- Document: `123456789`
- Password: `ChangeMe123!`
- Role: `USER`

## API Endpoints

- `POST /api/v1/auth/login`
- `GET /api/v1/funds`
- `POST /api/v1/subscriptions`
- `DELETE /api/v1/subscriptions/{id}`
- `GET /api/v1/transactions`

Swagger UI: `http://localhost:8080/swagger-ui.html`
OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Run with PostgreSQL / pgAdmin

1. Create DB in pgAdmin: `funds_db`
2. Ensure credentials match:
   - `DB_URL=jdbc:postgresql://localhost:5432/funds_db`
   - `DB_USERNAME=postgres`
   - `DB_PASSWORD=postgres`
3. Run:
   - `mvn clean test`
   - `mvn spring-boot:run`
4. Open Swagger and authorize with JWT from login endpoint.

Also set:

- `JWT_SECRET`

## Run with AWS RDS PostgreSQL

Use Spring profile `aws`.

Environment variables:

- `SPRING_PROFILES_ACTIVE=aws`
- `DB_URL=jdbc:postgresql://funds-db-rds2.cgxoc8kks46u.us-east-1.rds.amazonaws.com:5432/postgres?sslmode=require`
- `DB_USERNAME=postgres`
- `DB_PASSWORD=<your-rds-password>`
- `JWT_SECRET=<at-least-32-chars>`

Run:

- `mvn spring-boot:run`

## AWS Terraform

Folder: `terraform/`

Current Terraform files were left as the original AWS serverless draft (API Gateway/Lambda plus DynamoDB/SNS/SES).  
If you want, I can also migrate Terraform to `RDS PostgreSQL` so infra matches pgAdmin/PostgreSQL end to end.

## PostgreSQL SQL Scripts

- Schema and tables: `sql/btg_schema.sql`
- Required query: `sql/query_clientes_producto_disponible.sql`

## Postman

- `postman/funds-api.postman_collection.json`
