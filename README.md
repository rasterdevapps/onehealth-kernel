# One ERP Kernel

> A full ERP platform kernel — not a set of CRUD microservices — designed to act as the **core operating system for enterprise applications**. Built on Spring Boot 3 + Java 21.

---

## System Overview

The One ERP Kernel is the foundational layer of a platform-first enterprise system. It is closer in nature to SAP NetWeaver, Oracle ERP, or ServiceNow than a typical Spring Boot application.

Every action passes through **Licensing → Authorization → Policy → Audit**. Data is not just stored — it is **governed**.

---

## Architecture

```
         Enterprise Applications
         (built on top of the kernel)

                 │
                 ▼

            ERP Kernel Layer
    ┌──────────────────────────────┐
    │  identity-service  :8081     │
    │  authorization-service :8082 │
    │  tenant-service    :8083     │
    │  config-service    :8084     │
    │  audit-service     :8085     │
    └──────────────────────────────┘

                 │
                 ▼

       Platform Infrastructure
    ┌──────────────────────────────┐
    │  PostgreSQL (multi-schema)   │
    │  Redis (caching)             │
    └──────────────────────────────┘
```

All requests enter through the **cloud-gateway** (port 8080).

---

## Module Structure

### Shared Libraries (not deployable)

| Module | Responsibility |
|---|---|
| **kernel-api** | Extension contracts: `ExtensionPoint`, `ExtensionRegistry`, `KernelModule`, `AuditableAction`, `@ZNamespace`, `ApiResponse`, `KernelEvent` |
| **kernel-auth** | Multi-tenant security: `TenantContext`, `TenantFilter`, `TenantAware`, `BaseEntity`, `KernelAuditorAware`, `SecurityConfig`, `JwtTenantConverter`, RBAC authority model |
| **kernel-metadata** | SAP-inspired Data Dictionary (DDIC): `DdicDomain`, `DdicDataElement`, `DdicTable`, `DdicTableField` |

### Kernel Microservices (deployable)

| Service | Port | Responsibility |
|---|---|---|
| **cloud-gateway** | 8080 | API gateway — routes, JWT validation, tenant header propagation |
| **identity-service** | 8081 | User authentication, JWT issuance, BCrypt passwords, refresh tokens |
| **authorization-service** | 8082 | RBAC — roles, permissions, user-role and role-permission assignments |
| **tenant-service** | 8083 | Tenant lifecycle management — create, suspend, activate |
| **config-service** | 8084 | Dynamic configuration and feature flags, Redis-cached |
| **audit-service** | 8085 | Immutable audit log recording and querying |

---

## Technology Stack

| Concern | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3 |
| Build | Gradle |
| ORM | Spring Data JPA + Flyway |
| Database | PostgreSQL (multi-schema) |
| Cache | Redis |
| Security | Spring Security + JWT (RS256) |
| Password Hashing | BCrypt |
| API Docs | OpenAPI / Swagger (springdoc) |
| Testing | JUnit 5 + Testcontainers |
| Containerization | Docker + Docker Compose |

---

## Database Architecture

Single PostgreSQL instance with multiple schemas — one per service:

```
erp_kernel database
├── kernel_identity        (users, refresh_tokens)
├── kernel_authorization   (roles, permissions, role_permissions, user_roles)
├── kernel_tenant          (tenants)
├── kernel_config          (config_entries)
└── kernel_audit           (audit_logs)
```

---

## Base Entity

All entities extend `BaseEntity` (via `TenantAware`), which provides:

```
id           UUID, primary key, auto-generated
tenant_id    VARCHAR, non-null, enforced by TenantFilter
created_at   TIMESTAMPTZ, set by Spring Data auditing
updated_at   TIMESTAMPTZ, set by Spring Data auditing
created_by   VARCHAR, set from SecurityContext
updated_by   VARCHAR, set from SecurityContext
deleted_at   TIMESTAMPTZ, soft-delete support
```

---

## Security

- **Stateless JWT authentication** using RS256 signed tokens
- **Short-lived access tokens** (15 minutes by default)
- **Refresh tokens** stored as SHA-256 hashes
- **BCrypt password hashing**
- **Multi-tenant isolation** enforced at HTTP (TenantFilter), JPA (TenantAware), and DB (RLS) layers
- **RBAC** with fine-grained permissions: `resource + action` model
- **Immutable audit logs** — protected at both application and DB rule level

---

## API Design

All endpoints follow REST conventions with the standard response envelope:

```json
{
  "status": "success",
  "data": { ... },
  "error": null,
  "timestamp": "2026-03-06T12:00:00Z"
}
```

### Identity Service
```
POST /api/auth/register     Register a new user
POST /api/auth/login        Authenticate and obtain tokens
POST /api/auth/refresh      Refresh access token
POST /api/auth/logout       Revoke refresh tokens
POST /api/auth/change-password
GET  /api/auth/me           Current user profile
```

### Authorization Service
```
POST   /api/roles                          Create role
GET    /api/roles                          List roles
GET    /api/roles/{id}                     Get role
DELETE /api/roles/{id}                     Delete role
POST   /api/roles/{id}/permissions/{pid}   Grant permission
DELETE /api/roles/{id}/permissions/{pid}   Revoke permission

POST   /api/permissions                    Create permission
GET    /api/permissions                    List permissions
```

### Tenant Service
```
POST /api/tenants             Create tenant
GET  /api/tenants             List tenants
GET  /api/tenants/{id}        Get tenant
GET  /api/tenants/code/{code} Get tenant by code
POST /api/tenants/{id}/suspend
POST /api/tenants/{id}/activate
```

### Config Service
```
PUT  /api/config              Set or update config
GET  /api/config              List all config
GET  /api/config/value/{key}  Get value by key
GET  /api/config/feature-flags
DELETE /api/config/{id}
```

### Audit Service
```
POST /api/audit                   Record audit event
GET  /api/audit                   List audit logs (paginated)
GET  /api/audit/action/{type}     Filter by action type
GET  /api/audit/user/{username}   Filter by user
GET  /api/audit/range?from=&to=   Filter by date range
```

---

## Quick Start (Development)

### Prerequisites

- Java 21
- Docker + Docker Compose

### Run with Docker Compose

```bash
# Build all services
./gradlew build -x test

# Start the full stack
docker-compose up -d

# Services will be available at:
#   Gateway:       http://localhost:8080
#   Identity:      http://localhost:8081/swagger-ui.html
#   Authorization: http://localhost:8082/swagger-ui.html
#   Tenant:        http://localhost:8083/swagger-ui.html
#   Config:        http://localhost:8084/swagger-ui.html
#   Audit:         http://localhost:8085/swagger-ui.html
```

### Build

```bash
# Requires Java 21+
./gradlew build
```

### Run Tests

```bash
./gradlew test
```

---

## Extensibility — Z-Namespace Extensions

The kernel follows a **Clean Core** philosophy: core code is never modified directly.

Custom functionality is added through **Z-namespace extensions**:

```java
@ZNamespace("Z_HOSPITAL")
public class ZHospitalDischargeRule implements ExtensionPoint {
    public boolean canDischarge(String patientId) { ... }
}
```

See the `kernel-api` module for `ExtensionPoint`, `ExtensionRegistry`, and `@ZNamespace`.

---

## License

See [ERP_KERNEL_SYSTEM_PROMPT.md](ERP_KERNEL_SYSTEM_PROMPT.md) and [spec.md](spec.md) for the full system specification.
