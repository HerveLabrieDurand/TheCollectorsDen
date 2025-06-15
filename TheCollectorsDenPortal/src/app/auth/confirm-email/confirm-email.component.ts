import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { matCheck } from '@ng-icons/material-icons/baseline';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng/api';
import { HeaderComponent } from '../../header/header.component';
import { AuthService } from '../../services/auth/auth.service';
import { LoadingService } from '../../services/loading/loading.service';

@Component({
  selector: 'app-confirm-email',
  standalone: true,
  imports: [
    HeaderComponent,
    TranslatePipe,
    ReactiveFormsModule,
    NgIcon,
    NgFor,
    NgIf,
  ],
  templateUrl: './confirm-email.component.html',
  styleUrl: './confirm-email.component.css',
  viewProviders: [provideIcons({ matCheck })],
})
export class ConfirmEmailComponent implements OnInit {
  emailForm!: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private formBuilder: FormBuilder,
    private messageService: MessageService,
    private translateService: TranslateService,
    private loadingService: LoadingService,
  ) {}

  tipsBeforeResend: string[] = [
    'confirm.email.having.trouble.tips.one',
    'confirm.email.having.trouble.tips.two',
    'confirm.email.having.trouble.tips.three',
  ];

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');

    if (token) {
      this.authService.confirmEmail(token).subscribe({
        next: () => {
          this.router.navigate(['']);
          this.messageService.add({
            severity: 'success',
            summary: this.translateService.instant(
              'confirm.email.sucessful.confirmation.title',
            ),
            detail: this.translateService.instant(
              'confirm.email.sucessful.confirmation.description',
            ),
            sticky: true,
          });
        },
      });
    }

    this.emailForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

  onSubmit() {
    if (this.emailForm.valid) {
      this.loadingService.show();

      this.authService
        .resendConfirmationEmail(this.emailForm.get('email')!.value)
        .subscribe({
          next: () => {
            this.loadingService.hide();
            this.router.navigate(['']);
            this.messageService.add({
              severity: 'success',
              summary: this.translateService.instant(
                'confirm.email.sucessful.resend.title',
              ),
              detail: this.translateService.instant(
                'confirm.email.sucessful.resend.description',
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
      this.emailForm.markAllAsTouched();
    }
  }
}
