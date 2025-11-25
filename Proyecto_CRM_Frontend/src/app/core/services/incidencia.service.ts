import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Incidencia } from '../models/incidencia.model';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class IncidenciaService extends ApiService {
  
  getIncidencias(): Observable<Incidencia[]> {
    return this.http.get<Incidencia[]>(this.getUrl('incidencias'));
  }
  
  getIncidenciaById(id: number): Observable<Incidencia> {
    return this.http.get<Incidencia>(`${this.getUrl('incidencias')}/${id}`);
  }
  
  getIncidenciasByCliente(clienteId: number): Observable<Incidencia[]> {
    return this.http.get<Incidencia[]>(`${this.getUrl('incidencias')}/cliente/${clienteId}`);
  }
  
  getIncidenciasByEstado(estado: string): Observable<Incidencia[]> {
    return this.http.get<Incidencia[]>(`${this.getUrl('incidencias')}/estado/${estado}`);
  }
  
  createIncidencia(incidencia: Incidencia): Observable<Incidencia> {
    return this.http.post<Incidencia>(this.getUrl('incidencias'), incidencia);
  }
  
  updateIncidencia(incidencia: Incidencia): Observable<Incidencia> {
    return this.http.put<Incidencia>(`${this.getUrl('incidencias')}/${incidencia.id}`, incidencia);
  }
  
  deleteIncidencia(id: number): Observable<void> {
    return this.http.delete<void>(`${this.getUrl('incidencias')}/${id}`);
  }
}
