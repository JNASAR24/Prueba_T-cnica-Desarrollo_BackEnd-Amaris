# BTG Funds API + Frontend - Java + Spring Boot + Hexagonal Architecture

Banking solution for investment funds with clean architecture, JWT security, PostgreSQL persistence (pgAdmin compatible), AWS deployment, and React frontend.

## Tech Stack

- Java 21
- Spring Boot 3.4.5
- Spring Security + JWT
- Spring Data JPA + PostgreSQL
- JUnit 5 + Mockito
- OpenAPI/Swagger
- React + Vite (frontend)
- AWS (Elastic Beanstalk, RDS, S3, CloudFront, SNS)

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
- Password: `123456`
- Role: `USER`

## API Endpoints

- `POST /api/v1/auth/login`
- `GET /api/v1/funds`
- `POST /api/v1/subscriptions`
- `DELETE /api/v1/subscriptions/{id}`
- `GET /api/v1/transactions`

Swagger UI: `http://localhost:8080/swagger-ui.html`
OpenAPI JSON: `http://localhost:8080/v3/api-docs`

> Note: in this project the backend commonly runs on port `8081` locally.

## Run with PostgreSQL / pgAdmin

1. Create DB in pgAdmin: `funds_db`
2. Ensure credentials match:
   - `DB_URL=jdbc:postgresql://localhost:5432/funds_db`
   - `DB_USERNAME=postgres`
   - `DB_PASSWORD=<your_local_password>`
   - `JWT_SECRET=<at-least-32-chars>`
3. Run:
   - `mvn clean test`
   - `mvn spring-boot:run`
4. Open Swagger and authorize with JWT from login endpoint.

## Run with AWS RDS PostgreSQL

Use Spring profile `aws`.

Environment variables:

- `SPRING_PROFILES_ACTIVE=aws`
- `SERVER_PORT=5000`
- `DB_URL=jdbc:postgresql://funds-db-rds2.cgxoc8kks46u.us-east-1.rds.amazonaws.com:5432/postgres?sslmode=require`
- `DB_USERNAME=postgres`
- `DB_PASSWORD=<your-rds-password>`
- `JWT_SECRET=<at-least-32-chars>`
- `NOTIFICATION_PROVIDER=SNS` (or `MOCK`)
- `AWS_REGION=us-east-1`
- `SNS_EMAIL_TOPIC_ARN=arn:aws:sns:us-east-1:746902954826:funds-email-notifications`

Run:

- `mvn spring-boot:run`

## Frontend (React)

Path: `frontend/`

Local run:

1. `cd frontend`
2. `npm install`
3. `npm run dev`
4. Open `http://localhost:5173`

Production build:

1. `cd frontend`
2. `npm run build`
3. Upload `frontend/dist/*` to S3 bucket `pt-amaris-frontend-746902954826`
4. Create CloudFront invalidation with path `/*`

Frontend URL (AWS):

- `https://d2jbrhwnav2xw1.cloudfront.net`

### CloudFront API routing (important)

To avoid mixed-content and CORS issues in production, add a CloudFront behavior:

- Path pattern: `/api/*`
- Origin: `pt-be-amaris.us-east-1.elasticbeanstalk.com`
- Viewer protocol policy: `Redirect HTTP to HTTPS`
- Allowed methods: `GET, HEAD, OPTIONS, PUT, POST, PATCH, DELETE`
- Cache policy: `CachingDisabled`
- Origin request policy: `AllViewer`

## AWS Terraform

Folder: `terraform/`

Current Terraform files were left as the original AWS serverless draft (API Gateway/Lambda plus DynamoDB/SNS/SES).  
If you want, I can also migrate Terraform to `RDS PostgreSQL` so infra matches pgAdmin/PostgreSQL end to end.

## PostgreSQL SQL Scripts

- Schema and tables: `sql/btg_schema.sql`
- Required query: `sql/query_clientes_producto_disponible.sql`

## Postman

- `postman/funds-api.postman_collection.json`
- `postman/funds-api.local.postman_environment.json`
- `postman/funds-api.aws.postman_environment.json`

Execution order:

1. `POST /api/v1/auth/login`
2. `GET /api/v1/funds`
3. `POST /api/v1/subscriptions`
4. `GET /api/v1/transactions`
5. `DELETE /api/v1/subscriptions/{id}`
6. `GET /api/v1/transactions`

## URLs

- Backend Swagger local: `http://localhost:8081/swagger-ui.html`
- Backend Swagger AWS: `http://pt-be-amaris.us-east-1.elasticbeanstalk.com/swagger-ui.html`
- Frontend local: `http://localhost:5173`
- Frontend AWS: `https://d2jbrhwnav2xw1.cloudfront.net`

## Documentation

- Test manual (final): `docs/Manual_Pruebas_Funds_API_vFinal.pdf`
- Clone guide addendum: `docs/Manual_Pruebas_Funds_API_Anexo_Clonacion.md`
