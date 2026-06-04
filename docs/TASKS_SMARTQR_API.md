# 📋 Task Manager - SMARTQR API Final Project (GitFlow)


## Estado actual del proyecto

**Rama en desarrollo:** `feature/final-refactor-review`

A estas alturas la API ya permite gestionar menú, pedidos, pagos, roles, dashboard, gráficos, IA con OpenAI, agente local con Ollama/MCP, excepciones controladas y estadísticas de feedback para administración.

La última parte del trabajo no consiste en añadir funcionalidades grandes, sino en dejar el proyecto seguro, limpio, fácil de explicar en la presentación y probado de principio a fin.

---

## ✅ Fase 1: `setup/init-project`

**Objetivo:** crear la base del proyecto para poder desarrollar por funcionalidades sin mezclarlo todo desde el principio.

* [x] Inicializar Spring Boot, `pom.xml`, `application.properties` y carpetas principales.
* [x] Crear entidades, enums y repositorios JPA.
* [x] Modelar la herencia de pagos: `Payment` → `CardPayment` / `CashPayment`.
* [x] Añadir roadmap, diagramas iniciales y primeros archivos HTTP.

---

## ✅ Fase 2: `feature/auth-security`

**Objetivo:** permitir que los usuarios puedan identificarse y recibir un token JWT para acceder a las rutas protegidas.

* [x] Crear DTOs, `AuthController` y `AuthenticationService`.
* [x] Implementar autenticación JWT mediante filtros de Spring Security.
* [x] Configurar una primera protección de endpoints en `SecurityConfig`.

---

## ✅ Fase 3: `feature/catalog-management`

**Objetivo:** disponer de un menú administrable y visible para el cliente.

* [x] Implementar CRUD de productos con controlador, servicio y DTOs.
* [x] Exponer el menú público de productos disponibles.
* [x] Añadir pruebas HTTP del catálogo.

---

## ✅ Fase 4: `feature/order-processing`

**Objetivo:** convertir el menú en un flujo real de restaurante: el cliente pide y el personal gestiona la comanda.

* [x] Implementar creación de pedidos y cálculo de subtotales en backend.
* [x] Implementar consulta, modificación, cancelación y estados del pedido.
* [x] Implementar cola de cocina.
* [x] Añadir generación de QR de mesa y pruebas HTTP para cliente y empleado.

---

## ✅ Fase 5: `feature/payments-billing`

**Objetivo:** cerrar el ciclo del pedido con pago y ticket.

* [x] Implementar pago simulado con tarjeta.
* [x] Implementar solicitud y confirmación de pago en efectivo.
* [x] Implementar generación de ticket imprimible.
* [x] Validar que un pago cash insuficiente sea rechazado.
* [x] Añadir pruebas HTTP de pagos y ticket.

---

## ✅ Corrección durante pruebas: `bugfix/auth-login-flow`

**Motivo:** el registro funcionaba, pero los usuarios registrados no podían iniciar sesión correctamente.

* [x] Investigar el `403` del login.
* [x] Crear `CustomUserDetailsService` para cargar usuarios desde MySQL.
* [x] Configurar correctamente `POST /api/login`.
* [x] Validar generación de JWT y acceso autenticado.
* [x] Integrar la corrección en `develop`.

---

## ✅ Corrección durante pruebas: `bugfix/product-seed-data`

**Motivo:** sin productos iniciales consistentes no se podían probar bien pedidos ni pagos.

* [x] Crear `data.sql` con catálogo de prueba.
* [x] Eliminar productos duplicados.
* [x] Añadir restricción única al nombre del producto.
* [x] Validar carga del menú al reiniciar la aplicación.
* [x] Integrar la corrección en `develop`.

---

## ✅ Refuerzo de seguridad: `feature/security-rbac`

**Objetivo:** que cada perfil pueda hacer solo lo que le corresponde.

