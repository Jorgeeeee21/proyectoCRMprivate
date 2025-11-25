import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Función Guard para proteger las rutas del CRM.
 * Permite la activación si el usuario está autenticado, sino redirige a /login.
 */
export const authGuard: CanActivateFn = (route, state) => {
  // Inyección del servicio de autenticación y el Router
  const authService = inject(AuthService);
  const router = inject(Router);

  // Usamos el estado reactivo (Signal) del servicio
  const isLoggedIn = authService.isLoggedIn();

  if (isLoggedIn) {
    // Si está autenticado, permite la navegación
    return true;
  } else {
    // Si NO está autenticado, redirige al componente de login
    console.log('Navegación bloqueada: Redirigiendo a Login');
    return router.createUrlTree(['/login']);
  }
};