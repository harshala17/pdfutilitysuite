import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemovePagesPdf } from './remove-pages-pdf';

describe('RemovePagesPdf', () => {
  let component: RemovePagesPdf;
  let fixture: ComponentFixture<RemovePagesPdf>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RemovePagesPdf]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RemovePagesPdf);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
