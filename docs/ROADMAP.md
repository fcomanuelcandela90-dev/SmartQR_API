# 🗺️ ROADMAP ARCHITECTURE SMARTQR

---

# 📖 Project Overview
SMARTQR is a RESTful API built with Java 25 and Spring Boot that simulates a digital restaurant ecosystem (100 Montaditos style).
The application allows customers to scan QR codes, create orders, pay, and submit feedback. Administrators can manage products, analyze sales with PNG charts, and use AI-powered analytics.

# 👁️ Visual Reference
- **Business Logic:** [View Use Case Diagram](USE_CASE_DIAGRAM.png)
- **Technical Stack & Flow:** [View System Architecture](DIAGRAMS.png)

---

# 🏗️ Global Architecture

## Layered Architecture
`Controller Layer` ➔ `DTO Layer` ➔ `Service Layer` ➔ `Repository Layer` ➔ `Entity Layer` ➔ `MySQL Database`

---

# 📁 Final Project Structure
```txt
src/main/java/com/ironhack/smartqr
├── config      # Security, OpenAPI, and AI Beans
├── controller  # REST API Endpoints
├── dto         # Records with @Valid annotations
├── entity      # JPA Domain Models
├── enums       # Business constants
├── exception   # Global Exception Handling
├── repository  # Spring Data JPA Interfaces
├── security    # JWT Filters and Auth logic
└── service     # Core Business, AI, and Chart Logic
```

---

# 🧬 JPA Inheritance

## Parent / Child Requirement

The project fulfills the inheritance requirement using InheritanceType.JOINED
for cleaner database normalization:
```txt
Payment (Parent)
  ├── CardPayment (Child)
  └── CashPayment (Child)
```

# 🗄️ Database Relationships & Cortafuegos

We strictly use @JsonIgnore on bidirectional relationships (e.g., Order ->
OrderItem) to prevent infinite recursion and StackOverflow memory leaks
during JSON serialization.

# 🔐 Security Architecture

- Framework: Spring Security + JWT Authentication.
- Roles: CUSTOMER (Orders & feedback), EMPLOYEE (Validate cash), ADMIN (Full management & Dashboard).

# 📦 Technologies Used


| Technology         | Purpose                    |
|--------------------|----------------------------|
| Java 25            | Main language              |
| Spring Boot  4.0.x | REST API Framework         |
| Spring Security    | Authentication             |
| JWT                | Authorization              |
| Spring Data JPA    | ORM                        |
| MySQL              | Database                   |
| Maven              | Dependency manager         |
| Swagger/OpenAPI    | API documentation          |
| Lombok             | Boilerplate reduction      |
| JFreeChart         | PNG chart generation       |
| Spring AI + OpenAI | OpenAI integration         |
| Git & GitHub       | Version control            |
| Ollama             | MCP Local Autonomous Agent |
| Ubuntu             | Development environment    |

---

# 🌱 Git Workflow

Strict feature-branch workflow. All features branch off and merge into develop, 
which acts as our pre-production integration branch.