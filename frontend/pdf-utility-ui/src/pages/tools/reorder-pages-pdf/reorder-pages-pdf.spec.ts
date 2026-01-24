import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReorderPagesPdf } from './reorder-pages-pdf';

describe('ReorderPagesPdf', () => {
  let component: ReorderPagesPdf;
  let fixture: ComponentFixture<ReorderPagesPdf>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReorderPagesPdf]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReorderPagesPdf);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
