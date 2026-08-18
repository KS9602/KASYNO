import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, tap, throwError  } from 'rxjs';
import { CreateUserResponse } from '../models/create-user-response'
import { CreateUserRequest } from '../models/create-user-request'

@Injectable({
  providedIn: 'root',
})
export class RegisterService {
  private http = inject(HttpClient);
  register(username: string, password: string, email: string): Observable<CreateUserResponse>{
      const body: CreateUserRequest = {
      username: username,
      password: password,
      email: email
    };
    return this.http.post<CreateUserResponse>('http://localhost:7777/auth/register', body)
      .pipe(
        tap(r => console.log("Create user response: ", r)),
        catchError(e => {console.error("Create user Errror: ", e)
          return throwError(() => e);
        })
      )
  }
}