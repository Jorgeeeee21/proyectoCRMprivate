import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const appRoutes: Routes = [
  // Ruta principal que redirige al Dashboard (vista principal del CRM)
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  
  // Módulo de Autenticación (se cargará de forma perezosa)
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login.component').then(m => m.LoginComponent)
  },

  // RUTAS PROTEGIDAS (CRM)
  {
    path: '',
    canActivate: [authGuard],
    children: [
      // 1. DASHBOARD y ESTADÍSTICAS
      {
        path: 'dashboard',
        loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent),
        title: 'CRM | Dashboard'
      },
      // 2. GESTIÓN DE CLIENTES (Módulo funcional 1)
      {
        path: 'clientes',
        loadComponent: () => import('./features/clientes/cliente-list.component').then(m => m.ClienteListComponent),
        title: 'CRM | Clientes'
      },
      // 3. GESTIÓN DE TAREAS COMERCIALES (Módulo funcional 3)
      {
        path: 'tareas',
        loadComponent: () => import('./features/tareas/tarea-list.component').then(m => m.TareaListComponent),
        title: 'CRM | Tareas'
      },
      // 4. GESTIÓN DE CONTACTOS E INCIDENCIAS (Módulo funcional 2)
      {
        path: 'contactos-incidencias',
        loadComponent: () => import('./features/contactos/contacto-list.component').then(m => m.ContactoListComponent),
        title: 'CRM | Contactos'
      },
    ]
  },

  // Cualquier otra ruta no encontrada
  { path: '**', redirectTo: 'dashboard' }
];