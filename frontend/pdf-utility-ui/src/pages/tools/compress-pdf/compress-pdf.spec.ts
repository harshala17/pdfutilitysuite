import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompressPdf } from './compress-pdf';

describe('CompressPdf', () => {
  let component: CompressPdf;
  let fixture: ComponentFixture<CompressPdf>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompressPdf]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompressPdf);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
