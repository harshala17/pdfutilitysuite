import { Routes } from '@angular/router';
import { LandingPage } from '../pages/landing-page/landing-page';

export const routes: Routes = [
    {
        path: 'home', component: LandingPage
    },
    {
        path: '', redirectTo: 'home', pathMatch: 'full'
    }
];
