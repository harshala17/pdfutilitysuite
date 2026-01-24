import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PptToPdf } from './ppt-to-pdf';

describe('PptToPdf', () => {
  let component: PptToPdf;
  let fixture: ComponentFixture<PptToPdf>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PptToPdf]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PptToPdf);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
