import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Contacto } from '../models/contacto.model';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class ContactoService extends ApiService {
  
  getContactos(): Observable<Contacto[]> {
    return this.http.get<Contacto[]>(this.getUrl('contactos'));
  }
  
  getContactosByCliente(clienteId: number): Observable<Contacto[]> {
    return this.http.get<Contacto[]>(`${this.getUrl('contactos')}/cliente/${clienteId}`);
  }
  
  getContactoById(id: number): Observable<Contacto> {
    return this.http.get<Contacto>(`${this.getUrl('contactos')}/${id}`);
  }
  
  createContacto(contacto: Contacto): Observable<Contacto> {
    return this.http.post<Contacto>(this.getUrl('contactos'), contacto);
  }
  
  updateContacto(contacto: Contacto): Observable<Contacto> {
    return this.http.put<Contacto>(`${this.getUrl('contactos')}/${contacto.id}`, contacto);
  }
  
  deleteContacto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.getUrl('contactos')}/${id}`);
  }
}
