# 🗺️ ROADMAP ARCHITECTURE - SMARTQR API

---

# 📖 Qué es SmartQR

**SmartQR** es una API REST creada como proyecto final de backend para digitalizar el flujo de un restaurante. La idea principal es sencilla: un cliente consulta la carta desde una mesa con QR, crea su pedido, paga y puede dejar una opinión; mientras tanto, empleados y administración tienen herramientas distintas según sus responsabilidades.

La versión `v1.0.0` se centra en un flujo completo, seguro y demostrable desde archivos `.http`, sin necesitar un frontend.

---

# 🎯 Objetivo de la versión `v1.0.0`

La entrega final permite demostrar:

- Carta pública y pedidos asociados a un cliente autenticado.
- Pagos por tarjeta simulada y efectivo confirmado por personal interno.
- Tickets de pedidos pagados.
- Control de acceso por roles y protección de pedidos propios.
- OpenAI para feedback y recomendaciones de combos.
- Dashboard administrativo con gráficos PNG.
- Estadísticas administrativas del feedback analizado.
- Agente local Ollama y herramientas MCP de solo lectura.
- Validaciones y errores HTTP coherentes.

---

# 👥 Perfiles del sistema

## Customer

El cliente puede:

- Consultar el menú disponible.
- Crear una comanda.
- Consultar el estado de su propio pedido.
- Pagar con tarjeta o solicitar un pago en efectivo.
- Descargar su ticket cuando el pago ya está completado.
- Enviar feedback para análisis de sentimiento.
- Solicitar una recomendación de combo con IA.

**Decisión de seguridad:** el cliente no consulta la cola global de cocina; solo puede ver sus propios pedidos.

## Employee

El empleado puede:

- Generar códigos QR de mesa.
- Consultar productos fuera de stock.
- Ver pedidos activos de mesa y cola de cocina.
- Modificar pedidos pendientes.
- Cambiar el estado operativo de los pedidos.
- Cancelar una comanda si las reglas lo permiten.
- Confirmar pagos en efectivo.

## Admin

El administrador puede:

- Gestionar productos mediante CRUD.
- Consultar métricas de ventas.
- Generar gráficos PNG.
- Consultar estadísticas del feedback clasificado por OpenAI.
- Utilizar el agente local Ollama.
- Ejecutar tools MCP read-only sobre métricas, cocina y menú.

---

# 🏗️ Arquitectura final

## Capas principales

```text
Controller → DTO → Service → Repository → Entity → MySQL
```

## Integraciones externas y componentes especiales

```text
SmartQR REST API
├── MySQL                 → Datos persistentes de negocio
├── Spring Security + JWT → Autenticación, roles y ownership
├── OpenAI / Spring AI    → Sentimiento de feedback y recomendación de combos
├── Ollama / Spring AI    → Agente administrativo local
├── MCP Server HTTP       → Herramientas administrativas de solo lectura
└── JFreeChart            → Gráficos PNG del dashboard
```

---

# 🔐 Seguridad aplicada

## Autenticación

```text
POST /api/login → JWT
```

El token se envía en las rutas protegidas mediante:

```http
Authorization: Bearer <token>
```

## Roles

```text
CUSTOMER → pedidos propios, pagos, tickets propios, feedback y combos
EMPLOYEE → QR, cocina y confirmación de cash
ADMIN    → productos, dashboard, gráficos, feedback analytics, Ollama y MCP
```

## Refactor final de seguridad

Durante la revisión final se realizaron dos mejoras importantes:

- `PasswordEncoder` se separó en `security/EncoderConfig`, usando el patrón trabajado en clase con `DelegatingPasswordEncoder`.
- La clave de firma JWT dejó de estar escrita en los filtros y se configuró mediante la variable de entorno `JWT_SECRET`.

## Ownership de pedidos

Además de validar el rol, la API impide que un cliente pueda consultar, pagar, descargar ticket o enviar feedback sobre pedidos de otro cliente.

---

# 🧬 Modelo de pagos

## Herencia JPA

```text
Payment
├── CardPayment
└── CashPayment
```

Se utiliza:

```java
InheritanceType.JOINED
```

## Pago con tarjeta

```text
Customer crea pedido
        ↓
Customer envía pago con tarjeta simulada
        ↓
Pago completado
        ↓
Ticket disponible
```

## Pago en efectivo

```text
Customer crea pedido
        ↓
Customer solicita pagar en efectivo
        ↓
Pago pendiente de confirmación
        ↓
Employee o Admin confirma el efectivo
        ↓
Pago completado y ticket disponible
```

