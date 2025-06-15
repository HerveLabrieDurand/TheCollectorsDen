import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PLATFORM_ID } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { mockTranslateService } from '../../tests/mocks/translateServiceMock';
import { AuthService } from '../../services/auth/auth.service';
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
        { provide: Router, useValue: { navigate: jest.fn() } },
        {
          provide: AuthService,
          useValue: {
            getCurrentUser: jest.fn(() => null),
            clearUser: jest.fn(),
          },
        },
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
