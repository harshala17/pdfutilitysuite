import { Routes } from '@angular/router';
import { LandingPage } from '../pages/landing-page/landing-page';
import { SidebarPanel } from '../shared/sidebar-panel/sidebar-panel';

export const routes: Routes = [
    {
        path: 'home', component: SidebarPanel
    },
    {
        path: '', redirectTo: 'home', pathMatch: 'full'
    }
];
