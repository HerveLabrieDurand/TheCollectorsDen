import { Injectable } from '@angular/core';
import { AuthenticateRequest } from '../../dto/auth/authenticateRequest';
import { RegisterRequest } from '../../dto/auth/registerRequest';
import { ApiService } from '../api/api.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private apiService: ApiService) {}

  private readonly TOKEN_KEY = 'jwt';

  authenticate(request: AuthenticateRequest) {
    return this.apiService.post('auth/authenticate', request);
  }

  confirmEmail(token: string) {
    return this.apiService.post(
      `auth/confirm-email?token=${encodeURIComponent(token)}`,
      null,
    );
  }

  register(request: RegisterRequest) {
    return this.apiService.post('auth/register', request);
  }

  resendConfirmationEmail(email: string) {
    return this.apiService.post(
      `auth/resend-confirmation-email?email=${encodeURIComponent(email)}`,
      null,
    );
  }

  getToken(): string | null {
    return sessionStorage.getItem(this.TOKEN_KEY);
  }

  saveToken(token: string): void {
    console.log('saving token');
    sessionStorage.setItem(this.TOKEN_KEY, token);
  }

  removeToken(): void {
    sessionStorage.removeItem(this.TOKEN_KEY);
  }
}
