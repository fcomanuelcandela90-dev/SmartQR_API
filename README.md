<h1 align="center">
🤖 SMARTQR - Smart REST API
</h1>

<div align="center">

<br>

<img src="https://img.shields.io/badge/Spring_Boot-REST_API-6DB33F?style=for-the-badge&logo=springboot" />
<img src="https://img.shields.io/badge/Spring_Security-JWT-000000?style=for-the-badge&logo=springsecurity" />
<img src="https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql" />
<img src="https://img.shields.io/badge/JFreeChart-PNG_Charts-orange?style=for-the-badge" />
<img src="https://img.shields.io/badge/OpenAI-Spring_AI-412991?style=for-the-badge&logo=openai" />
<img src="https://img.shields.io/badge/Ollama-MCP_Local_Agent-white?style=for-the-badge" />

<p align="center">
<img src="https://skillicons.dev/icons?i=java,spring,maven,idea,mysql,git,github,ubuntu" />
</p>

<p align="center">
<img src="https://img.shields.io/badge/Ironhack-000000?style=for-the-badge"/>
<img src="https://img.shields.io/badge/Amazon_Career_Choice-FF9900?style=for-the-badge&logo=amazon&logoColor=white"/>
</p>

</div>

---

# 📖 Sobre el proyecto

**SmartQR** es mi proyecto final de backend: una API REST que simula el flujo digital de un restaurante en el que el cliente consulta la carta desde una mesa con QR, realiza su pedido, paga y puede dejar una valoración.

A partir de ese flujo principal, el proyecto añade tres partes que quería trabajar especialmente:

- Seguridad real con JWT y permisos distintos para cliente, empleado y administrador.
- Un panel de administración con métricas y gráficos PNG generados desde el backend.
- Dos usos de inteligencia artificial separados: OpenAI para funcionalidades de cliente y Ollama/MCP para consultas administrativas locales de solo lectura.

La API está pensada como proyecto demostrable desde archivos `.http`, sin frontend. Durante la presentación se puede seguir el ciclo completo de pedido desde los tres perfiles.

---
# 🛠️ Tecnologías usadas

- Java 25
- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- MySQL
- Maven
- Spring AI
- OpenAI
- Ollama
- MCP
- JFreeChart
- ZXing

---

# ✅ Alcance de la versión `v1.0.0`

## 👤 Customer

El cliente puede:

- Consultar la carta pública disponible.
- Crear una comanda asociada a una mesa.
- Consultar el estado de **su propio pedido**.
- Pagar con tarjeta simulada o solicitar pago en efectivo.
- Descargar el ticket de un pedido pagado.
- Enviar feedback, que OpenAI clasifica como positivo, neutral o negativo.
- Pedir una recomendación de combo según preferencias, presupuesto y número de personas.

> La cola completa de cocina no se muestra al cliente: es información interna. Como mejora futura se podría añadir una estimación visual de espera para su propio pedido.

## 👨‍🍳 Employee

El empleado puede:

- Generar códigos QR de mesa.
- Consultar productos fuera de stock.
- Consultar pedidos activos por mesa y la cola de cocina.
- Modificar las líneas de una comanda pendiente.
- Actualizar el estado de preparación.
- Cancelar pedidos cuando las reglas de negocio lo permiten.
- Confirmar cobros en efectivo y descargar tickets.

## 👨‍💼 Admin

El administrador puede:

- Gestionar el catálogo completo de productos.
- Consultar métricas del restaurante.
- Generar gráficos PNG de ingresos y productos vendidos.
- Consultar estadísticas del feedback ya analizado por OpenAI.
- Utilizar un agente local con Ollama para consultar información interna.
- Acceder mediante MCP a herramientas de consulta de métricas, menú y cola de cocina.

---

# 🏗️ Arquitectura

## Arquitectura por capas

```text
Controller → DTO → Service → Repository → Entity → MySQL
```

## Estructura principal

