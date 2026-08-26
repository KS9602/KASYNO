import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private http = inject(HttpClient);

  isAuthenticated(): Observable<boolean> {
    return this.http.get('/api/auth/me').pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }

  refresh(): Observable<void> {
    return this.http.post<void>('/api/auth/refresh', {});
  }

  logout(): Observable<void> {
    return this.http.post<void>('/api/auth/logout', {});
  }
}