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
import { TranslatePipe } from '@ngx-translate/core';
import { RegisterRequest } from '../../dto/auth/registerRequest';
import { SettingsComponent } from '../../header/settings/settings.component';
import { AuthService } from '../../services/auth/auth.service';
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
  ) {}

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, passwordValidator]], // Use passwordValidator
      country: ['', Validators.required],
      address: ['', Validators.required],
      city: ['', Validators.required],
      postalCode: ['', [Validators.required, postalCodeValidator]], // Use postalCodeValidator
      phoneNumber: ['', [Validators.required, phoneNumberValidator]], // Use phoneNumberValidator
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
      // Extract form values and map them to the RegisterRequest object
      const registerRequest: RegisterRequest = this.registerForm.value;

      // Call the register method in AuthService
      this.authService.register(registerRequest).subscribe({
        next: (response) => {
          // Handle successful registration, e.g., navigate to a different page or show a success message
          console.log('Registration successful:', response);
          this.router.navigate(['/login']);
        },
        error: (error) => {
          // Handle registration failure, e.g., show an error message
          console.error('Registration failed:', error);
        },
      });
    } else {
      // Mark all controls as touched to trigger validation messages
      this.registerForm.markAllAsTouched();
    }
  }

  toggleOpenFeatures() {
    this.openFeatures = !this.openFeatures;
  }
}