```text
src/main/java/com/ironhack/smartqr
├── config       # Configuración de OpenAI y Ollama
├── controller   # Endpoints REST
├── dto          # Records de petición y respuesta con validaciones
├── entity       # Entidades JPA
├── enums        # Roles, estados, categorías y métodos de pago
├── exception    # Excepciones personalizadas y manejo global de errores
├── repository   # Interfaces Spring Data JPA
├── security     # EncoderConfig, SecurityConfig y filtros JWT
├── service      # Lógica de negocio, IA y generación de gráficos
└── tool         # Herramientas read-only publicadas para el agente y MCP
```

---

# 🧬 Modelo de datos y pagos

El requisito de herencia JPA está representado mediante los distintos métodos de pago:

```text
Payment
├── CardPayment
└── CashPayment
```

con estrategia:

```text
InheritanceType.JOINED
```

Un pago se asocia a una comanda concreta mediante `orderId`. El token JWT identifica al usuario conectado y el backend válida que un cliente no consulte, pague, descargue ticket o deje feedback sobre pedidos de otro cliente.

En las líneas de pedido, el cliente solo envía el producto seleccionado, la cantidad y sus notas. El nombre del producto y el subtotal se obtienen o calculan en backend para evitar que el precio pueda ser manipulado desde la petición.

---

# 🔐 Seguridad

La seguridad del proyecto utiliza:

- Spring Security.
- Autenticación JWT mediante `POST /api/login`.
- Autorización por roles: `CUSTOMER`, `EMPLOYEE` y `ADMIN`.
- `PasswordEncoderFactories.createDelegatingPasswordEncoder()` en `security/EncoderConfig`.
- Clave JWT cargada desde la variable de entorno `JWT_SECRET`.
- Validaciones de entrada mediante Jakarta Validation.
- Manejo global de errores mediante `GlobalExceptionHandler`.

## Roles y permisos principales

| Rol | Permisos principales |
|---|---|
| `CUSTOMER` | Carta, pedidos propios, pagos, tickets propios, feedback y recomendación IA |
| `EMPLOYEE` | QR, cola de cocina, preparación de pedidos y confirmación de efectivo |
| `ADMIN` | Productos, dashboard, gráficos, feedback analytics, agente local y MCP |

## Respuestas de error validadas

| Situación | Código HTTP |
|---|---:|
| Datos de entrada inválidos o regla de negocio incumplida | `400 Bad Request` |
| Token inválido o caducado | `401 Unauthorized` |
| Operación sin permiso o acceso a pedido ajeno | `403 Forbidden` |
| Recurso no encontrado | `404 Not Found` |
| Conflicto, por ejemplo email duplicado o pago repetido | `409 Conflict` |
| Fallo de OpenAI u Ollama | `503 Service Unavailable` |

---

# 🤖 Inteligencia artificial

## OpenAI para funcionalidades de cliente

OpenAI se utiliza en dos operaciones:

1. **Análisis de sentimiento del feedback**  
   El cliente envía comentario y puntuación; la API almacena el resultado como `POSITIVE`, `NEUTRAL` o `NEGATIVE`.

2. **Recomendación de combos**  
   El cliente indica preferencias, presupuesto y número de personas; la IA recomienda un combo usando productos disponibles del menú.

## Estadísticas administrativas del feedback

El endpoint administrativo de estadísticas **no vuelve a llamar a OpenAI**. Calcula total de opiniones, valoración media y distribución de sentimientos utilizando los datos que ya quedaron guardados al recibir cada feedback.

## Ollama local y MCP para administración

El agente administrativo utiliza un modelo local de Ollama:

```text
qwen3:4b
```

Sus herramientas son de solo lectura y permiten consultar:

- Métricas de ventas.
- Cola actual de cocina.
- Productos disponibles del menú.

Esta separación permite explicar dos usos distintos de IA:

```text
OpenAI      → experiencia del cliente
Ollama/MCP  → consulta interna del administrador
```

---

# 📊 Dashboard y gráficos

El administrador dispone de métricas y gráficos generados desde la API:

- Gráfico circular de ingresos por método de pago.
- Gráfico de barras de productos vendidos, con escala de unidades enteras.

Los PNG se generan localmente dentro de la carpeta de requests utilizada para pruebas y están excluidos del control de versiones.

---

# 🚀 Endpoints implementados

## 🔐 Autenticación

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `POST` | `/auth/register` | Público | Registrar un nuevo customer |
| `POST` | `/api/login` | Público | Obtener token JWT |

