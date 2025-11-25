import { HttpInterceptorFn, HttpRequest, HttpHandlerFn, HttpEvent } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

/**
 * HttpInterceptorFn que añade el token JWT de autenticación a todas las peticiones salientes.
 * Esto es necesario para que Spring Security valide las solicitudes al Backend.
 */
export const authInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>, 
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {
  
  // Inyección del servicio de autenticación
  const authService = inject(AuthService);
  
  // Usamos la Signal del token para obtener su valor actual
  const authToken = authService.authToken();
  
  if (authToken) {
    // Si existe el token, clonamos la petición y le añadimos la cabecera 'Authorization'
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${authToken}`
      }
    });
    console.log('Interceptor: Añadiendo token JWT a la petición:', authReq.url);
    return next(authReq);
  }
  
  // Si no hay token, la petición pasa sin modificar
  return next(req);
};