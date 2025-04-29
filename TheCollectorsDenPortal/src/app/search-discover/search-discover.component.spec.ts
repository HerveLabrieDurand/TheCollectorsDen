import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchDiscoverComponent } from './search-discover.component';

describe('SearchDiscoverComponent', () => {
  let component: SearchDiscoverComponent;
  let fixture: ComponentFixture<SearchDiscoverComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchDiscoverComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchDiscoverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