## 🍔 Productos

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `GET` | `/products/menu` | Público | Consultar carta disponible |
| `GET` | `/products` | `ADMIN` | Consultar catálogo completo |
| `POST` | `/products` | `ADMIN` | Crear producto |
| `PUT` | `/products/{id}` | `ADMIN` | Editar producto |
| `DELETE` | `/products/{id}` | `ADMIN` | Eliminar producto |
| `GET` | `/products/out-of-stock` | `EMPLOYEE`, `ADMIN` | Consultar productos no disponibles |

## 📱 Códigos QR

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `POST` | `/qr/table/{tableNumber}` | `EMPLOYEE`, `ADMIN` | Generar QR para una mesa |

## 📋 Pedidos

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `POST` | `/orders` | `CUSTOMER` | Crear pedido |
| `GET` | `/orders/{orderId}` | Roles autenticados; ownership para cliente | Consultar pedido |
| `GET` | `/orders/table/{tableNumber}` | `EMPLOYEE`, `ADMIN` | Consultar pedidos activos por mesa |
| `GET` | `/orders/kitchen/queue` | `EMPLOYEE`, `ADMIN` | Consultar cola de cocina |
| `PUT` | `/orders/{orderId}/items` | `EMPLOYEE`, `ADMIN` | Modificar líneas de pedido |
| `PATCH` | `/orders/{orderId}/status` | `EMPLOYEE`, `ADMIN` | Cambiar estado |
| `PUT` | `/orders/{orderId}/cancel` | `EMPLOYEE`, `ADMIN` | Cancelar pedido permitido |

## 💳 Pagos y ticket

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `POST` | `/payments/card` | `CUSTOMER` | Realizar pago simulado por tarjeta |
| `POST` | `/payments/cash/request` | `CUSTOMER` | Solicitar pago en efectivo |
| `PUT` | `/payments/cash/{orderId}/confirm` | `EMPLOYEE`, `ADMIN` | Confirmar pago efectivo |
| `GET` | `/payments/ticket/{orderId}` | Roles autenticados; ownership para cliente | Descargar ticket imprimible |

## 💬 Feedback e IA

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `POST` | `/feedback` | `CUSTOMER` | Enviar feedback y analizar sentimiento |
| `POST` | `/ai/combo-recommendation` | `CUSTOMER` | Solicitar recomendación de combo |
| `GET` | `/feedback/statistics` | `ADMIN` | Consultar estadísticas de sentimientos guardados |

## 📈 Dashboard

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `GET` | `/dashboard/metrics` | `ADMIN` | Consultar métricas |
| `GET` | `/dashboard/charts/income-by-payment-method` | `ADMIN` | Generar gráfico circular PNG |
| `GET` | `/dashboard/charts/product-sales` | `ADMIN` | Generar gráfico de barras PNG |

## 🧩 Agente local y MCP

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `POST` | `/agent/local/ask` | `ADMIN` | Consultar agente local Ollama |
| `POST` | `/mcp` | `ADMIN` | Inicializar y ejecutar herramientas MCP |

---

# ⚙️ Instalación y configuración

## Requisitos

- Java 25.
- MySQL.
- Maven Wrapper incluido en el proyecto.
- Ollama activo para probar el agente local y MCP.
- Modelo local utilizado:

```bash
ollama pull qwen3:4b
```

## Base de datos

Crear la base:

```sql
CREATE DATABASE smartqr_database;
```

## Variables de entorno

Configurar en IntelliJ o en el sistema:

```text
PASS_DB=tu_password_mysql
OPENAI_API_KEY=tu_api_key_de_openai
JWT_SECRET=una_clave_larga_y_privada_para_firmar_tokens
```

La clave JWT no se guarda en el repositorio.

## Arranque

```bash
./mvnw spring-boot:run
```

Durante el desarrollo la aplicación utiliza datos seed para poder repetir la demo desde una base limpia. Al reiniciar, se vuelven a cargar los usuarios y productos de prueba definidos para el flujo.

## Swagger UI

```text
http://localhost:8080/swagger-ui.html
```

---

# 🧪 Pruebas y demostración

Los requests están separados por perfil y por pruebas negativas:

