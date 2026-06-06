# Seguimiento del Proyecto - SmartQR API

## Estado actual

**Rama actual:** `release/v1.0.0`

La API ya está desarrollada y probada por perfiles. En esta rama estoy cerrando la entrega final: README, roadmap, diagramas, presentación, revisión de archivos y última prueba antes de integrar la versión final en `develop` y `main`.

---

## Fase 1: Inicio del proyecto - `setup/init-project`

**Qué hice:** preparar la base del proyecto para poder trabajar de forma ordenada desde el principio.

* [x] Crear el proyecto Spring Boot y configurar Maven.
* [x] Preparar la conexión con MySQL.
* [x] Crear la estructura principal de paquetes.
* [x] Crear las primeras entidades, enums y repositorios.
* [x] Preparar los primeros documentos y archivos `.http`.

---

## Fase 2: Registro y login - `feature/auth-security`

**Qué hice:** permitir que un usuario pueda registrarse e iniciar sesión para recibir un token.

* [x] Crear los DTOs de registro y login.
* [x] Crear el controlador y el servicio de autenticación.
* [x] Configurar JWT con Spring Security.
* [x] Empezar a proteger rutas según el usuario autenticado.

---

## Fase 3: Carta de productos - `feature/catalog-management`

**Qué hice:** crear la parte de productos y la carta que consulta el cliente.

* [x] Crear el CRUD de productos para administración.
* [x] Crear la consulta pública del menú disponible.
* [x] Añadir las peticiones HTTP para comprobar el catálogo.

---

## Fase 4: Pedidos y cocina - `feature/order-processing`

**Qué hice:** convertir la carta en un flujo real de pedidos.

* [x] Permitir que el cliente cree un pedido.
* [x] Calcular importes en backend usando productos reales.
* [x] Consultar pedidos desde el personal interno.
* [x] Añadir estados del pedido.
* [x] Añadir la cola de cocina.
* [x] Añadir la generación de QR de mesa.

---

## Fase 5: Pagos y ticket - `feature/payments-billing`

**Qué hice:** cerrar el flujo del pedido con dos formas de pago.

* [x] Crear pago con tarjeta simulado.
* [x] Crear pago en efectivo pendiente de confirmación.
* [x] Permitir que empleado o administrador confirme el pago en efectivo.
* [x] Generar el ticket de un pedido pagado.
* [x] Comprobar errores como efectivo insuficiente o ticket sin pago.

---

## Arreglo durante pruebas - `bugfix/auth-login-flow`

**Problema que encontré:** podía registrar usuarios, pero después el login no funcionaba correctamente.

* [x] Revisar el error de login.
* [x] Crear `CustomUserDetailsService` para cargar usuarios desde la base de datos.
* [x] Validar que el login devuelve JWT.
* [x] Comprobar que el token permite acceder a rutas protegidas.
* [x] Integrar el arreglo en `develop`.

---

## Arreglo durante pruebas - `bugfix/product-seed-data`

**Problema que encontré:** necesitaba productos iniciales fijos para poder repetir bien la demo de pedidos y pagos.

* [x] Añadir productos iniciales en `data.sql`.
* [x] Evitar datos duplicados.
* [x] Comprobar que el menú se carga al reiniciar.
* [x] Integrar el arreglo en `develop`.

---

## Permisos por roles - `feature/security-rbac`

**Qué hice:** separar lo que puede hacer cada tipo de usuario.

* [x] Usar los roles `CUSTOMER`, `EMPLOYEE` y `ADMIN`.
* [x] Mantener el menú público.
* [x] Permitir QR y gestión operativa a personal interno.
* [x] Reservar el CRUD de productos y el dashboard al administrador.
* [x] Crear pruebas HTTP separadas por rol.

---

## Protección de pedidos del cliente - `bugfix/customer-order-ownership`

**Problema que encontré:** un cliente no debe poder usar el identificador de un pedido de otro cliente.

* [x] Evitar que un cliente consulte pedidos ajenos.
* [x] Evitar que pague pedidos ajenos.
* [x] Evitar que descargue tickets ajenos.
* [x] Evitar que deje feedback sobre pedidos ajenos.
* [x] Comprobar el caso con dos clientes diferentes.

