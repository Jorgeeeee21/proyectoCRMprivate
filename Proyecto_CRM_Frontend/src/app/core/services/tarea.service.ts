import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Tarea } from '../models/tarea.model';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class TareaService extends ApiService {
  
  getTareas(): Observable<Tarea[]> {
    return this.http.get<Tarea[]>(this.getUrl('tareas'));
  }
  
  getTareaById(id: number): Observable<Tarea> {
    return this.http.get<Tarea>(`${this.getUrl('tareas')}/${id}`);
  }
  
  getTareasByUsuario(usuarioId: number): Observable<Tarea[]> {
    return this.http.get<Tarea[]>(`${this.getUrl('tareas')}/usuario/${usuarioId}`);
  }
  
  getTareasByEstado(estado: string): Observable<Tarea[]> {
    return this.http.get<Tarea[]>(`${this.getUrl('tareas')}/estado/${estado}`);
  }
  
  createTarea(tarea: Tarea): Observable<Tarea> {
    return this.http.post<Tarea>(this.getUrl('tareas'), tarea);
  }
  
  updateTarea(tarea: Tarea): Observable<Tarea> {
    return this.http.put<Tarea>(`${this.getUrl('tareas')}/${tarea.id}`, tarea);
  }
  
  deleteTarea(id: number): Observable<void> {
    return this.http.delete<void>(`${this.getUrl('tareas')}/${id}`);
  }
}
