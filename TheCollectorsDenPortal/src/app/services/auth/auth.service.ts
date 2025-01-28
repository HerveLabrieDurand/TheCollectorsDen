import { Injectable } from '@angular/core';
import { RegisterRequest } from '../../dto/auth/registerRequest';
import { ApiService } from '../api/api.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private apiService: ApiService) {}

  confirmEmail(token: string) {
    return this.apiService.post(`auth/confirm-email?token=${encodeURIComponent(token)}`, null);
  }

  register(request: RegisterRequest) {
    return this.apiService.post('auth/register', request);
  }

  resendConfirmationEmail(email: string) {
    return this.apiService.post(`auth/resend-confirmation-email?email=${encodeURIComponent(email)}`, null);
  }
}
