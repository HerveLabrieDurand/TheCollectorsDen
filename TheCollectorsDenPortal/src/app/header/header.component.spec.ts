import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { screen } from '@testing-library/angular';
import '@testing-library/jest-dom';
import { mockActivatedRoute } from '../tests/mocks/mockActivatedRoute';
import { mockRouter } from '../tests/mocks/mockRouter';
import { mockTranslateService } from '../tests/mocks/translateServiceMock';
import { HeaderComponent } from './header.component';
import { SettingsComponent } from './settings/settings.component';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let router: Router;
  let translateService: TranslateService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderComponent, SettingsComponent],
      providers: [
        { provide: Router, useValue: mockRouter },
        { provide: TranslateService, useValue: mockTranslateService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;

    component.showAuthBtns = true;
    component.showTitle = true;

    router = TestBed.inject(Router);
    translateService = TestBed.inject(TranslateService);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display the title when showTitle is true', () => {
    component.showTitle = true;
    fixture.detectChanges();
    expect(screen.getByTestId('title')).toBeInTheDocument();
  });

  it('should not display the title when showTitle is false', () => {
    component.showTitle = false;
    fixture.detectChanges();
    expect(screen.queryByTestId('title')).not.toBeInTheDocument();
  });

  it('should display auth buttons when showAuthBtns is true', () => {
    component.showAuthBtns = true;
    fixture.detectChanges();
    expect(screen.getByTestId('authButtonsDiv')).toBeInTheDocument();
  });

  it('should not display auth buttons when showAuthBtns is false', () => {
    component.showAuthBtns = false;
    fixture.detectChanges();
    expect(screen.queryByTestId('authButtonsDiv')).not.toBeInTheDocument();
  });
});
