import { PLATFORM_ID } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ApiService } from '../api/api.service';
import { AuthService } from './auth.service';

jest.mock('../api/api.service');

describe('AuthService', () => {
  let service: AuthService;
  let apiService: jest.Mocked<ApiService>;
  const mockPlatformId = 'browser';

  beforeEach(() => {
    const apiServiceMock: jest.Mocked<ApiService> = {
      post: jest.fn(),
    } as any;

    TestBed.configureTestingModule({
      providers: [
        AuthService,
        { provide: ApiService, useValue: apiServiceMock },
        { provide: PLATFORM_ID, useValue: mockPlatformId },
      ],
    });

    service = TestBed.inject(AuthService);
    apiService = TestBed.inject(ApiService) as jest.Mocked<ApiService>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call apiService.post on authenticate', () => {
    const request = { email: 'test@example.com', password: 'password' };
    apiService.post.mockReturnValue(of({ token: 'mockToken' }));

    service.authenticate(request).subscribe((response) => {
      expect(response).toEqual({ token: 'mockToken' });
    });

    expect(apiService.post).toHaveBeenCalledWith('auth/authenticate', request);
  });

  it('should call apiService.post on confirmEmail', () => {
    const token = 'mockToken';
    apiService.post.mockReturnValue(of(null));

    service.confirmEmail(token).subscribe();

    expect(apiService.post).toHaveBeenCalledWith(
      `auth/confirm-email?token=${encodeURIComponent(token)}`,
      null,
    );
  });

  it('should save token in sessionStorage when saveToken is called', () => {
    const token = 'mockJwt';
    Storage.prototype.setItem = jest.fn();

    service.saveToken(token);

    expect(sessionStorage.setItem).toHaveBeenCalledWith('jwt', token);
  });

  it('should retrieve token from sessionStorage when getToken is called', () => {
    Storage.prototype.getItem = jest.fn().mockReturnValue('mockJwt');

    const token = service.getToken();

    expect(token).toBe('mockJwt');
  });

  it('should remove token from sessionStorage when removeToken is called', () => {
    Storage.prototype.removeItem = jest.fn();

    service.removeToken();

    expect(sessionStorage.removeItem).toHaveBeenCalledWith('jwt');
  });
});
