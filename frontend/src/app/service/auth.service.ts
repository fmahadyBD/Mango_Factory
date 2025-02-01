import { isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { AuthResponse } from '../model/AuthResponse';
import { response } from 'express';

@Injectable({
  providedIn: 'root'
})
  export class AuthService {
    private baseUrl = 'http://localhost:8080'; // Your backend API URL
    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });
  
    private userRoleSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);
    public userRole$: Observable<string | null> = this.userRoleSubject.asObservable();
  
    constructor(@Inject(PLATFORM_ID) private platformId: object, private http: HttpClient, private router: Router) {
      if (this.isBrowser()) {
        const storedRole = localStorage.getItem('userRole');
        this.userRoleSubject.next(storedRole);
      }
    }
  
    login(email: string, password: string): Observable<AuthResponse> {
      return this.http.post<AuthResponse>(`${this.baseUrl}/login`, { email, password }, { headers: this.headers }).pipe(
        map((response: AuthResponse) => {
          if (this.isBrowser() && response.token) {
            localStorage.setItem('authToken', response.token);
            const decodedToken = this.decodeToken(response.token);
            localStorage.setItem('userRole', decodedToken.role);
            this.userRoleSubject.next(decodedToken.role);
          }
          return response;
        })
      );
    }
  
    register(user: { name: string; email: string; password: string; phone: string; address: string; dob: Date; gender: string; image: string; }, image: File): Observable<AuthResponse> {
      const formData = new FormData();
      formData.append('user', new Blob([JSON.stringify(user)], { type: 'application/json' }));
      formData.append('image', image);
  
      return this.http.post<AuthResponse>(`${this.baseUrl}/register`, formData).pipe(
        map((response: AuthResponse) => {
          if (this.isBrowser() && response.token) {
            localStorage.setItem('authToken', response.token);
          }
          return response;
        })
      );
    }
  
    private isBrowser(): boolean {
      return isPlatformBrowser(this.platformId);
    }
  
    decodeToken(token: string): any {
      const payload = token.split('.')[1];
      return JSON.parse(atob(payload));
    }
  
    getToken(): string | null {
      return this.isBrowser() ? localStorage.getItem('authToken') : null;
    }
  
    getUserRole(): string | null {
      return this.isBrowser() ? localStorage.getItem('userRole') : null;
    }
  
    isAdmin(): boolean {
      return this.getUserRole() === 'ADMIN';
    }
  
    isUser(): boolean {
      return this.getUserRole() === 'USER';
    }
  
    isManager(): boolean {
      return this.getUserRole() === 'MANAGER';
    }
  
    isSells(): boolean {
      return this.getUserRole() === 'SELLS';
    }
  
    isTokenExpired(token: string): boolean {
      const decodedToken = this.decodeToken(token);
      return Date.now() > decodedToken.exp * 1000;
    }
  
    isLoggedIn(): boolean {
      const token = this.getToken();
      if (token) {
        if (!this.isTokenExpired(token)) {
          return true;
        }
        this.logout();
      }
      return false;
    }
  
    logout(): void {
      if (this.isBrowser()) {
        localStorage.removeItem('authToken');
        localStorage.removeItem('userRole');
        this.userRoleSubject.next(null);
      }
      this.router.navigate(['login']);
    }
  
    hasRole(roles: string[]): boolean {
      return roles.includes(this.getUserRole() || '');
    }
  }
  