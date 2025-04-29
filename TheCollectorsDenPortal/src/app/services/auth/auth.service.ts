import { isPlatformBrowser } from '@angular/common';
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { BehaviorSubject, tap } from 'rxjs';
import { AuthenticateRequest } from '../../dto/auth/authenticateRequest';
import { RegisterRequest } from '../../dto/auth/registerRequest';
import { UserDto } from '../../dto/auth/userDto';
import { ApiService } from '../api/api.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly TOKEN_KEY = 'jwt';
  private userSubject = new BehaviorSubject<UserDto | null>(null);
  user$ = this.userSubject.asObservable();

  constructor(
    private apiService: ApiService,
    @Inject(PLATFORM_ID) private platformId: any,
  ) {
    this.loadUserFromServer();
  }

  authenticate(request: AuthenticateRequest) {
    return this.apiService.post('auth/authenticate', request).pipe(
      tap((response: any) => {
        this.saveToken(response.token);
        this.setUser(response.user);
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
    return isPlatformBrowser(this.platformId)
      ? sessionStorage.getItem(this.TOKEN_KEY)
      : null;
  }

  saveToken(token: string): void {
    if (isPlatformBrowser(this.platformId)) {
      sessionStorage.setItem(this.TOKEN_KEY, token);
    }
  }

  removeToken(): void {
    if (isPlatformBrowser(this.platformId)) {
      sessionStorage.removeItem(this.TOKEN_KEY);
    }
  }

  setUser(user: UserDto): void {
    this.userSubject.next(user);
  }

  clearUser(): void {
    this.removeToken();
    this.userSubject.next(null);
  }

  getCurrentUser(): UserDto | null {
    return this.userSubject.value;
  }

  /**
   * Loads the current user from the backend if a token is present.
   * Should be called automatically on service construction.
   */
  private loadUserFromServer(): void {
    const token = this.getToken();
    if (!token) {
      return;
    }

    this.apiService.get<UserDto>('auth/me').subscribe({
      next: (user) => this.setUser(user),
      error: () => this.clearUser(),
    });
  }
}
