import { Component } from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { matSearch } from '@ng-icons/material-icons/baseline';
import { TranslatePipe } from '@ngx-translate/core';
import { SettingsComponent } from '../shared/settings/settings.component';

@Component({
  selector: 'app-search-bar',
  imports: [NgIcon, TranslatePipe, SettingsComponent],
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.css',
  viewProviders: [
    provideIcons({
      matSearch,
    }),
  ],
})
export class SearchBarComponent {}