* [x] Auditar rutas de productos, pedidos, pagos y QR.
* [x] Aplicar roles `CUSTOMER`, `EMPLOYEE` y `ADMIN` en `SecurityConfig`.
* [x] Mantener público el menú digital.
* [x] Restringir generación de QR a personal interno.
* [x] Guardar QR generados localmente e ignorarlos en Git.
* [x] Crear usuarios controlados para pruebas de `EMPLOYEE` y `ADMIN`.
* [x] Añadir requests por rol y validar accesos permitidos y denegados.
* [x] Documentar la matriz inicial de permisos.

---

## ✅ Corrección durante pruebas: `bugfix/customer-order-ownership`

**Motivo:** un cliente no debe poder acceder a un pedido ajeno modificando manualmente el ID en la URL.

* [x] Impedir que un cliente consulte pedidos de otro cliente.
* [x] Impedir pagos con tarjeta sobre pedidos ajenos.
* [x] Impedir solicitudes cash sobre pedidos ajenos.
* [x] Impedir consulta de tickets ajenos.
* [x] Validar accesos cruzados entre dos clientes.

---

## ✅ Fase 6: `feature/ai-integration`

**Objetivo:** utilizar OpenAI en funcionalidades visibles para el cliente.

* [x] Configurar Spring AI con OpenAI usando `OPENAI_API_KEY`.
* [x] Analizar el sentimiento de un feedback al guardarlo.
* [x] Crear recomendaciones automáticas de combos con productos reales del menú.
* [x] Añadir pruebas HTTP para feedback y recomendaciones.

**Decisión explicable:** OpenAI se utiliza en funcionalidades orientadas al cliente porque interpreta texto libre y genera recomendaciones.

---

## ✅ Fase 7: `feature/analytics-dashboard`

**Objetivo:** que administración pueda consultar resultados del restaurante de forma visual.

* [x] Calcular ingresos y ventas usando consulta SQL nativa, JPQL y lógica sencilla en servicio.
* [x] Crear endpoint de métricas administrativas.
* [x] Generar PNG con JFreeChart: gráfico circular de ingresos y barras de productos vendidos.
* [x] Ajustar escala de productos vendidos a unidades enteras.
* [x] Ignorar imágenes PNG generadas localmente en Git.
* [x] Añadir pruebas HTTP para `ADMIN`.

---

## ✅ Fase 8: `feature/mcp-local-agent`

**Objetivo:** añadir un asistente administrativo local, separado de las funciones de cliente que usan OpenAI.

* [x] Configurar Ollama con el modelo local `qwen3:4b`.
* [x] Mantener OpenAI como cliente principal y seleccionar Ollama mediante `@Qualifier` para el agente local.
* [x] Crear endpoint de agente administrativo protegido para `ADMIN`.
* [x] Crear herramientas de solo lectura para métricas, cocina y menú.
* [x] Publicar esas herramientas mediante MCP HTTP.
* [x] Validar `tools/list` y `tools/call`.
* [x] Añadir pruebas HTTP del agente y MCP.

**Decisión explicable:** el agente local consulta información interna sin modificar datos; por eso sus herramientas están marcadas como de solo lectura y no destructivas.

---

## ✅ Fase 9: `feature/exception-validation`

**Objetivo:** que los errores de la API expliquen realmente lo que ha ocurrido.

* [x] Crear `ErrorResponse`, excepciones personalizadas y `GlobalExceptionHandler`.
* [x] Corregir estados HTTP: validaciones `400`, autenticación inválida `401`, acceso prohibido `403`, recurso inexistente `404` y conflicto `409`.
* [x] Aplicar validaciones en DTOs y controladores con `@Valid`.
* [x] Corregir el filtro JWT para que un error de negocio no termine fingiendo ser un `403`.
* [x] Crear `EXCEPTION_VALIDATION_REQUESTS.http`.
* [x] Validar errores de registro, pedidos, pagos, IA, feedback y permisos reales.

**Decisión explicable:** `403` queda reservado para operaciones realmente prohibidas, como acceder al pedido de otro cliente o intentar entrar en una ruta de administrador.

