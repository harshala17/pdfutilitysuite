import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WordToPdf } from './word-to-pdf';

describe('WordToPdf', () => {
  let component: WordToPdf;
  let fixture: ComponentFixture<WordToPdf>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WordToPdf]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WordToPdf);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
