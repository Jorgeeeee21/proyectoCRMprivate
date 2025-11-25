import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class ApiService {
  protected http = inject(HttpClient);
  
  // URL base del Backend (asumiendo que se ejecutarán en el puerto 8080)
  protected baseUrl = 'http://localhost:8080/api';
  
  // Función de utilidad para construir la URL completa del recurso
  protected getUrl(resource: string): string {
    return `${this.baseUrl}/${resource}`;
  }
}