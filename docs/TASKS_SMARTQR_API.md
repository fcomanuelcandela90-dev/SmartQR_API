# 📋 Task Manager - SMARTQR Final Project (GitFlow)

## ✅ Fase 1: `setup/init-project`
- [x] 1.1: Inicializar Spring Boot, `pom.xml`, `application.properties` y carpetas.
- [x] 1.2: Crear Entidades con herencia (`Payment`), Enums y Repositorios JPA.
- [x] 1.3: Añadir diagramas de arquitectura, roadmap y plantillas HTTP.

## 🔲 Fase 2: `feature/auth-security`
- [x] 2.1: Implementar lógica JWT (`JwtAuthenticationFilter`, `JwtService`).
- [x] 2.2: Crear `AuthController`, `AuthenticationService` y DTOs de Auth.
- [x] 2.3: Configurar control de accesos en `SecurityConfig`.

## 🔲 Fase 3: `feature/catalog-management`
- [x] 3.1: Implementar CRUD de productos (`ProductController`, `ProductService`, DTOs).

## 🔲 Fase 4: `feature/order-processing`
- [ ] 4.1: Creación de comandas, generación de QR y subtotales.
- [ ] 4.2: Gestión de estados y colas de cocina.

## 🔲 Fase 5: `feature/payments-billing`
- [ ] 5.1: Pasarela de pagos (Card Gateway y validación Cash).
- [ ] 5.2: Endpoint de generación de tickets de pedido.

## 🔲 Fase 6: `feature/ai-integration`
- [ ] 6.1: Configurar Spring AI y OpenAI API Keys.
- [ ] 6.2: Implementar análisis de sentimiento de Feedback.
- [ ] 6.3: Generar combos recomendados automatizados.

## 🔲 Fase 7: `feature/analytics-dashboard`
- [ ] 7.1: Consultas SQL nativas para métricas de ingresos.
- [ ] 7.2: Generación de gráficos PNG en servidor con JFreeChart.

## 🔲 Fase 8: `feature/mcp-local-agent`
- [ ] 8.1: Integrar dependencias de Ollama (Agente local).
- [ ] 8.2: Implementar Agente Autónomo con `@Tool` y llamadas MCP.

## 🔲 Fase 9: `bugfix/final-adjustments`
- [ ] 9.1: Manejo global de excepciones y validaciones `@Valid`.

## 🔲 Fase 10: `release/v1.0`
- [ ] 10.1: Ocultar credenciales e higienizar propiedades.
- [ ] 10.2: Redactar README.md espectacular para la entrega.