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
      console.log("QQQQQQQQQQQ")
      const body: CreateUserRequest = {
      username: username,
      password: password,
      email: email
    };
          console.log("zzzzzzzz")
    return this.http.post<CreateUserResponse>('/api/auth/register', body)
      .pipe(
        tap(r => console.log("Create user response: ", r)),
        catchError(e => {console.error("Create user Errror: ", e)
          return throwError(() => e);
        })
      )
  }
}