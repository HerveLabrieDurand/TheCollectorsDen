import { isPlatformBrowser } from '@angular/common';
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { AuthenticateRequest } from '../../dto/auth/authenticateRequest';
import { RegisterRequest } from '../../dto/auth/registerRequest';
import { ApiService } from '../api/api.service';
import { UserDto } from '../../dto/auth/UserDto';
import { BehaviorSubject, tap } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(
    private apiService: ApiService,
    @Inject(PLATFORM_ID) private platformId: any,
  ) {}

  private readonly TOKEN_KEY = 'jwt';
  private userSubject = new BehaviorSubject<UserDto | null>(null);
  user$ = this.userSubject.asObservable();

  authenticate(request: AuthenticateRequest) {
    return this.apiService.post('auth/authenticate', request).pipe(
      tap((response: any) => {
        this.saveToken(response.token);
        this.setUser(response.user); // Assuming the response contains user data
      }),
    );
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
    if (isPlatformBrowser(this.platformId)) {
      return sessionStorage.getItem(this.TOKEN_KEY);
    }
    return null;
  }

  saveToken(token: string): void {
    if (isPlatformBrowser(this.platformId)) {
      sessionStorage.setItem(this.TOKEN_KEY, token);
    }
  }

  removeToken(): void {
    if (isPlatformBrowser(this.platformId)) {
      sessionStorage.removeItem(this.TOKEN_KEY);
      this.clearUser();
    }
  }

  setUser(user: UserDto): void {
    this.userSubject.next(user);
  }

  clearUser(): void {
    this.userSubject.next(null);
  }

  getCurrentUser(): UserDto | null {
    return this.userSubject.value;
  }
}
