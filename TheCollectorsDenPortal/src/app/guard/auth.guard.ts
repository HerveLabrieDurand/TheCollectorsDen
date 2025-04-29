import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng/api';
import { of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ApiService } from '../services/api/api.service';
import { AuthService } from '../services/auth/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const apiService = inject(ApiService);
  const authService = inject(AuthService);
  const messageService = inject(MessageService);
  const translateService = inject(TranslateService);
  const router = inject(Router);
  const http = inject(HttpClient);

  const token = authService.getToken();

  if (!token) {
    authService.clearUser();
    router.navigate(['login']);
    messageService.add({
      severity: 'error',
      summary: translateService.instant(
        'errors.specific.guard.no.token.present.title',
      ),
      detail: translateService.instant(
        'errors.specific.guard.no.token.present.description',
      ),
    });
    return false;
  }

  return apiService.get('token/validation/validate-token').pipe(
    map(() => true), // Token is validated by backend, allow access
    catchError(() => {
      authService.clearUser();
      router.navigate(['login']);
      messageService.add({
        severity: 'error',
        summary: translateService.instant(
          'errors.specific.guard.invalid.token.title',
        ),
        detail: translateService.instant(
          'errors.specific.guard.invalid.token.description',
        ),
      });
      return of(false);
    }),
  );
};
