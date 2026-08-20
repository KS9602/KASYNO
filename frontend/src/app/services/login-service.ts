import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, tap, throwError  } from 'rxjs';
import { LoginRequest } from '../models/login-request'

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  private http = inject(HttpClient);
  login(username: string, password: string): Observable<void>{
      const body: LoginRequest = {
      username: username,
      password: password,
    };
    return this.http.post<void>('/api/auth/login', body)
      .pipe(
        tap(r => console.log("Create user response: ", r)),
        catchError(e => {console.error("Create user Errror: ", e)
          return throwError(() => e);
        })
      )
  }
}