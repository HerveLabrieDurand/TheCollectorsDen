import { Injectable } from '@angular/core';
import { RegisterRequest } from '../../dto/auth/registerRequest';
import { ApiService } from '../api/api.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private apiService: ApiService) {}

  register(request: RegisterRequest) {
    return this.apiService.post('auth/register', request);
  }
}
