import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExcelToPdf } from './excel-to-pdf';

describe('ExcelToPdf', () => {
  let component: ExcelToPdf;
  let fixture: ComponentFixture<ExcelToPdf>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExcelToPdf]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExcelToPdf);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
