import { CommonModule } from '@angular/common';
import {
  ComponentFixture,
  fakeAsync,
  TestBed,
  tick,
} from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { NgIcon, provideIcons } from '@ng-icons/core';
import {
  matCheck,
  matKeyboardArrowDown,
  matKeyboardArrowRight,
} from '@ng-icons/material-icons/baseline';
import { TranslateService } from '@ngx-translate/core';
import { screen } from '@testing-library/angular';
import '@testing-library/jest-dom';
import { MessageService } from 'primeng/api';
import { of } from 'rxjs';
import { AuthService } from '../../services/auth/auth.service';
import { LoadingService } from '../../services/loading/loading.service';
import { SettingsComponent } from '../../shared/settings/settings.component';
import { mockTranslateService } from '../../tests/mocks/translateServiceMock';
import { RegisterComponent } from './register.component';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;
  let messageService: MessageService;
  let loadingService: LoadingService;
  let translateService: TranslateService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        CommonModule,
        ReactiveFormsModule,
        SettingsComponent,
        RegisterComponent,
        NgIcon,
      ],
      providers: [
        { provide: Router, useValue: { navigate: jest.fn() } },
        {
          provide: AuthService,
          useValue: {
            register: jest.fn(() => of({})),
            getCurrentUser: jest.fn(() => null),
          },
        },
        { provide: MessageService, useValue: { add: jest.fn() } },
        {
          provide: LoadingService,
          useValue: { show: jest.fn(), hide: jest.fn() },
        },
        { provide: TranslateService, useValue: mockTranslateService },
        provideIcons({ matKeyboardArrowRight, matKeyboardArrowDown, matCheck }),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
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
    expect(component.registerForm.contains('name')).toBeTruthy();
    expect(component.registerForm.contains('email')).toBeTruthy();
    expect(component.registerForm.contains('password')).toBeTruthy();
    expect(component.registerForm.contains('country')).toBeTruthy();
    expect(component.registerForm.contains('address')).toBeTruthy();
    expect(component.registerForm.contains('city')).toBeTruthy();
    expect(component.registerForm.contains('postalCode')).toBeTruthy();
    expect(component.registerForm.contains('phoneNumber')).toBeTruthy();
  });

  it('should handle successful registration', fakeAsync(() => {
    const validFormData = {
      name: 'Test User',
      email: 'test@example.com',
      password: 'ValidPass123!',
      country: 'Country',
      address: 'Address',
      city: 'City',
      postalCode: 'G1G1G1',
      phoneNumber: '1112223344',
    };

    component.registerForm.setValue(validFormData);
    component.onSubmit();
    tick();

    expect(authService.register).toHaveBeenCalledWith(validFormData);
    expect(loadingService.show).toHaveBeenCalled();
    expect(loadingService.hide).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['']);
    expect(messageService.add).toHaveBeenCalledWith({
      severity: 'success',
      summary: 'register.success.toast.title',
      detail: 'register.success.toast.description',
      sticky: true,
    });
  }));

  it('should toggle features visibility', () => {
    expect(screen.queryByTestId('features')).not.toBeInTheDocument();
    expect(component.openFeatures).toBe(false);

    component.toggleOpenFeatures();
    expect(component.openFeatures).toBe(true);

    fixture.detectChanges();

    expect(screen.getByTestId('features')).toBeInTheDocument();

    component.featureTitleTranslationKeys.forEach((element) => {
      expect(screen.getByText(element)).toBeInTheDocument();
    });
    component.featureDescriptionTranslationKeys.forEach((element) => {
      expect(screen.getByText(element)).toBeInTheDocument();
    });
  });

  it('should display validation messages for required fields', () => {
    const nameControl = component.registerForm.get('name');
    nameControl?.markAsTouched();
    fixture.detectChanges();

    expect(screen.getByText('validation.field.required')).toBeInTheDocument();
  });

  it('should enable submit button when form is valid', async () => {
    const button = screen.getByTestId('submit-button');
    expect(button).toBeDisabled();

    const validFormData = {
      name: 'Test User',
      email: 'test@example.com',
      password: 'ValidPass123!',
      country: 'Country',
      address: 'Address',
      city: 'City',
      postalCode: 'G1G1G1',
      phoneNumber: '1112223344',
    };

    component.registerForm.setValue(validFormData);
    fixture.detectChanges();

    expect(button).toBeEnabled();
  });
});
