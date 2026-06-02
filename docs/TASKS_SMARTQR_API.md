# 📋 Task Manager - SMARTQR Final Project (GitFlow)

## ✅ Fase 1: `setup/init-project`

* [x] 1.1: Inicializar Spring Boot, `pom.xml`, `application.properties` y estructura base de carpetas.
* [x] 1.2: Crear entidades con herencia (`Payment` → `CardPayment` / `CashPayment`), enums y repositorios JPA.
* [x] 1.3: Añadir diagramas de arquitectura, roadmap y plantillas HTTP iniciales.

---

## ✅ Fase 2: `feature/auth-security`

* [x] 2.1: Implementar autenticación JWT mediante filtros de Spring Security.
* [x] 2.2: Crear `AuthController`, `AuthenticationService` y DTOs de autenticación.
* [x] 2.3: Configurar acceso autenticado inicial en `SecurityConfig`.

---

## ✅ Fase 3: `feature/catalog-management`

* [x] 3.1: Implementar CRUD de productos mediante `ProductController`, `ProductService` y DTOs.
* [x] 3.2: Añadir pruebas HTTP del catálogo de productos.

---

## ✅ Fase 4: `feature/order-processing`

* [x] 4.1: Implementar creación de comandas, generación de QR y cálculo de subtotales.
* [x] 4.2: Implementar gestión de estados, modificaciones de pedidos y cola de cocina.
* [x] 4.3: Añadir pruebas HTTP para cliente y empleado.

---

## ✅ Fase 5: `feature/payments-billing`

* [x] 5.1: Implementar pasarela simulada de tarjeta y flujo de pago en efectivo.
* [x] 5.2: Implementar endpoint de generación de tickets imprimibles de pedido.
* [x] 5.3: Validar que un pago en efectivo insuficiente sea rechazado.
* [x] 5.4: Añadir pruebas HTTP de pagos, ticket y autenticación por perfil.

---

## ✅ Corrección detectada durante pruebas: `bugfix/auth-login-flow`

* [x] A.1: Investigar por qué el registro funcionaba pero el login devolvía `403`.
* [x] A.2: Crear `CustomUserDetailsService` para cargar usuarios registrados desde MySQL.
* [x] A.3: Configurar el filtro de autenticación para procesar `POST /api/login`.
* [x] A.4: Validar generación de JWT y acceso autenticado a rutas protegidas.
* [x] A.5: Integrar la corrección en `develop` y posteriormente en la rama de pagos.

---

## ✅ Corrección detectada durante pruebas: `bugfix/product-seed-data`

* [x] D.1: Añadir `data.sql` con catálogo inicial válido para probar pedidos y pagos.
* [x] D.2: Eliminar datos duplicados del catálogo inicial.
* [x] D.3: Añadir restricción única al nombre de producto para evitar duplicados.
* [x] D.4: Validar arranque de la aplicación y carga correcta del menú.
* [x] D.5: Integrar la corrección en `develop` y posteriormente en la rama de pagos.

---

## 🔲 Security Hardening: `feature/security-rbac`

* [x] S.1: Auditar las rutas actuales de productos, pedidos, pagos y QR.
* [x] S.2: Implementar autorización real basada en roles (`CUSTOMER`, `EMPLOYEE`, `ADMIN`) en `SecurityConfig`.
- [x] S.3: Mantener público el acceso al menú digital y restringir la generación de QR de mesa a `EMPLOYEE` y `ADMIN`.
- [x] S.4: Guardar los códigos QR generados en `requests/table_qr/` identificados por número de mesa.
- [x] S.5: Añadir usuarios de prueba controlados para `EMPLOYEE` y `ADMIN`, sin permitir su registro público.
- [x] S.6: Actualizar los archivos HTTP de cliente, empleado y administrador para probar permisos por rol.
- [x] S.7: Validar accesos permitidos y denegados para cada perfil.
- [x] S.8: Documentar la matriz inicial de autorización por roles y el flujo de generación de QR.

---
## 🔲 Corrección detectada durante pruebas: `bugfix/customer-order-ownership`

- [x] O.1: Impedir que un cliente consulte pedidos pertenecientes a otro usuario.
- [x] O.2: Impedir que un cliente pague con tarjeta un pedido perteneciente a otro usuario.
- [x] O.3: Impedir que un cliente solicite pago cash para un pedido perteneciente a otro usuario.
- [x] O.4: Impedir que un cliente consulte tickets pertenecientes a otro usuario.
- [x] O.5: Validar mediante requests intentos de acceso cruzado entre dos clientes.

