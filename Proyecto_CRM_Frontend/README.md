# CRM Frontend - Angular 20

Frontend del sistema CRM desarrollado con Angular 20 y Angular Material.

## Requisitos

- Node.js 18+
- npm o yarn
- Angular CLI 20+

## Instalación

1. Instalar dependencias:
```bash
npm install
```

2. Instalar Angular Material (si es necesario):
```bash
ng add @angular/material
```

3. Ejecutar servidor de desarrollo:
```bash
ng serve
```

La aplicación estará disponible en `http://localhost:4200`

## Estructura

- `core/` - Servicios, modelos, guards, interceptors
- `features/` - Módulos funcionales (auth, clientes, tareas, etc.)
- `shared/` - Componentes compartidos

## Características

- ✅ Autenticación JWT
- ✅ Guards de rutas
- ✅ Interceptor HTTP para tokens
- ✅ Formularios reactivos
- ✅ Gráficos con Chart.js
- ✅ Diseño responsivo con Angular Material

## Build para producción

```bash
ng build --configuration production
```
