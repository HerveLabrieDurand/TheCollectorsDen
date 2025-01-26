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

  private handleError(error: HttpErrorResponse): void {
    switch (error.status) {
      case 400:
        this.messageService.add({
          severity: 'error',
          summary: 'MUST_HANDLE_ERROR',
          detail: 'handle this new 400 error.',
        });
        break;
      case 401:
        if (error.error.error === 'EmailAlreadyInUseException') {
          this.messageService.add({
            severity: 'error',
            summary: this.translate.instant(
              'errors.specific.email.already.in.use.error.title',
            ),
            detail: this.translate.instant(
              'errors.specific.email.already.in.use.error.description',
            ),
          });
        } else {
          this.messageService.add({
            severity: 'error',
            summary: 'MUST_HANDLE_ERROR',
            detail: 'handle this new 401 error.',
          });
          break;
        }
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
