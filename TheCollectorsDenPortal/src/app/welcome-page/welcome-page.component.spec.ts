import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TranslateService } from '@ngx-translate/core';
import { HeaderComponent } from '../header/header.component';
import { mockTranslateService } from '../tests/mocks/translateServiceMock';
import { WelcomePageComponent } from './welcome-page.component';
import { ActivatedRoute } from '@angular/router';
import { mockActivatedRoute } from '../tests/mocks/mockActivatedRoute';

describe('WelcomePageComponent', () => {
  let component: WelcomePageComponent;
  let fixture: ComponentFixture<WelcomePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WelcomePageComponent, HeaderComponent],
      providers: [
        { provide: TranslateService, useValue: mockTranslateService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(WelcomePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
