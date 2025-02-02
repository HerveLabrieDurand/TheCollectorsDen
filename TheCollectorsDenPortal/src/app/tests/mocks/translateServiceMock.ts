import { of } from 'rxjs';

export const mockTranslateService = {
  instant: jest.fn((key: string) => key),
  get: jest.fn((key: string) => of(key)),
  stream: jest.fn((key: string) => of(key)),
  use: jest.fn(),
  currentLang: 'en',
  onLangChange: of({ lang: 'en', translations: {} }),
  onTranslationChange: of({}),
  onDefaultLangChange: of({}),
};
