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

## ✅ Security Hardening: `feature/security-rbac`

* [x] S.1: Auditar las rutas actuales de productos, pedidos, pagos y QR.
* [x] S.2: Implementar autorización real basada en roles (`CUSTOMER`, `EMPLOYEE`, `ADMIN`) en `SecurityConfig`.
- [x] S.3: Mantener público el acceso al menú digital y restringir la generación de QR de mesa a `EMPLOYEE` y `ADMIN`.
- [x] S.4: Guardar los códigos QR generados en `requests/table_qr/` identificados por número de mesa.
- [x] S.5: Añadir usuarios de prueba controlados para `EMPLOYEE` y `ADMIN`, sin permitir su registro público.
- [x] S.6: Actualizar los archivos HTTP de cliente, empleado y administrador para probar permisos por rol.
- [x] S.7: Validar accesos permitidos y denegados para cada perfil.
- [x] S.8: Documentar la matriz inicial de autorización por roles y el flujo de generación de QR.

---
## ✅ Corrección detectada durante pruebas: `bugfix/customer-order-ownership`

- [x] O.1: Impedir que un cliente consulte pedidos pertenecientes a otro usuario.
- [x] O.2: Impedir que un cliente pague con tarjeta un pedido perteneciente a otro usuario.
- [x] O.3: Impedir que un cliente solicite pago cash para un pedido perteneciente a otro usuario.
- [x] O.4: Impedir que un cliente consulte tickets pertenecientes a otro usuario.
- [x] O.5: Validar mediante requests intentos de acceso cruzado entre dos clientes.

---

## ✅ Fase 6: `feature/ai-integration`

* [x] 6.1: Configurar Spring AI con OpenAI mediante la variable de entorno segura `OPENAI_API_KEY`.
* [x] 6.2: Implementar análisis de sentimiento en feedback mediante `FeedBackController`, `FeedBackService` y DTOs.
* [x] 6.3: Implementar recomendaciones automatizadas de productos o combos mediante `AiController` y `AiService`.
* [x] 6.4: Añadir pruebas HTTP de endpoints de IA según rol autorizado.

---

## ✅ Fase 7: `feature/analytics-dashboard`

* [x] 7.1: Implementar métricas de ingresos mediante una consulta SQL nativa, una consulta JPQL y cálculos básicos en servicio.
* [x] 7.2: Crear dashboard administrativo mediante `DashboardController`, `DashboardService` y DTOs.
* [x] 7.3: Generar gráficos PNG en servidor mediante `ChartController`, `ChartService` y JFreeChart.
* [x] 7.4: Añadir pruebas HTTP del dashboard y de generación de gráficos para `ADMIN`.

---

## 🚧 Fase 8: `feature/mcp-local-agent`

* [ ] 8.1: Investigar y configurar la integración local con Ollama para el agente MCP.
* [ ] 8.2: Crear el servicio del agente local y definir herramientas mediante `@Tool`.
* [ ] 8.3: Implementar un caso de uso demostrable del agente sobre datos o acciones del sistema.
* [ ] 8.4: Proteger los endpoints del agente según el rol autorizado.
* [ ] 8.5: Añadir pruebas HTTP y documentar claramente qué funcionalidad usa OpenAI y cuál usa Ollama/MCP.

---

## 🔲 Fase 9: `bugfix/exception-validation`

* [ ] 9.1: Crear `ErrorResponse`, excepciones personalizadas y `GlobalExceptionHandler`.
* [ ] 9.2: Corregir los códigos HTTP de errores de negocio, evitando responder `403` para validaciones, recursos inexistentes o conflictos.
* [ ] 9.3: Auditar DTOs y aplicar validaciones con `@Valid`, `@NotNull`, `@NotBlank`, `@Min`, `@Max` y restricciones necesarias.
* [ ] 9.4: Validar mediante requests los errores de registro, pedidos, pagos, feedback, IA y dashboard.
* [ ] 9.5: Comprobar que `403 Forbidden` queda reservado para accesos realmente no autorizados.

---

## 🔲 Fase 10: `bugfix/final-refactor-security`

* [ ] 10.1: Separar la configuración de contraseñas en `security/EncoderConfig`, manteniendo BCrypt para no romper usuarios existentes.
* [ ] 10.2: Refactorizar la capa de servicios siguiendo la estructura enseñada (`service` + `service/impl`) sin alterar la lógica ya validada.
* [ ] 10.3: Externalizar la clave secreta JWT y eliminar secretos escritos directamente en código o propiedades.
* [ ] 10.4: Auditar permisos RBAC de todos los endpoints finales, incluidos IA, dashboard y MCP.
* [ ] 10.5: Limpiar imports, comentarios, nombres, código no utilizado y archivos temporales.
* [ ] 10.6: Crear un archivo HTTP end-to-end definitivo para validar el flujo completo de `CUSTOMER`, `EMPLOYEE` y `ADMIN`.

---

## 🔲 Fase 11: `release/v1.0`

* [ ] 11.1: Ocultar credenciales, tokens y API Keys; higienizar `application.properties` y documentación.
* [ ] 11.2: Crear o actualizar el UML Class Diagram final para que coincida con el código entregado.
* [ ] 11.3: Corregir y completar `README.md` con configuración, endpoints, arquitectura, tecnologías y pruebas reales.
* [ ] 11.4: Añadir enlaces al repositorio, herramienta de gestión de tareas y presentación online.
* [ ] 11.5: Preparar slides online y guion de demo de cinco minutos.
* [ ] 11.6: Ejecutar revisión final de arranque, requests, seguridad, gráficos, IA, MCP y GitHub.
* [ ] 11.7: Integrar ramas pendientes, crear tag y publicar release final `v1.0`.

---

# 🧪 Evidencias pendientes de validación

* [ ] Dashboard administrativo accesible únicamente para `ADMIN`.
* [ ] Gráfico circular de ingresos por método de pago generado como PNG.
* [ ] Gráfico de productos vendidos generado con escala de unidades enteras.
* [ ] PNG generados localmente ignorados por Git.
* [ ] Agente MCP local funcional y documentado.
* [ ] Errores HTTP corregidos mediante manejo global de excepciones.
* [ ] Flujo end-to-end final por perfiles ejecutado correctamente.

---

# ⚠️ Deudas técnicas controladas

* [ ] Corregir en Fase 9 los errores de negocio que actualmente terminan respondiendo con HTTP `403`.
* [ ] Aplicar validaciones completas de entrada y respuestas de error consistentes.
* [ ] Adaptar la estructura de seguridad y servicios al patrón enseñado en el bootcamp durante la Fase 10.
* [ ] Mantener el README y los diagramas sincronizados con las rutas y clases realmente implementadas.
* [ ] No documentar MCP ni release final como terminados hasta validarlos mediante pruebas reales.
