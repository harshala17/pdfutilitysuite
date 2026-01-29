import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class Theme {
  private darkClass = 'dark';

  constructor() {
    this.loadTheme();
  }

  toggle(): void {
    const html = document.documentElement;
    const isDark = html.classList.toggle(this.darkClass);
    localStorage.setItem('theme', isDark ? 'dark' : 'light');
  }

  isDark(): boolean {
    return document.documentElement.classList.contains(this.darkClass);
  }

  private loadTheme(): void {
    const saved = localStorage.getItem('theme');

    if (saved === 'dark') {
      document.documentElement.classList.add(this.darkClass);
    }
  }
}
