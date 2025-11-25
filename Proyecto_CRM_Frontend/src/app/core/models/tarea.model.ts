import { Cliente } from './cliente.model';
import { Usuario } from './user.model';

export interface Tarea {
  id?: number;
  titulo: string;
  descripcion?: string;
  estado: EstadoTarea;
  prioridad: PrioridadTarea;
  fechaCreacion?: string;
  fechaVencimiento?: string;
  fechaCompletada?: string;
  usuarioAsignado?: Usuario;
  usuarioAsignadoId: number;
  usuarioCreador?: Usuario;
  usuarioCreadorId: number;
  cliente?: Cliente;
  clienteId?: number;
}

export enum EstadoTarea {
  PENDIENTE = 'PENDIENTE',
  EN_PROCESO = 'EN_PROCESO',
  COMPLETADA = 'COMPLETADA',
  CANCELADA = 'CANCELADA'
}

export enum PrioridadTarea {
  BAJA = 'BAJA',
  MEDIA = 'MEDIA',
  ALTA = 'ALTA',
  URGENTE = 'URGENTE'
}

