import {
  ComponentFixture,
  fakeAsync,
  TestBed,
  tick,
} from '@angular/core/testing';

import { PLATFORM_ID } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { screen } from '@testing-library/angular';
import '@testing-library/jest-dom';
import { MessageService } from 'primeng/api';
import { of } from 'rxjs';
import { AuthService } from '../../services/auth/auth.service';
import { LoadingService } from '../../services/loading/loading.service';
import { mockActivatedRoute } from '../../tests/mocks/mockActivatedRoute';
import { mockRouter } from '../../tests/mocks/mockRouter';
import { mockTranslateService } from '../../tests/mocks/translateServiceMock';
import { ConfirmEmailComponent } from './confirm-email.component';

describe('ConfirmEmailComponent', () => {
  let component: ConfirmEmailComponent;
  let fixture: ComponentFixture<ConfirmEmailComponent>;
  let authService: AuthService;
  let router: Router;
  let messageService: MessageService;
  let loadingService: LoadingService;
  let translateService: TranslateService;
  let platformId: any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmEmailComponent],
      providers: [
        { provide: Router, useValue: mockRouter },
        {
          provide: AuthService,
          useValue: {
            resendConfirmationEmail: jest.fn(() => of({})),
            confirmEmail: jest.fn(() => of({})),
            getCurrentUser: jest.fn(() => null),
          },
        },
        { provide: MessageService, useValue: { add: jest.fn() } },
        {
          provide: LoadingService,
          useValue: { show: jest.fn(), hide: jest.fn() },
        },
        { provide: TranslateService, useValue: mockTranslateService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: PLATFORM_ID, useValue: 'browser' },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ConfirmEmailComponent);
    component = fixture.componentInstance;

    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    messageService = TestBed.inject(MessageService);
    loadingService = TestBed.inject(LoadingService);
    translateService = TestBed.inject(TranslateService);
    platformId = TestBed.inject(PLATFORM_ID);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();

    component.tipsBeforeResend.forEach((element) => {
      expect(screen.getByText(element)).toBeInTheDocument();
    });
  });

  it('should not confirm email when token is absent', () => {
    mockActivatedRoute.snapshot.queryParamMap = {
      get: jest.fn().mockReturnValue(null),
      has: jest.fn(),
      getAll: jest.fn(),
      keys: [],
    };

    fixture.detectChanges();

    expect(authService.confirmEmail).not.toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();
    expect(messageService.add).not.toHaveBeenCalled();
  });

  it('should confirm email when token is present', () => {
    // Mock token presence BEFORE component initialization
    mockActivatedRoute.snapshot.queryParamMap = {
      get: jest.fn().mockReturnValue('valid-token'),
      has: jest.fn(),
      getAll: jest.fn(),
      keys: [],
    };

    // Reinitialize the component
    fixture = TestBed.createComponent(ConfirmEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(authService.confirmEmail).toHaveBeenCalledWith('valid-token');
    expect(router.navigate).toHaveBeenCalledWith(['']);
    expect(messageService.add).toHaveBeenCalledWith({
      severity: 'success',
      summary: 'confirm.email.sucessful.confirmation.title',
      detail: 'confirm.email.sucessful.confirmation.description',
      sticky: true,
    });
  });

  it('should handle successful resend', fakeAsync(() => {
    const validFormData = {
      email: 'test@example.com',
    };

    component.emailForm.setValue(validFormData);
    component.onSubmit();
    tick();

    expect(authService.resendConfirmationEmail).toHaveBeenCalledWith(
      validFormData.email,
    );
    expect(loadingService.show).toHaveBeenCalled();
    expect(loadingService.hide).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['']);
    expect(messageService.add).toHaveBeenCalledWith({
      severity: 'success',
      summary: 'confirm.email.sucessful.resend.title',
      detail: 'confirm.email.sucessful.resend.description',
      sticky: true,
    });
  }));
});
