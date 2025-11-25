export interface Cliente {
  id?: number;
  nombre: string;
  cif?: string;
  direccion?: string;
  ciudad?: string;
  codigoPostal?: string;
  provincia?: string;
  telefono?: string;
  email?: string;
  activo: boolean;
  fechaCreacion?: string;
  fechaModificacion?: string;
}

