import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente } from '../models/cliente.model';

@Injectable({
  // providedIn: 'root' asegura que el servicio es un Singleton global,
  // accesible en toda la aplicación (buena práctica para servicios de API).
  providedIn: 'root'
})
export class ClienteService {
  
  // Usamos 'inject' para obtener el HttpClient (patrón moderno de Angular)
  private http = inject(HttpClient);
  
  // URL base para el Backend de Spring Boot (se debe cambiar a la URL real)
  // Por ahora, usamos un placeholder. En producción sería 'http://tu-servidor-spring/api/clientes'
  private apiUrl = 'http://localhost:8080/api/clientes';

  // --- Métodos CRUD ---

  /**
   * Obtiene la lista completa de clientes.
   * @returns Un Observable que emite un array de Cliente.
   */
  getClientes(): Observable<Cliente[]> {
    console.log(`Petición GET: ${this.apiUrl}`);
    // HttpClient devuelve automáticamente un Observable
    return this.http.get<Cliente[]>(this.apiUrl);
  }

  /**
   * Obtiene un cliente por su ID.
   * @param id El ID del cliente a obtener.
   * @returns Un Observable que emite el objeto Cliente.
   */
  getClienteById(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.apiUrl}/${id}`);
  }

  /**
   * Crea un nuevo cliente en el Backend.
   * @param cliente El objeto Cliente sin la ID (Spring la asignará).
   * @returns Un Observable que emite el cliente creado (con la ID asignada).
   */
  createCliente(cliente: Cliente): Observable<Cliente> {
    console.log(`Petición POST a ${this.apiUrl}`, cliente);
    return this.http.post<Cliente>(this.apiUrl, cliente);
  }

  /**
   * Actualiza un cliente existente.
   * @param cliente El objeto Cliente con la ID.
   * @returns Un Observable que emite el cliente actualizado.
   */
  updateCliente(cliente: Cliente): Observable<Cliente> {
    console.log(`Petición PUT a ${this.apiUrl}/${cliente.id}`, cliente);
    return this.http.put<Cliente>(`${this.apiUrl}/${cliente.id}`, cliente);
  }

  /**
   * Elimina un cliente por su ID.
   * @param id El ID del cliente a eliminar.
   * @returns Un Observable vacío (void).
   */
  deleteCliente(id: number): Observable<void> {
    console.log(`Petición DELETE: ${this.apiUrl}/${id}`);
    // Nota: El método delete de HttpClient tiene un body opcional, pero se usa más comúnmente sin él.
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}