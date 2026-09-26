import { inject } from '@angular/core';
import {
  HttpErrorResponse,
  HttpInterceptorFn
} from '@angular/common/http';
import { Router } from '@angular/router';
import {
  BehaviorSubject,
  catchError,
  filter,
  switchMap,
  take,
  throwError
} from 'rxjs';

import { AuthService } from '../services/auth-service';

let refreshInProgress = false;

const refreshSubject = new BehaviorSubject<boolean | null>(null);

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const authService = inject(AuthService);
  const router = inject(Router);

  if (req.url.includes('/api/auth/refresh')) {
    return next(req);
  }

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {

      const tokenExpired =
        error.status === 401 &&
        error.headers.get('X-Auth-Error') === 'TOKEN_EXPIRED';

      if (!tokenExpired) {
        return throwError(() => error);
      }

      if (refreshInProgress) {

        return refreshSubject.pipe(
          filter(result => result !== null),
          take(1),

          switchMap(success => {

            if (success) {
              return next(req);
            }
            return throwError(() => error);
          })
        );
      }

      refreshInProgress = true;
      refreshSubject.next(null);

      return authService.refresh().pipe(

        switchMap(() => {

          refreshInProgress = false;
          refreshSubject.next(true);
          return next(req);
        }),


        catchError(refreshError => {

          refreshInProgress = false;
          refreshSubject.next(false);

          router.navigate(['/login']);

          return throwError(() => refreshError);
        })
      );
    })
  );
};