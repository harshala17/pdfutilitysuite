import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MergePdf } from './merge-pdf';

describe('MergePdf', () => {
  let component: MergePdf;
  let fixture: ComponentFixture<MergePdf>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MergePdf]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MergePdf);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
