import { Component, CUSTOM_ELEMENTS_SCHEMA, EventEmitter, Output } from '@angular/core';
import { Footer } from "../../shared/footer/footer";

@Component({
  selector: 'app-landing-page',
  imports: [Footer],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  templateUrl: './landing-page.html',
  styleUrl: './landing-page.css',
})
export class LandingPage {
  @Output() openSidebarEvent = new EventEmitter<void>();

  openSidebar() {
    this.openSidebarEvent.emit();
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
}
