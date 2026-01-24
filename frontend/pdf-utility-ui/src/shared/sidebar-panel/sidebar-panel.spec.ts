import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SidebarPanel } from './sidebar-panel';

describe('SidebarPanel', () => {
  let component: SidebarPanel;
  let fixture: ComponentFixture<SidebarPanel>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SidebarPanel]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SidebarPanel);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
