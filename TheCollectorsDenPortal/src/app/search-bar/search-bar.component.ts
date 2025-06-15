import { Component } from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { matSearch } from '@ng-icons/material-icons/baseline';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-search-bar',
  imports: [NgIcon, TranslatePipe],
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.css',
  viewProviders: [
    provideIcons({
      matSearch,
    }),
  ],
})
export class SearchBarComponent {}