El pago recibe `orderId` porque el token identifica al cliente, pero el backend necesita saber qué comanda concreta se está pagando.

---

# 📋 Flujo operativo del pedido

```text
Customer crea pedido
        ↓
Employee consulta cocina o mesa
        ↓
Employee puede modificar el pedido pendiente
        ↓
Employee actualiza su estado operativo
        ↓
Customer puede consultar el estado de su propio pedido
```

La cola completa de cocina se mantiene reservada a `EMPLOYEE` y `ADMIN`.

---

# 🤖 Inteligencia artificial

## OpenAI: funcionalidades del cliente

### Feedback

```text
Customer envía feedback
        ↓
OpenAI clasifica sentimiento
        ↓
SmartQR guarda POSITIVE / NEUTRAL / NEGATIVE
        ↓
Admin consulta estadísticas agregadas
```

### Recomendación de combos

```text
Customer envía preferencias y presupuesto
        ↓
AiService consulta productos disponibles
        ↓
OpenAI redacta recomendación de combo
```

## Ollama y MCP: administración local

```text
Admin formula una pregunta
        ↓
Agente local con qwen3:4b
        ↓
Tools read-only
        ↓
Métricas / cola de cocina / menú disponible
```

También se publican estas tools mediante el endpoint MCP para demostrar el protocolo con `tools/list` y `tools/call`.

**Decisión de diseño:** OpenAI se usa en experiencia de cliente; Ollama/MCP se reserva para consultas internas del administrador.

---

# 📊 Dashboard administrativo

```text
ADMIN → GET /dashboard/metrics
ADMIN → GET /dashboard/charts/income-by-payment-method → PNG circular
ADMIN → GET /dashboard/charts/product-sales → PNG de barras
ADMIN → GET /feedback/statistics → métricas del feedback analizado
```

Los gráficos se generan con JFreeChart y sus PNG locales quedan ignorados por Git.

---

# ⚠️ Validaciones y errores

La API utiliza DTOs validados, excepciones personalizadas y `GlobalExceptionHandler`.

| Situación probada | HTTP esperado |
|---|---:|
| Datos inválidos o regla de negocio | `400` |
| Token inválido o caducado | `401` |
| Acceso sin permiso o pedido ajeno | `403` |
| Recurso inexistente | `404` |
| Conflicto como email duplicado o pago repetido | `409` |
| Problema de servicio externo de IA | `503` |

Las pruebas negativas están documentadas en:

```text
requests/EXCEPTION_VALIDATION_REQUESTS.http
```

---

# 🌱 Evolución del desarrollo

```text
setup/init-project
        ↓
feature/auth-security
        ↓
feature/catalog-management
        ↓
feature/order-processing
        ↓
feature/payments-billing
        ↓
bugfix/auth-login-flow
        ↓
bugfix/product-seed-data
        ↓
feature/security-rbac
        ↓
bugfix/customer-order-ownership
        ↓
feature/ai-integration
        ↓
feature/analytics-dashboard
        ↓
feature/mcp-local-agent
        ↓
feature/exception-validation
        ↓
feature/feedback-analytics
        ↓
feature/final-refactor-review
        ↓
release/v1.0.0
```

---

# 🧪 Demo final

La demo se ejecuta con base de datos limpia y archivos HTTP separados por rol:

```text
CUSTOMER_REQUESTS.http → EMPLOYEE_REQUESTS.http → ADMIN_REQUESTS.http
```

Además:

```text
EXCEPTION_VALIDATION_REQUESTS.http → evidencia independiente de validaciones y seguridad
```

Los tokens JWT se copian manualmente en la cabecera de cada archivo para que el flujo sea fácil de explicar durante la presentación.

---

# 🔭 Mejora futura que no entra en `v1.0.0`

La idea inicial incluía una representación visual del tiempo de espera del cliente, con posición aproximada en la cola y un mensaje generado por IA.

Se mantiene como mejora futura porque necesitaría:

- Definir una regla fiable de estimación de tiempo.
- Mostrar información del pedido propio sin exponer la cola de otros clientes.
- Añadir endpoint y DTO específicos.
- Generar un nuevo gráfico.
- Volver a probar seguridad, excepciones y demo completa.

Para `v1.0.0`, la solución entregada es clara y segura:

```text
Customer consulta el estado de su propio pedido.
Employee y Admin consultan la cola operativa de cocina.
```