---

## Fase 6: IA para el cliente - `feature/ai-integration`

**Qué hice:** integrar OpenAI en dos partes del flujo del cliente.

* [x] Configurar Spring AI con OpenAI.
* [x] Analizar el sentimiento cuando se guarda un feedback.
* [x] Crear recomendaciones de combos usando productos disponibles.
* [x] Probar ambas peticiones desde el archivo del customer.

**Nota personal:** OpenAI queda para funciones relacionadas con texto libre del cliente: valorar una opinión y generar una recomendación de compra.

---

## Fase 7: Dashboard administrativo - `feature/analytics-dashboard`

**Qué hice:** crear información útil para administración.

* [x] Calcular métricas de ingresos, pedidos y productos vendidos.
* [x] Crear el endpoint de métricas.
* [x] Generar gráficos PNG con JFreeChart.
* [x] Ajustar el gráfico de productos para mostrar unidades enteras.
* [x] Ignorar los PNG generados localmente durante las pruebas.

---

## Fase 8: Agente local y MCP - `feature/mcp-local-agent`

**Qué hice:** añadir un segundo uso de IA orientado a administración.

* [x] Configurar Ollama con el modelo local `qwen3:4b`.
* [x] Crear un agente local accesible solamente para `ADMIN`.
* [x] Crear herramientas de consulta para métricas, cocina y menú.
* [x] Publicar las herramientas mediante MCP.
* [x] Probar `tools/list` y `tools/call`.

**Nota personal:** en el proyecto hay dos `ChatClient`. OpenAI se mantiene como cliente principal para las funciones del customer, y Ollama se selecciona con `@Qualifier` en el agente local para las consultas internas de administración.

---

## Fase 9: Errores y validaciones - `feature/exception-validation`

**Qué hice:** conseguir que la API responda con errores más claros y controlados.

* [x] Crear excepciones propias.
* [x] Crear `GlobalExceptionHandler`.
* [x] Crear una respuesta de error común.
* [x] Validar datos de entrada con anotaciones.
* [x] Corregir el filtro JWT para no convertir errores de negocio en un `403`.
* [x] Probar errores `400`, `401`, `403`, `404` y `409`.

**Nota personal:** un `403` debe aparecer cuando no hay permisos, no cuando falta un recurso o una regla de negocio no se cumple.

---

## Fase 10: Estadísticas de feedback - `feature/feedback-analytics`

**Qué hice:** completar la parte del administrador relacionada con las opiniones.

* [x] Crear la respuesta de estadísticas.
* [x] Calcular número de feedbacks.
* [x] Calcular media de valoración.
* [x] Calcular distribución de sentimientos.
* [x] Crear `GET /feedback/statistics` para `ADMIN`.
* [x] Probar el flujo customer envía feedback y admin consulta resultados.

**Nota personal:** OpenAI analiza la opinión cuando el cliente la envía. Después, las estadísticas se calculan leyendo los datos guardados, sin volver a llamar a la IA.

---

## Fase 11: Revisión final del backend - `feature/final-refactor-review`

**Qué hice:** dejar la seguridad y los requests preparados para una demo que pueda explicar paso a paso.

* [x] Separar `PasswordEncoder` en `security/EncoderConfig`.
* [x] Utilizar `PasswordEncoderFactories.createDelegatingPasswordEncoder()`.
* [x] Sacar la clave JWT de los filtros y configurarla mediante `JWT_SECRET`.
* [x] Dejar los tokens visibles en la cabecera de los archivos `.http`.
* [x] Limpiar `OrderItemRequest` para que el cliente no envíe nombre ni subtotal.
* [x] Ejecutar el flujo completo con customer, employee y admin.
* [x] Comprobar OpenAI, gráficos, feedback statistics, Ollama y MCP.

---
## Fase 12: Agente administrativo OpenAI con memoria JDBC - `feature/openai-admin-chat-memory`

