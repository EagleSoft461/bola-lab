# BOLA Lab - Spring Boot

A practical Spring Boot security lab demonstrating **Broken Object Level Authorization (BOLA)** and ownership-based access control.

## Overview

Broken Object Level Authorization occurs when an API allows an authenticated user to access an object they are not authorized to access.

This project demonstrates the difference between an insecure endpoint that retrieves a document by its ID alone and a secure endpoint that checks both the document ID and the authenticated user's identity.

## Tech Stack

- Java 21
- Spring Boot
- Spring Security with HTTP Basic authentication
- Spring Data JPA
- H2 in-memory database
- Maven
- JUnit and MockMvc

## Security Demonstration

| Endpoint | Behavior |
|---|---|
| `GET /api/lab/insecure/documents/{id}` | Demonstrates the BOLA vulnerability |
| `GET /api/documents/{id}` | Checks document ownership |

### Insecure implementation

The insecure service retrieves a document using its ID alone:

```java
documentRepository.findById(documentId);
```

This does not verify whether the authenticated user owns the document.

### Secure implementation

The secure service checks the document ID and authenticated user's ID together:

```java
documentRepository.findByIdAndOwnerId(
    documentId,
    authenticatedUserId
);
```

A user attempting to access another user's document receives `404 Not Found` in this lab.

## Test Results

The automated tests demonstrate the expected behavior:

- **Insecure endpoint:** another authenticated user can read a document they do not own (`200 OK`).
- **Secure endpoint:** another authenticated user cannot read that document (`404 Not Found`).
- **Authentication:** unauthenticated requests to protected API endpoints are rejected.

Run the tests with:

```powershell
.\mvnw.cmd test
```

## Running the Application

Start the application locally:

```powershell
.\mvnw.cmd spring-boot:run
```

The lab uses an H2 in-memory database. Test data created during automated tests is not automatically available in a separate application run.

## Important Security Note

This is an educational demonstration, not a production-ready application. The insecure endpoint is intentionally vulnerable and should only be used in a controlled local environment. Do not deploy it to a public server.

## Learning Objective

Understand why authentication alone is not sufficient and why APIs must enforce authorization for every requested object.

## References

- [OWASP API Security Top 10 - API1:2023 Broken Object Level Authorization](https://owasp.org/API-Security/editions/2023/en/0x11-t10/)
- [OWASP IDOR Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Insecure_Direct_Object_Reference_Prevention_Cheat_Sheet.html)