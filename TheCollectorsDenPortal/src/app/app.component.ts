import { isPlatformBrowser } from '@angular/common';
import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import translationsEN from '../../public/i18n/en.json';
import translationsFR from '../../public/i18n/fr.json';
import { FooterComponent } from './footer/footer.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FooterComponent], // add TranslateDirective if needed
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'TheCollectorsDenPortal';
  constructor(
    private translate: TranslateService,
    @Inject(PLATFORM_ID) private platformId: object // Injects platform context
  ) {
    this.translate.addLangs(['en', 'fr']);
    this.translate.setTranslation('en', translationsEN);
    this.translate.setTranslation('fr', translationsFR);
    this.translate.setDefaultLang('en');

    // Ensure localStorage is accessed only in the browser
    if (isPlatformBrowser(this.platformId)) {
      const currentLang = localStorage.getItem('fav-lang');
      if (currentLang) {
        this.translate.use(currentLang);
      } else {
        this.translate.use('en');
        localStorage.setItem('fav-lang', 'en');
      }

      const currentTheme = localStorage.getItem('theme');
      if (currentTheme) {
        const isDark = currentTheme === 'dark';

        isDark
          ? document.body.classList.add('dark')
          : document.body.classList.remove('dark');
      }
    }
  }
}
