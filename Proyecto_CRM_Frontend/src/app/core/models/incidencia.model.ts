import { Cliente } from './cliente.model';
import { Contacto } from './contacto.model';
import { Usuario } from './user.model';

export interface Incidencia {
  id?: number;
  titulo: string;
  descripcion?: string;
  estado: EstadoIncidencia;
  prioridad: PrioridadIncidencia;
  fechaCreacion?: string;
  fechaResolucion?: string;
  cliente?: Cliente;
  clienteId?: number;
  contacto?: Contacto;
  contactoId?: number;
  usuarioAsignado?: Usuario;
  usuarioAsignadoId?: number;
}

export enum EstadoIncidencia {
  ABIERTA = 'ABIERTA',
  EN_PROCESO = 'EN_PROCESO',
  RESUELTA = 'RESUELTA',
  CERRADA = 'CERRADA'
}

export enum PrioridadIncidencia {
  BAJA = 'BAJA',
  MEDIA = 'MEDIA',
  ALTA = 'ALTA',
  URGENTE = 'URGENTE'
}

