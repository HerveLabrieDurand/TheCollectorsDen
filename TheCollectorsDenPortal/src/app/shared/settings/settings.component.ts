import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { Router } from '@angular/router';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { matLogoutOutline, matSettingsOutline } from '@ng-icons/material-icons/outline';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [
    TranslatePipe,
    NgIcon,
    MatButtonModule,
    MatMenuModule,
    MatIconModule,
    NgIf,
  ],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css',
  viewProviders: [provideIcons({ matSettingsOutline, matLogoutOutline })],
})
export class SettingsComponent {
  constructor(
    private translate: TranslateService,
    private authService: AuthService,
    private router: Router
  ) {}

  get isLoggedIn(): boolean {
    return this.authService.getCurrentUser() !== null;
  }

  logout(): void {
    this.authService.clearUser();
    this.router.navigate(['/']);
  }

  getLang() {
    return localStorage.getItem('fav-lang');
  }

  toggleTheme() {
    const body = document.body;

    if (body.classList.contains('dark')) {
      body.classList.remove('dark'); // Switch to light theme
      localStorage.setItem('theme', 'light');
    } else {
      body.classList.add('dark'); // Switch to dark theme
      localStorage.setItem('theme', 'dark');
    }
  }

  toggleLocale() {
    const currentLang = localStorage.getItem('fav-lang');
    const isFrench = currentLang === 'fr';
    if (isFrench) {
      this.translate.use('en');
      localStorage.setItem('fav-lang', 'en');
    } else {
      this.translate.use('fr');
      localStorage.setItem('fav-lang', 'fr');
    }
  }
}
