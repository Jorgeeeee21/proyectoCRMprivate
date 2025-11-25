# CRM Backend - Spring Boot

Backend del sistema CRM desarrollado con Spring Boot 3.3.0 y Java 21.

## Requisitos

- Java 21
- Maven 3.6+
- MySQL 8.0+

## Configuración

1. Editar `src/main/resources/application.properties` con tus credenciales de MySQL:
```properties
spring.datasource.username=root
spring.datasource.password=tu_password
```

2. Compilar:
```bash
mvn clean install
```

3. Ejecutar:
```bash
mvn spring-boot:run
```

## Usuario por defecto

- Username: `admin`
- Password: `admin123`

## Características

- ✅ Autenticación JWT
- ✅ CRUD completo de todas las entidades
- ✅ Tareas programadas con @Scheduled
- ✅ Procesamiento asíncrono con @Async
- ✅ Spring Security configurado
- ✅ CORS habilitado para Angular

## Estructura

- `controller/` - Controladores REST
- `service/` - Lógica de negocio
- `repository/` - Repositorios JPA
- `model/entity/` - Entidades JPA
- `model/dto/` - DTOs para transferencia de datos
- `security/` - Configuración de seguridad y JWT
- `config/` - Configuraciones (Async, DataInit)

