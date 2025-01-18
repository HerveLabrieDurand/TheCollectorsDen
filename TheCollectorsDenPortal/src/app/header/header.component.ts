import { NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { provideIcons } from '@ng-icons/core';
import { matSettingsOutline } from '@ng-icons/material-icons/outline';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { SettingsComponent } from './settings/settings.component';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [TranslatePipe, SettingsComponent, NgIf, RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  viewProviders: [provideIcons({ matSettingsOutline })],
})
export class HeaderComponent {
  @Input({ required: true }) showAuthBtns!: boolean;
  @Input({ required: true }) showTitle!: boolean;

  constructor(private translate: TranslateService, private router: Router) {}
}
