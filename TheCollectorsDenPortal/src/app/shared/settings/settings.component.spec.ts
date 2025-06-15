import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PLATFORM_ID } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { mockTranslateService } from '../../tests/mocks/translateServiceMock';
import { SettingsComponent } from './settings.component';

describe('SettingsComponent', () => {
  let component: SettingsComponent;
  let fixture: ComponentFixture<SettingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SettingsComponent],
      providers: [
        { provide: TranslateService, useValue: mockTranslateService },
        { provide: PLATFORM_ID, useValue: 'browser' },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
