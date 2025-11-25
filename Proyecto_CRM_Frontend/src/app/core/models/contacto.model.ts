import { Cliente } from './cliente.model';

export interface Contacto {
  id?: number;
  nombre: string;
  apellidos?: string;
  cargo?: string;
  telefono?: string;
  email?: string;
  activo: boolean;
  fechaCreacion?: string;
  cliente?: Cliente;
  clienteId?: number;
}