```text
requests/CUSTOMER_REQUESTS.http
requests/EMPLOYEE_REQUESTS.http
requests/ADMIN_REQUESTS.http
requests/EXCEPTION_VALIDATION_REQUESTS.http
```

Cada archivo de perfil dispone de una variable visible para pegar manualmente el token que devuelve el login:

```http
@customerToken = PEGA_AQUI_EL_TOKEN_CUSTOMER
```

Para ejecutar la demo funcional desde base limpia he creado un demo que incluye todas las peticiones necesarias para ejecutar el flujo completo desde un archivo único:

```text
0. DEMO_REQUESTS.http`: demo final del flujo completo por roles, pedidos, pagos, feedback, dashboard, agente local y MCP.

Los demas archivos de requests son:
1. CUSTOMER_REQUESTS.http
2. EMPLOYEE_REQUESTS.http
3. ADMIN_REQUESTS.http
4. ECEPTION_VALIDATION_REQUESTS.http
```

El archivo de excepciones se utiliza aparte para comprobar validaciones, conflictos, permisos y recursos inexistentes.

---

# 🗂️ Documentación visual

- [Class Diagram](docs/CLASS_DIAGRAM.png)
- [Use Case Diagram](docs/USE_CASE_DIAGRAM.drawio.png)
- [Technical Diagrams](docs/DIAGRAMS.drawio.png)
- [Roadmap de arquitectura](docs/ROADMAP_SMARTQR.md)
- [Task Manager](docs/TASKS_SMARTQR_API.md)

La idea inicial de mostrar al cliente una gráfica de espera con mensajes generados por IA queda documentada como **mejora futura**. En la versión actual, el cliente consulta el estado real de su propio pedido y el personal interno gestiona la cola de cocina.
---

# 🔗 Extra Links

- GitHub Repository: https://github.com/fcomanuelcandela90-dev/SmartQR_API
- Project Management: [Task Manager](docs/TASKS_SMARTQR_API.md)
- Presentation Slides PDF: [SmartQR Presentation PDF](docs/SMARTQR_PRESENTATION.pdf)
- Presentation Slides PPTX: [SmartQR Presentation PPTX](docs/SMARTQR_PRESENTATION.pptx)
---

# 🌱 GitFlow

El desarrollo se ha realizado mediante ramas por funcionalidad integradas primero en `develop`.

Para la entrega final:

```text
release/v1.0.0 → develop → main → tag v1.0.0
```

De esta forma, `develop` contiene la integración validada y `main` representa la versión final entregable.

---

# 🔭 Mejoras futuras

- Seguimiento visual de espera del pedido propio.
- Estimación de tiempo y mensajes de espera personalizados por IA.
- Notificaciones en tiempo real mediante WebSocket.
- Integración con una pasarela de pago real.
- Despliegue cloud.
- Añadir memoria conversacional al agente administrativo para mantener contexto entre preguntas.

---
# 📚 Referencias y documentación consultada

* **Ironhack.** *Java Backend Development Bootcamp - materiales de clase y Student Portal*. Contenidos docentes utilizados como guía para la arquitectura por capas, Spring Security, GitFlow y estructura del proyecto.
* [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/index.html)
* [Spring Security Reference Documentation](https://docs.spring.io/spring-security/reference/index.html)
* [Spring AI Reference Documentation](https://docs.spring.io/spring-ai/reference/index.html)
* [OpenAI Platform Documentation](https://platform.openai.com/docs)
* [Model Context Protocol - Official Specification](https://modelcontextprotocol.io/specification)
* [Ollama Documentation](https://docs.ollama.com/)
* [JFreeChart Project Documentation](https://www.jfree.org/jfreechart/)
* [ZXing - QR Code Library](https://github.com/zxing/zxing)
* [MySQL Documentation](https://dev.mysql.com/doc/)

---

# 👤 Team member

Proyecto individual desarrollado por:

Francisco Manuel Candela Manchón - MáNueL  
[GitHub](https://github.com/fcomanuelcandela90-dev) · [LinkedIn](https://www.linkedin.com/in/francisco-manuel-candela-manch%C3%B3n-506559357/)

---

# 📜 Licencia

Proyecto educativo desarrollado con fines de aprendizaje.
