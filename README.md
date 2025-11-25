# Proyecto CRM Empresarial Full Stack

Sistema CRM (Customer Relationship Management) desarrollado con arquitectura multicapa moderna.

## 🏗️ Arquitectura del Proyecto

- **Frontend**: Angular 20 con TypeScript, RxJS y Angular Material
- **Backend**: Spring Boot 3.3.0 (Java 21) con Spring Security y JWT
- **Base de Datos**: MySQL
- **Concurrencia**: Servicios programados con ExecutorService y tareas asíncronas

## 📁 Estructura del Proyecto

```
proyecto_CRM/
├── Proyecto_CRM_Backend/          # Backend Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/crm/
│   │   │   │   ├── config/        # Configuraciones (Security, Async, DataInit)
│   │   │   │   ├── controller/    # Controladores REST
│   │   │   │   ├── model/         # Entidades y DTOs
│   │   │   │   ├── repository/    # Repositorios JPA
│   │   │   │   ├── security/      # JWT y Security
│   │   │   │   └── service/       # Servicios de negocio
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── database/schema.sql
│   │   └── pom.xml
│
└── Proyecto_CRM_Frontend/         # Frontend Angular
    ├── src/
    │   └── app/
    │       ├── core/              # Servicios, modelos, guards, interceptors
    │       ├── features/          # Módulos funcionales
    │       │   ├── auth/          # Autenticación
    │       │   ├── clientes/     # Gestión de clientes
    │       │   ├── contactos/    # Contactos e incidencias
    │       │   ├── dashboard/    # Dashboard y estadísticas
    │       │   └── tareas/       # Tareas comerciales
    │       └── shared/            # Componentes compartidos
    └── package.json
```

## 🚀 Requisitos Previos

- **Java 21** o superior
- **Node.js 18+** y npm
- **MySQL 8.0+** o MariaDB
- **Maven 3.6+**
- **Angular CLI 20+**

## 📦 Instalación

### 1. Base de Datos

1. Instalar MySQL y crear la base de datos:
```sql
CREATE DATABASE crm_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Ejecutar el script SQL (opcional, JPA creará las tablas automáticamente):
```bash
mysql -u root -p crm_db < Proyecto_CRM_Backend/src/main/resources/database/schema.sql
```

### 2. Backend (Spring Boot)

1. Navegar al directorio del backend:
```bash
cd Proyecto_CRM_Backend
```

2. Configurar la base de datos en `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/crm_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=tu_password
```

3. Compilar y ejecutar:
```bash
mvn clean install
mvn spring-boot:run
```

El backend estará disponible en `http://localhost:8080`

**Usuario por defecto:**
- Username: `admin`
- Password: `admin123`

### 3. Frontend (Angular)

1. Navegar al directorio del frontend:
```bash
cd Proyecto_CRM_Frontend
```

2. Instalar dependencias:
```bash
npm install
```

3. Instalar Angular Material (si no se instaló automáticamente):
```bash
ng add @angular/material
```

4. Ejecutar el servidor de desarrollo:
```bash
ng serve
```

El frontend estará disponible en `http://localhost:4200`

## 🔐 Autenticación

El sistema utiliza JWT (JSON Web Tokens) para la autenticación:

1. Login en `/login` con username y password
2. El backend devuelve un token JWT
3. El token se almacena en localStorage
4. Todas las peticiones incluyen el token en el header `Authorization: Bearer <token>`

## 📋 Módulos Funcionales

### 1. Gestión de Clientes
- CRUD completo de clientes
- Búsqueda y filtrado
- Gestión de estado activo/inactivo

### 2. Gestión de Contactos e Incidencias
- Relación cliente-contacto-incidencia (1:N)
- Estados de incidencias: ABIERTA, EN_PROCESO, RESUELTA, CERRADA
- Prioridades: BAJA, MEDIA, ALTA, URGENTE
- Notificaciones automáticas por cambios de estado

### 3. Gestión de Tareas Comerciales
- Creación y asignación de tareas
- Control de estado, fechas y prioridad
- Recordatorios automáticos de tareas próximas a vencer

### 4. Dashboard y Estadísticas
- Métricas en tiempo real:
  - Número de clientes activos
  - Incidencias por estado
  - Tareas completadas
  - Gráficos interactivos

### 5. Gestión de Usuarios y Roles
- Sistema de autenticación JWT
- Roles: ADMIN, COMERCIAL, GESTOR
- Autorización basada en roles

