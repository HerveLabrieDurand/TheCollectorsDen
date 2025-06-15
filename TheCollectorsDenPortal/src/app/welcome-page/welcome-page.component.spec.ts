import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { HeaderComponent } from '../header/header.component';
import { AuthService } from '../services/auth/auth.service';
import { mockActivatedRoute } from '../tests/mocks/mockActivatedRoute';
import { mockTranslateService } from '../tests/mocks/translateServiceMock';
import { WelcomePageComponent } from './welcome-page.component';

describe('WelcomePageComponent', () => {
  let component: WelcomePageComponent;
  let fixture: ComponentFixture<WelcomePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WelcomePageComponent, HeaderComponent],
      providers: [
        provideHttpClientTesting(),
        { provide: TranslateService, useValue: mockTranslateService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        {
          provide: AuthService,
          useValue: {
            getCurrentUser: jest.fn(() => null),
          },
        },
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
