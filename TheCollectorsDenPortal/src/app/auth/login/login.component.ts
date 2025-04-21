import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng/api';
import { AuthenticateRequest } from '../../dto/auth/authenticateRequest';
import { HeaderComponent } from '../../header/header.component';
import { AuthService } from '../../services/auth/auth.service';
import { LoadingService } from '../../services/loading/loading.service';

@Component({
  selector: 'app-login',
  imports: [HeaderComponent, ReactiveFormsModule, RouterModule, TranslatePipe],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;

  constructor(
    private authService: AuthService,
    private formBuilder: FormBuilder,
    private loadingService: LoadingService,
    private translateService: TranslateService,
    private messageService: MessageService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.loadingService.show();

      const authenticateRequest: AuthenticateRequest = this.loginForm.value;

      this.authService.authenticate(authenticateRequest).subscribe({
        next: (response: any) => {
          this.loadingService.hide();
          this.authService.setUser()
          this.authService.saveToken(response.accessToken);
          this.router.navigate(['dashboard']);
          this.messageService.add({
            severity: 'success',
            summary: this.translateService.instant('login.success.toast.title'),
            detail: this.translateService.instant(
              'login.success.toast.description',
            ),
          });
        },
        error: () => {
          this.loadingService.hide();
        },
      });
    } else {
      // trigger validation messages
      this.loginForm.markAllAsTouched();
    }
  }
}
