import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import translationsEN from '../../public/i18n/en.json';
import translationsFR from '../../public/i18n/fr.json';

@Component({
  selector: 'app-root',
  standalone: true,
  // These imports will be available in all child components, even if they are standalone
  imports: [RouterOutlet, TranslatePipe], // add TranslateDirective if needed
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'TheCollectorsDenPortal';
  constructor(private translate: TranslateService) {
    this.translate.addLangs(['en', 'fr']);
    this.translate.setTranslation('en', translationsEN);
    this.translate.setTranslation('fr', translationsFR);
    this.translate.setDefaultLang('en');
    this.translate.use('en');
  }
}
