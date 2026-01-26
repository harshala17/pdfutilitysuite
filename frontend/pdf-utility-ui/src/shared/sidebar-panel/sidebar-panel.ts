import { Component } from '@angular/core';
import { Header } from '../header/header';
import { LandingPage } from '../../pages/landing-page/landing-page';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sidebar-panel',
  imports: [Header,LandingPage, CommonModule],
  templateUrl: './sidebar-panel.html',
  styleUrl: './sidebar-panel.css',
})
export class SidebarPanel {
  isSidebarOpen = false;
  isHighlighted = false;

  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }

  openSidebar() {
    const wasOpen = this.isSidebarOpen;
    this.isSidebarOpen = true;
    if (wasOpen) {
      this.isHighlighted = true;
      setTimeout(() => this.isHighlighted = false, 10000);
    }
  }
}
