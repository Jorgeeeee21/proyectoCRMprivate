import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { AuthResponse, Usuario } from '../models/user.model';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  private authUrl = 'http://localhost:8080/api/auth';
  
  private _currentUser = signal<Usuario | null>(this.loadUserFromStorage());
  private _authToken = signal<string | null>(this.loadTokenFromStorage());

  currentUser = this._currentUser.asReadonly();
  authToken = this._authToken.asReadonly();
  
  isLoggedIn = computed(() => !!this._authToken());
  
  isAdmin = computed(() => this._currentUser()?.roles?.includes('ADMIN') ?? false);
  isComercial = computed(() => this._currentUser()?.roles?.includes('COMERCIAL') ?? false);
  
  login(username: string, password: string): Observable<AuthResponse> {
    const payload = { username, password };
    
    return this.http.post<AuthResponse>(`${this.authUrl}/login`, payload).pipe(
      tap({
        next: (response) => {
          this.saveAuthData(response);
          this.router.navigateByUrl('/dashboard');
        },
        error: (err) => {
          console.error('Login failed:', err);
          this.logout();
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('current_user');
    
    this._authToken.set(null);
    this._currentUser.set(null);
    
    this.router.navigateByUrl('/login');
  }
  
  private saveAuthData(response: AuthResponse): void {
    localStorage.setItem('auth_token', response.token);
    
    const usuario: Usuario = {
      id: response.id,
      username: response.username,
      email: response.email,
      roles: response.roles
    };
    
    localStorage.setItem('current_user', JSON.stringify(usuario));
    
    this._authToken.set(response.token);
    this._currentUser.set(usuario);
  }

  private loadTokenFromStorage(): string | null {
    return localStorage.getItem('auth_token');
  }
  
  private loadUserFromStorage(): Usuario | null {
    const userJson = localStorage.getItem('current_user');
    return userJson ? JSON.parse(userJson) : null;
  }
  
  getToken(): string | null {
    return this._authToken();
  }
}
