import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { NgIcon, provideIcons } from '@ng-icons/core';
import {
  matCheck,
  matKeyboardArrowDown,
  matKeyboardArrowRight,
} from '@ng-icons/material-icons/baseline';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng/api';
import { RegisterRequest } from '../../dto/auth/registerRequest';
import { SettingsComponent } from '../../header/settings/settings.component';
import { AuthService } from '../../services/auth/auth.service';
import { LoadingService } from '../../services/loading/loading.service';
import {
  passwordValidator,
  phoneNumberValidator,
  postalCodeValidator,
} from '../../utils/validators';

@Component({
  selector: 'app-register',
  imports: [
    TranslatePipe,
    SettingsComponent,
    NgIcon,
    NgIf,
    NgFor,
    ReactiveFormsModule,
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
  viewProviders: [
    provideIcons({ matKeyboardArrowRight, matKeyboardArrowDown, matCheck }),
  ],
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  openFeatures: boolean = false;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService,
    private translateService: TranslateService,
    private loadingService: LoadingService,
  ) {}

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, passwordValidator]],
      country: ['', Validators.required],
      address: ['', Validators.required],
      city: ['', Validators.required],
      postalCode: ['', [Validators.required, postalCodeValidator]],
      phoneNumber: ['', [Validators.required, phoneNumberValidator]],
    });
  }

  featureTitleTranslationKeys: string[] = [
    'register.features.search.title',
    'register.features.suggestions.title',
    'register.features.sales.title',
    'register.features.payment.title',
    'register.features.reviews.title',
    'register.features.delivery.title',
    'register.features.dashboard.title',
    'register.features.forum.title',
  ];

  featureDescriptionTranslationKeys: string[] = [
    'register.features.search.description',
    'register.features.suggestions.description',
    'register.features.sales.description',
    'register.features.payment.description',
    'register.features.reviews.description',
    'register.features.delivery.description',
    'register.features.dashboard.description',
    'register.features.forum.description',
  ];

  navigateToLogin() {
    this.router.navigate(['/login']);
  }

  onSubmit() {
    if (this.registerForm.valid) {
      this.loadingService.show();

      // Extract form values and map them to the RegisterRequest object
      const registerRequest: RegisterRequest = this.registerForm.value;

      this.authService.register(registerRequest).subscribe({
        next: () => {
          this.loadingService.hide();
          this.router.navigate(['']);
          this.messageService.add({
            severity: 'success',
            summary: this.translateService.instant(
              'register.success.toast.title',
            ),
            detail: this.translateService.instant(
              'register.success.toast.description',
            ),
            sticky: true,
          });
        },
        error: () => {
          this.loadingService.hide();
        },
      });
    } else {
      // trigger validation messages
      this.registerForm.markAllAsTouched();
    }
  }

  toggleOpenFeatures() {
    this.openFeatures = !this.openFeatures;
  }
}
