import { Routes } from '@angular/router';
import { ConfirmEmailComponent } from './auth/confirm-email/confirm-email.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { WelcomePageComponent } from './welcome-page/welcome-page.component';

export const routes: Routes = [
  { path: '', component: WelcomePageComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'confirm-email', component: ConfirmEmailComponent },
];