**Qué estoy haciendo ahora:** añadir una última mejora de IA antes de cerrar la release `v1.0.0`.

* [ ] 12.1: Crear rama `feature/openai-admin-chat-memory` desde el punto estable de `release/v1.0.0`.
* [ ] 12.2: Añadir dependencia/configuración necesaria para memoria JDBC con Spring AI.
* [ ] 12.3: Crear un endpoint administrativo para preguntar a OpenAI con `conversationId`.
* [ ] 12.4: Guardar la memoria conversacional con `JdbcChatMemoryRepository`.
* [ ] 12.5: Mantener OpenAI de feedback y combos como está, sin mezclarlo con este chat.
* [ ] 12.6: Mantener Ollama/MCP como agente local y herramientas de solo lectura.
* [ ] 12.7: Añadir requests de prueba en `ADMIN_REQUESTS.http` y `DEMO_REQUESTS.http`.
* [ ] 12.8: Documentar la nueva funcionalidad en README.
* [ ] 12.9: Comprobar `./mvnw clean package`.
* [ ] 12.10: Probar el chat con dos preguntas usando el mismo `conversationId`.

---

## Fase 12: Entrega final - `release/v1.0.0`

## Fase 13: Entrega final - `release/v1.0.0`

**Qué haré al final:** cerrar la versión definitiva y subir la entrega.

* [x] 13.1: Preparar el README con la explicación de la API, configuración, endpoints y forma de ejecutar la demo.
* [x] 13.2: Preparar el roadmap final con las funcionalidades realmente terminadas.
* [x] 13.3: Actualizar los diagramas para que coincidan con el proyecto final.
* [x] 13.4: Añadir el Class Diagram y revisar los enlaces de documentación visual.
* [x] 13.5: Revisar el código archivo por archivo y retirar comentarios o restos que no quiera entregar.
* [x] 13.6: Comprobar que no se suben tokens, claves, QR o gráficos generados durante las pruebas.
* [x] 13.7: Preparar el guion de presentación y exportar las diapositivas en PDF/PPTX.
* [x] 13.8: Ejecutar la comprobación de compilación y el flujo de demo desde base limpia.
* [ ] 13.9: Revisar que la mejora de memoria OpenAI está documentada y probada.
* [ ] 13.10: Hacer commit de la release, integrar en `develop` y `main`, crear el tag `v1.0.0` y subir la entrega.
---

# Evidencias que ya tengo comprobadas

* [x] Login correcto para `CUSTOMER`, `EMPLOYEE` y `ADMIN`.
* [x] Permisos separados por rol.
* [x] Un customer no puede trabajar con pedidos de otro customer.
* [x] Flujo de pedido completo.
* [x] Pago con tarjeta simulado.
* [x] Pago en efectivo pendiente y confirmación por personal interno.
* [x] Ticket de pedido pagado.
* [x] Dashboard de administración.
* [x] Gráficos PNG generados con JFreeChart.
* [x] Feedback analizado por OpenAI.
* [x] Recomendación de combo con OpenAI.
* [x] Estadísticas de feedback para administración.
* [x] Agente local con Ollama.
* [x] Herramientas MCP de solo lectura.
* [x] Respuestas de error y validaciones principales.
* [x] Flujo completo por perfiles ejecutado desde los requests finales.

---

# Idea que dejo para una versión futura

Al principio pensé en mostrar al cliente una gráfica con el progreso de su pedido y una estimación de espera. Para esta entrega he preferido dejar una versión más estable y segura: el cliente consulta el estado real de su propio pedido y la cola general queda reservada para empleado y administrador.

Esta mejora podría hacerse en una siguiente versión añadiendo una regla de cálculo de espera, nuevos DTOs, un endpoint específico y pruebas de seguridad para que el cliente siga viendo solo información de su propio pedido.

También quedan como posibles mejoras futuras:

* Crear un frontend para que el flujo se pueda usar de forma visual.
* Ampliar el dashboard con más filtros e histórico de métricas.
* Añadir más tests para cubrir mejor los casos principales.
* Mejorar la experiencia del administrador con más consultas internas de solo lectura.