## ⚙️ Características Técnicas

### Backend

- **Concurrencia y Paralelismo:**
  - `@Scheduled` para tareas programadas
  - `@Async` para procesamiento asíncrono
  - `ThreadPoolTaskExecutor` para gestión de hilos
  - Tareas programadas:
    - Recordatorios diarios de tareas (9:00 AM)
    - Actualización de incidencias antiguas (cada hora)
    - Resumen diario de actividades (medianoche)

- **Seguridad:**
  - Spring Security con JWT
  - CORS configurado para Angular
  - Encriptación de contraseñas con BCrypt

- **API REST:**
  - Endpoints documentados
  - Validación de datos
  - Manejo de errores

### Frontend

- **Arquitectura:**
  - Componentes modulares
  - Servicios inyectables
  - Guards para protección de rutas
  - Interceptors HTTP para JWT

- **Asincronía:**
  - Observables RxJS
  - Async/await
  - Signals para estado reactivo

- **UI/UX:**
  - Angular Material para componentes
  - Diseño responsivo
  - Gráficos con Chart.js

## 🔌 Endpoints API

### Autenticación
- `POST /api/auth/login` - Login de usuario

### Clientes
- `GET /api/clientes` - Listar clientes (con filtro opcional)
- `GET /api/clientes/{id}` - Obtener cliente
- `POST /api/clientes` - Crear cliente
- `PUT /api/clientes/{id}` - Actualizar cliente
- `DELETE /api/clientes/{id}` - Eliminar cliente (soft delete)

### Contactos
- `GET /api/contactos` - Listar contactos
- `GET /api/contactos/cliente/{clienteId}` - Contactos por cliente
- `POST /api/contactos` - Crear contacto
- `PUT /api/contactos/{id}` - Actualizar contacto
- `DELETE /api/contactos/{id}` - Eliminar contacto

### Incidencias
- `GET /api/incidencias` - Listar incidencias
- `GET /api/incidencias/cliente/{clienteId}` - Incidencias por cliente
- `GET /api/incidencias/estado/{estado}` - Incidencias por estado
- `POST /api/incidencias` - Crear incidencia
- `PUT /api/incidencias/{id}` - Actualizar incidencia
- `DELETE /api/incidencias/{id}` - Eliminar incidencia

### Tareas
- `GET /api/tareas` - Listar tareas
- `GET /api/tareas/usuario/{usuarioId}` - Tareas por usuario
- `GET /api/tareas/estado/{estado}` - Tareas por estado
- `POST /api/tareas` - Crear tarea
- `PUT /api/tareas/{id}` - Actualizar tarea
- `DELETE /api/tareas/{id}` - Eliminar tarea

### Estadísticas
- `GET /api/estadisticas` - Obtener estadísticas generales

## 🧪 Testing

### Backend
```bash
cd Proyecto_CRM_Backend
mvn test
```

### Frontend
```bash
cd Proyecto_CRM_Frontend
ng test
```

## 📝 Notas de Desarrollo

- El backend crea automáticamente las tablas al iniciar (JPA `ddl-auto=update`)
- Los roles y usuario admin se crean automáticamente al iniciar la aplicación
- Las tareas programadas se ejecutan en segundo plano
- El frontend usa Signals para gestión reactiva del estado

## 🐛 Solución de Problemas

### Error de conexión a la base de datos
- Verificar que MySQL esté ejecutándose
- Comprobar credenciales en `application.properties`
- Verificar que la base de datos `crm_db` exista

### Error CORS en el frontend
- Verificar que el backend esté ejecutándose en el puerto 8080
- Comprobar la configuración de CORS en `SecurityConfig.java`

### Error de autenticación
- Verificar que el token JWT esté siendo enviado en las peticiones
- Comprobar que el interceptor HTTP esté configurado correctamente

## 📚 Tecnologías Utilizadas

- **Backend:**
  - Spring Boot 3.3.0
  - Spring Security
  - Spring Data JPA
  - JWT (jjwt 0.12.3)
  - MySQL Connector
  - Lombok

- **Frontend:**
  - Angular 20
  - Angular Material
  - RxJS
  - Chart.js / ng2-charts
  - TypeScript 5.9

## 👥 Autores

Desarrollado como proyecto académico para el módulo de Desarrollo de Aplicaciones Multiplataforma (DAM).

## 📄 Licencia

Este proyecto es de uso educativo.

