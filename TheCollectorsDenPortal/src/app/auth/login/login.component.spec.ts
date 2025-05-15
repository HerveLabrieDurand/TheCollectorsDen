import {
  ComponentFixture,
  fakeAsync,
  TestBed,
  tick,
} from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng/api';
import { of } from 'rxjs';
import { AuthService } from '../../services/auth/auth.service';
import { LoadingService } from '../../services/loading/loading.service';
import { mockActivatedRoute } from '../../tests/mocks/mockActivatedRoute';
import { mockRouter } from '../../tests/mocks/mockRouter';
import { mockTranslateService } from '../../tests/mocks/translateServiceMock';
import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let router: Router;
  let messageService: MessageService;
  let loadingService: LoadingService;
  let translateService: TranslateService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouterModule, ReactiveFormsModule, LoginComponent],
      providers: [
        { provide: Router, useValue: mockRouter },
        {
          provide: AuthService,
          useValue: {
            authenticate: jest.fn(() => of({})),
            saveToken: jest.fn(),
            setUser: jest.fn()
          },
        },
        { provide: MessageService, useValue: { add: jest.fn() } },
        {
          provide: LoadingService,
          useValue: { show: jest.fn(), hide: jest.fn() },
        },
        { provide: TranslateService, useValue: mockTranslateService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;

    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    messageService = TestBed.inject(MessageService);
    loadingService = TestBed.inject(LoadingService);
    translateService = TestBed.inject(TranslateService);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with required controls', () => {
    expect(component.loginForm.contains('email')).toBeTruthy();
    expect(component.loginForm.contains('password')).toBeTruthy();
  });

  it('should handle successful login', fakeAsync(() => {
    const validFormData = {
      email: 'test@example.com',
      password: 'ValidPass123!',
    };

    component.loginForm.setValue(validFormData);
    component.onSubmit();
    tick();

    expect(authService.authenticate).toHaveBeenCalledWith(validFormData);
    expect(loadingService.show).toHaveBeenCalled();
    expect(loadingService.hide).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['overview']);
    expect(messageService.add).toHaveBeenCalledWith({
      severity: 'success',
      summary: 'login.success.toast.title',
      detail: 'login.success.toast.description',
    });
  }));
});
