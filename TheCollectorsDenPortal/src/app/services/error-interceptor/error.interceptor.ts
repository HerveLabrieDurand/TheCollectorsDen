import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng/api';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { BACKEND_ERROR_CODES } from './errorCodes';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(
    private messageService: MessageService,
    private translate: TranslateService,
  ) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler,
  ): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error instanceof HttpErrorResponse) {
          console.log('error intercepted:', JSON.stringify(error)); // Debug log

          this.handleError(error);
        }
        return throwError(() => new Error(error.message));
      }),
    );
  }

  private handleBadRequests(error: HttpErrorResponse): void {
    switch (error.error.error) {
      case BACKEND_ERROR_CODES.EMAIL_NOT_REGISTERED:
        this.messageService.add({
          severity: 'warn',
          summary: this.translate.instant(
            'errors.specific.email.not.registered.title',
          ),
          detail: this.translate.instant(
            'errors.specific.email.not.registered.description',
          ),
        });
        break;
      case BACKEND_ERROR_CODES.EMAIL_ALREADY_CONFIRMED:
        this.messageService.add({
          severity: 'warn',
          summary: this.translate.instant(
            'errors.specific.email.already.confirmed.title',
          ),
          detail: this.translate.instant(
            'errors.specific.email.already.confirmed.description',
          ),
        });
        break;
      default:
        this.messageService.add({
          severity: 'error',
          summary: this.translate.instant('errors.generic.bad.request.error.title'),
          detail: this.translate.instant(
            'errors.generic.bad.request.error.description',
          ),
        });
        break;
    }
  }

  private handleUnauthorized(error: HttpErrorResponse): void {
    switch (error.error.error) {
      case BACKEND_ERROR_CODES.EMAIL_ALREADY_IN_USE:
        this.messageService.add({
          severity: 'error',
          summary: this.translate.instant(
            'errors.specific.email.already.in.use.error.title',
          ),
          detail: this.translate.instant(
            'errors.specific.email.already.in.use.error.description',
          ),
        });
        break;
      case BACKEND_ERROR_CODES.TOKEN_EXPIRED:
        this.messageService.add({
          severity: 'error',
          summary: this.translate.instant(
            'errors.specific.token.expired.title',
          ),
          detail: this.translate.instant(
            'errors.specific.token.expired.description',
          ),
          sticky: true,
        });
        break;
      case BACKEND_ERROR_CODES.TOKEN_INVALID:
        this.messageService.add({
          severity: 'error',
          summary: this.translate.instant(
            'errors.specific.token.invalid.title',
          ),
          detail: this.translate.instant(
            'errors.specific.token.invalid.description',
          ),
          sticky: true,
        });
        break;
      default:
        this.messageService.add({
          severity: 'error',
          summary: this.translate.instant('errors.generic.unauthorized.error.title'),
          detail: this.translate.instant(
            'errors.generic.unauthorized.error.description',
          ),
        });
        break;
    }
  }

  private handleError(error: HttpErrorResponse): void {
    switch (error.status) {
      case 400:
        this.handleBadRequests(error);
        break;
      case 401:
        this.handleUnauthorized(error);
        break;
      case 404:
        this.messageService.add({
          severity: 'error',
          summary: 'MUST_HANDLE_ERROR',
          detail: 'handle this new 404 error.',
        });
        break;
      case 500:
        this.messageService.add({
          severity: 'error',
          summary: this.translate.instant(
            'errors.generic.internal.server.error.title',
          ),
          detail: this.translate.instant(
            'errors.generic.internal.server.error.description',
          ),
        });
        break;
      default:
        this.messageService.add({
          severity: 'error',
          summary: this.translate.instant(
            'errors.generic.unexpected.error.title',
          ),
          detail: this.translate.instant(
            'errors.generic.unexpected.error.description',
          ),
        });
    }
  }
}
