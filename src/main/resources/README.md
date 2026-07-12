# 🏛️ CoE Banco TEST - Backend Transaccional de Autorizaciones

Este repositorio contiene el microservicio principal y transaccional encargado de gestionar el ciclo de vida de las solicitudes de autorización interna del banco. Diseñado bajo una **Arquitectura Limpia** y aplicando el patrón de diseño táctico **Transactional Outbox** para garantizar consistencia eventual robusta tolerante a fallos de red.

## Stack Tecnológico
* **Java 25** & **Spring Boot 3.x**
* **Spring Data JPA** & **Hibernate** (Persistencia Relacional)
* **PostgreSQL** (Motor de Base de Datos Transaccional)
* **AWS SDK v2 para Java** (SnsClient asíncrono)
* **Lombok** (Optimización de código repetitivo)

## Patrón Arquitectónico: Transactional Outbox
Para mitigar el riesgo de inconsistencia (que los datos se guarden en la base de datos pero el evento de notificación se pierda por una caída de red con la nube), implementamos el patrón Outbox:
1. **Escritura Atómica:** El controlador recibe la petición y bajo una sola transacción física (`@Transactional`), guarda el registro en la tabla de negocios y un JSON con el evento en la `tabla_outbox`.
2. **Poller de Fondo (Scheduler):** Un hilo asíncrono (`OutboxProcessor`) despierta cada 5 segundos de forma automática, lee los eventos `PENDIENTE`, los eyecta hacia el tema de **AWS SNS** en LocalStack y los marca como `PROCESADO`.

## Configuración del Entorno de Desarrollo Local
Asegúrese de tener configuradas las siguientes variables de entorno o propiedades en su `application.properties`:
```properties
server.port=8081
server.servlet.context-path=/api

spring.datasource.url=jdbc:postgresql://localhost:5432/reto_coe_db
spring.jpa.hibernate.ddl-auto=update

# Infraestructura Cloud Simulada con LocalStack
reto.aws.sns-topic-arn=arn:aws:sns:us-east-1:000000000000:tema-solicitudes-aprobacion
```
