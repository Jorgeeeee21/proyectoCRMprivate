import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';

export interface Estadisticas {
  clientesActivos: number;
  totalClientes: number;
  incidenciasPorEstado: { [key: string]: number };
  totalIncidencias: number;
  tareasPorEstado: { [key: string]: number };
  totalTareas: number;
  tareasCompletadas: number;
}

@Injectable({
  providedIn: 'root'
})
export class EstadisticasService extends ApiService {
  
  getEstadisticas(): Observable<Estadisticas> {
    return this.http.get<Estadisticas>(this.getUrl('estadisticas'));
  }
}