---

## 🔲 Fase 6: `feature/ai-integration`

* [x] 6.1: Configurar Spring AI con OpenAI mediante la variable de entorno segura `OPENAI_API_KEY`.
* [x] 6.2: Implementar análisis de sentimiento en feedback mediante `FeedBackController`, `FeedBackService` y DTOs.
* [ ] 6.3: Implementar recomendaciones automatizadas de productos o combos mediante `AiController` y `AiService`.
* [ ] 6.4: Añadir pruebas HTTP de endpoints de IA según rol autorizado.

---

## 🔲 Fase 7: `feature/analytics-dashboard`

* [ ] 7.1: Implementar consultas SQL/JPA para métricas de ingresos y ventas.
* [ ] 7.2: Crear dashboard administrativo mediante `DashboardController`, `DashboardService` y DTO.
* [ ] 7.3: Generar gráficos PNG en servidor mediante `ChartController`, `ChartService` y JFreeChart.
* [ ] 7.4: Añadir pruebas HTTP del dashboard y de generación de gráficos para `ADMIN`.

---

## 🔲 Fase 8: `feature/mcp-local-agent`

* [ ] 8.1: Evaluar e integrar dependencias de Ollama para agente local.
* [ ] 8.2: Implementar herramientas del agente autónomo mediante `@Tool` y llamadas MCP.
* [ ] 8.3: Documentar claramente qué parte utiliza proveedor remoto y qué parte utiliza agente local.
* [ ] 8.4: Añadir pruebas demostrables del agente local si la integración es estable.

---

## 🔲 Fase 9: `bugfix/final-adjustments`

* [ ] 9.1: Implementar `GlobalExceptionHandler`, `ResourceNotFoundException` y `ErrorResponse`.
* [ ] 9.2: Corregir códigos HTTP de errores de negocio, evitando devolver `403` para errores de validación.
* [ ] 9.3: Auditar e implementar validaciones `@Valid` y anotaciones de validación en DTOs.
* [ ] 9.4: Revisar comentarios, nombres de clases, imports y código no utilizado.
* [ ] 9.5: Ejecutar pruebas positivas y negativas de todas las rutas mediante archivos `.http`.
* [ ] 9.6: Verificar que la autorización por roles funciona también en endpoints de IA y dashboard.

---

## 🔲 Fase 10: `release/v1.0`

* [ ] 10.1: Ocultar credenciales, tokens y API Keys; higienizar `application.properties`.
* [ ] 10.2: Crear o actualizar el UML Class Diagram final para que coincida con el código entregado.
* [ ] 10.3: Corregir y completar `README.md` según rutas, clases, tecnologías y configuración reales.
* [ ] 10.4: Añadir enlaces a repositorio, herramienta de gestión de tareas y presentación online.
* [ ] 10.5: Preparar slides online y guion de demo de cinco minutos.
* [ ] 10.6: Ejecutar revisión final de arranque, Swagger, requests, seguridad y GitHub.
* [ ] 10.7: Crear release final `v1.0`.

---

# 🧪 Evidencias de Testing Confirmadas

* [x] Registro de cliente y login mediante JWT.
* [x] Acceso autenticado a ruta protegida.
* [x] Consulta de menú con datos iniciales cargados.
* [x] Creación de pedido.
* [x] Pago con tarjeta y transición del pedido a cocina.
* [x] Generación de ticket imprimible para pedido pagado.
* [x] Rechazo de pago en efectivo con importe insuficiente.
* [x] Rechazo de ticket para pedido sin pago completado.

---

# ⚠️ Deudas Técnicas Controladas

* [ ] Corregir en Fase 9 los errores de negocio que actualmente terminan respondiendo con HTTP `403`.
* [ ] Mantener el README sincronizado con las rutas y clases realmente implementadas.
* [ ] Crear y enlazar una herramienta externa de gestión de proyecto, como Trello o GitHub Projects.
* [ ] Incorporar el UML Class Diagram final al repositorio y al README.
* [ ] No documentar funcionalidades de IA, dashboard o MCP como terminadas hasta que estén implementadas y probadas.
