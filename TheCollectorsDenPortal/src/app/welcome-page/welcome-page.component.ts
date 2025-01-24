import { Component } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { HeaderComponent } from '../header/header.component';

@Component({
  selector: 'app-welcome-page',
  standalone: true,
  imports: [TranslatePipe, HeaderComponent],
  templateUrl: './welcome-page.component.html',
  styleUrl: './welcome-page.component.css',
})
export class WelcomePageComponent {}
