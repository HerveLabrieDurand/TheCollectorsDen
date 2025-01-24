import { NgFor, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import {
  matCheck,
  matKeyboardArrowDown,
  matKeyboardArrowRight,
} from '@ng-icons/material-icons/baseline';
import { TranslatePipe } from '@ngx-translate/core';
import { SettingsComponent } from '../../header/settings/settings.component';

@Component({
  selector: 'app-register',
  imports: [TranslatePipe, SettingsComponent, NgIcon, NgIf, NgFor],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
  viewProviders: [
    provideIcons({ matKeyboardArrowRight, matKeyboardArrowDown, matCheck }),
  ],
})
export class RegisterComponent {
  openFeatures: boolean = false;

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

  toggleOpenFeatures() {
    this.openFeatures = !this.openFeatures;
  }
}
