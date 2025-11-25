export interface Usuario {
  id?: number;
  username: string;
  email: string;
  nombre?: string;
  apellidos?: string;
  activo?: boolean;
  roles?: string[];
}

export interface AuthResponse {
  token: string;
  type: string;
  id: number;
  username: string;
  email: string;
  roles: string[];
}