---

## ✅ Fase 10: `feature/feedback-analytics`

**Objetivo:** completar el caso de uso del diagrama en el que administración consulta resultados del feedback analizado por IA.

* [x] Crear `FeedbackStatisticsResponse`.
* [x] Calcular total de opiniones, media de valoración y conteos `POSITIVE`, `NEUTRAL` y `NEGATIVE`.
* [x] Exponer `GET /feedback/statistics` solo para `ADMIN`.
* [x] Añadir prueba HTTP en el archivo de administrador.
* [x] Validar el flujo: el cliente envía feedback, OpenAI guarda el sentimiento y administración consulta la estadística.

**Decisión explicable:** el endpoint de estadísticas no vuelve a llamar a OpenAI; utiliza el sentimiento ya guardado cuando se creó el feedback.

---

## 🚧 Fase 11: `feature/final-refactor-review`

**Objetivo:** dejar el proyecto con una estructura de seguridad que pueda explicar en clase y ejecutar una comprobación final sin automatismos que no hemos trabajado en el bootcamp.

* [x] 11.1: Separar `PasswordEncoder` en `security/EncoderConfig`, como en el ejemplo de clase, usando `PasswordEncoderFactories.createDelegatingPasswordEncoder()`.
    * Justificación: la seguridad queda separada por responsabilidades y los hashes identifican su algoritmo con `{bcrypt}`.
    * Validación realizada: login correcto de `ADMIN`, `EMPLOYEE` y un `CUSTOMER` nuevo.

* [x] 11.2: Sacar la clave JWT de los filtros y leerla desde la variable de entorno `JWT_SECRET`.
    * Justificación: la clave que firma tokens no debe quedar escrita directamente en el código ni subirse al repositorio.
    * Validación realizada: login correcto de los tres perfiles y acceso a rutas protegidas utilizando tokens generados con la nueva variable de entorno.

* [x] 11.3: Sustituir los archivos `.http` por variables de token visibles en la cabecera para un demo en la presentación.
    * Justificación: después de hacer login copio el token una sola vez arriba del archivo y puedo ejecutar el resto de peticiones de forma clara durante la presentación.
    * Archivos a revisar: `CUSTOMER_REQUESTS.http`, `EMPLOYEE_REQUESTS.http`, `ADMIN_REQUESTS.http` y `EXCEPTION_VALIDATION_REQUESTS.http`.

* [x] 11.4: Auditar el DTO de entrada de líneas de pedido y decidir si se eliminan campos que el backend no utiliza (`productName` y `subtotal`).
    * Justificación: el cliente solo debería enviar producto, cantidad y notas; el nombre y precio real deben salir de la base de datos.

* [x] 11.5: Ejecutar el flujo funcional completo con los tres perfiles y comprobar permisos finales.
    * Incluye: menú, QR, pedidos, pagos, ticket, feedback, recomendación IA, dashboard, gráficos, estadísticas de feedback, agente local y MCP.

* [x] 11.6: Limpiar imports, comentarios antiguos, archivos temporales y revisar el diff antes del commit.
    * Justificación: cerrar la rama solo con cambios necesarios y explicables.

---

## 🔲 Fase 12: `release/v1.0.0`

**Objetivo:** preparar la entrega y la demostración final del proyecto.

* [ ] 12.1: Preparar un flujo de demo corto y ordenado para la presentación.
* [ ] 12.2: Actualizar `README.md` con configuración, variables de entorno, arquitectura, endpoints y cómo probar el proyecto.
* [ ] 12.3: Actualizar diagramas para que coincidan con las rutas y funcionalidades finales.
* [ ] 12.4: Revisar que no se suban claves, tokens, imágenes generadas ni archivos temporales.
* [ ] 12.5: Preparar slides y guion de presentación.
* [ ] 12.6: Ejecutar la última prueba completa desde una base de datos limpia.
* [ ] 12.7: Integrar ramas pendientes, crear tag y publicar release `v1.0.0`.
