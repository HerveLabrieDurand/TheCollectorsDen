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
  private readonly USER_KEY = 'currentUser';
  private readonly TOKEN_KEY = 'jwt';
  private userSubject = new BehaviorSubject<UserDto | null>(null);
  user$ = this.userSubject.asObservable();

  constructor(
    private apiService: ApiService,
  ) {
    this.restoreSession();
  }

  private restoreSession() {
    const token = this.getToken();
    const userJson = sessionStorage.getItem(this.USER_KEY);

    if (token && userJson) {
      try {
        const user: UserDto = JSON.parse(userJson);
        this.userSubject.next(user);
      } catch (e) {
        this.clearUser(); // corrupted user object
      }
    }
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
    return sessionStorage.getItem(this.TOKEN_KEY)
  }

  saveToken(token: string): void {
    sessionStorage.setItem(this.TOKEN_KEY, token);
  }

  removeToken(): void {
    sessionStorage.removeItem(this.TOKEN_KEY);
  }

  setUser(user: UserDto): void {
    this.userSubject.next(user);
    sessionStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }

  clearUser(): void {
    this.userSubject.next(null);
    sessionStorage.removeItem(this.TOKEN_KEY);
    sessionStorage.removeItem(this.USER_KEY);
  }

  getCurrentUser(): UserDto | null {
    return this.userSubject.value;
  }
}
